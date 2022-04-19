package com.pm.aiost.collection.iterator;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ArrayIterator<T> implements ListIterator<T>, Iterable<T> {

	private T[] array;
	private int pos;

	public ArrayIterator(T array[]) {
		this.array = array;
		this.pos = -1;
	}

	@Override
	public boolean hasNext() {
		return pos <= array.length;
	}

	@Override
	public T next() throws NoSuchElementException {
		return array[++pos];
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(T t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasPrevious() {
		return pos >= 0;
	}

	@Override
	public int nextIndex() {
		return pos + 1;
	}

	@Override
	public T previous() throws NoSuchElementException {
		return array[--pos];
	}

	@Override
	public int previousIndex() {
		return pos - 1;
	}

	@Override
	public void set(T t) {
		array[pos] = t;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}
}