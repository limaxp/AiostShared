package com.pm.aiost.misc.log;

import java.util.function.Supplier;
import java.util.logging.Level;

public abstract class AbstractLogger {

	protected AbstractLogger() {
	}

	public abstract void log(String msg);

	public abstract void log(Supplier<String> msg);

	public abstract void warn(String msg);

	public abstract void warn(Supplier<String> msg);

	public abstract void err(String msg, Throwable throwable);

	public abstract void err(Supplier<String> msg, Throwable throwable);

	public abstract void err(Level lvl, String msg, Throwable throwable);

	public abstract void err(Level lvl, Supplier<String> msg, Throwable throwable);
}
