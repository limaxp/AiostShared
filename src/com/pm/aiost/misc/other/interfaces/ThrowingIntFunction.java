package com.pm.aiost.misc.other.interfaces;

@FunctionalInterface
public interface ThrowingIntFunction<T, E extends Exception> {

	public T apply(int i) throws E;
}