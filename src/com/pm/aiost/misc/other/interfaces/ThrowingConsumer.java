package com.pm.aiost.misc.other.interfaces;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {

	public void accept(final T elem) throws E;
}
