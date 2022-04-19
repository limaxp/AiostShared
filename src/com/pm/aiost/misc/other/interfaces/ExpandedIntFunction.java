package com.pm.aiost.misc.other.interfaces;

public interface ExpandedIntFunction<T> extends ThrowingIntFunction<T, Exception> {

	@Override
	T apply(int i);
}
