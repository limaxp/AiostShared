package com.pm.aiost.misc.log;

import java.util.function.Supplier;
import java.util.logging.Level;

public class Logger {

	private Logger() {
	}

	private static AbstractLogger logger;

	public static void log(String msg) {
		logger.log(msg);
	}

	public static void log(Object obj) {
		logger.log(obj.toString());
	}

	public static void log(Supplier<String> msg) {
		logger.log(msg);
	}

	public static void warn(String msg) {
		logger.warn(msg);
	}

	public static void warn(Supplier<String> msg) {
		logger.warn(msg);
	}

	public static void err(String msg, Throwable thowable) {
		logger.err(msg, thowable);
	}

	public static void err(Supplier<String> msg, Throwable thowable) {
		logger.err(msg, thowable);
	}

	public static void err(Level level, String msg, Throwable thowable) {
		logger.err(level, msg, thowable);
	}

	public static void err(Level level, Supplier<String> msg, Throwable thowable) {
		logger.err(level, msg, thowable);
	}

	public static void setLogger(AbstractLogger logger) {
		Logger.logger = logger;
	}

	public static AbstractLogger getLogger() {
		return logger;
	}
}
