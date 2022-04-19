package com.pm.aiost.misc.other.interfaces;

@FunctionalInterface
public interface ThrowingFunction<T1, T2, E extends Exception> {

	public T2 apply(T1 t) throws E;
}