package com.pm.aiost.misc.other.interfaces;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {

	public T get() throws E;
}