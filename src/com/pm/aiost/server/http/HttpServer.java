package com.pm.aiost.server.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Date;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.server.http.controller.ControllerManager;

public class HttpServer {

	public static final String ROOT = System.getProperty("user.dir");

	private static ServerSocket serverSocket;
	private static boolean isLogging = false;

	static {
		ControllerManager.initialize();
	}

	public static void start() {
		if (serverSocket == null)
			new Thread(HttpServer::run).start();
	}

	public static void stop() {
		if (serverSocket == null)
			return;
		try {
			serverSocket.close();
		} catch (IOException e) {
			Logger.err("HTTPServer: Error on closing ServerSocket!", e);
		}
	}

	public static void run() {
		try {
			serverSocket = new ServerSocket(0);
			Logger.log("HttpServer: Start listening with port " + getPort());
			while (true) {
				Session session = new Session(serverSocket.accept());
				if (isLogging)
					Logger.log("HttpServer: Connection opened (" + new Date() + ")");
				new Thread(session).start();
			}
		} catch (SocketException e) {
			if (!e.getMessage().equals("Socket closed"))
				Logger.err("SocketServer: Connection error!", e);
		} catch (IOException e) {
			Logger.err("SocketServer: Connection error!", e);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// ignore
			}
		}
		serverSocket = null;
		Logger.log("SocketServer: Stopped listening!");
	}

	public static void setLogging(boolean log) {
		HttpServer.isLogging = log;
	}

	public static boolean isLogging() {
		return isLogging;
	}

	public static InetAddress getAddress() {
		return serverSocket.getInetAddress();
	}

	public static int getPort() {
		return serverSocket.getLocalPort();
	}
}
