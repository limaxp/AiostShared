package com.pm.aiost.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.StringUtils;
import com.pm.aiost.server.http.controller.Controller;
import com.pm.aiost.server.http.controller.ControllerManager;

public class Session implements Runnable {

	private static final ThreadLocal<Socket> SOCKET = new ThreadLocal<Socket>() {
		@Override
		protected Socket initialValue() {
			return null;
		}
	};

	private final Socket socket;

	public Session(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		SOCKET.set(socket);
		BufferedReader in = null;
		String url = null;
		String[] urlSplit = null;
		Object[] args = null;

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String input = in.readLine();
			StringTokenizer parse = new StringTokenizer(input);
			String method = parse.nextToken().toUpperCase();
			url = parse.nextToken().toLowerCase();
			url = url.replaceFirst("/", "");
			int urlLength = url.length() - 1;
			if (urlLength > -1 && url.charAt(urlLength) == '/')
				url = url.substring(0, urlLength);
			urlSplit = url.split("/");

			if (url.contains("?")) {
				String[] urlSplit2 = url.split("\\?");
				url = urlSplit2[0];
				String argumentString = urlSplit2[1];
				if (argumentString.contains("&")) {
					String[] argumentSplit = argumentString.split("&");
					args = new Object[argumentSplit.length];
					int i = 0;
					for (String argument : argumentSplit) {
						args[i++] = castObjectType(argument);
					}
				} else {
					args = new Object[1];
					args[0] = argumentString;
				}
			}

			Controller controller;
			if (urlSplit.length > 1)
				controller = ControllerManager.get(urlSplit[0]);
			else
				controller = ControllerManager.get("home");
			if (controller == null)
				controller = ControllerManager.get("home");
			controller.invoke(method, url, args);

		} catch (IOException e) {
			Logger.err("Session: Error!", e);
		} finally {
			try {
				in.close();
				socket.close();
			} catch (Exception e) {
				Logger.err("Session: Error closing socket!", e);
			}

			if (HttpServer.isLogging())
				Logger.log("Session: Connection closed");
		}
	}

	private static Object castObjectType(String str) {
		if (StringUtils.isDouble(str))
			return Double.parseDouble(str);
		else if (StringUtils.isFloat(str))
			return Float.parseFloat(str);
		else if (StringUtils.isInteger(str))
			return Integer.parseInt(str);
		else if (StringUtils.isShort(str))
			return Short.parseShort(str);
		else if (StringUtils.isByte(str))
			return Byte.parseByte(str);
		return str;
	}

	public static Socket socket() {
		return SOCKET.get();
	}
}
