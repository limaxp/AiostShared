package com.pm.aiost.collection.map;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ArrayMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

	// TODO: add map index predict functionality

	protected static final int DEFAULT_CAPACITY = 10;

	protected K[] keyArray;
	protected V[] valueArray;
	protected int size;

	public ArrayMap() {
		this(DEFAULT_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public ArrayMap(int capacity) {
		keyArray = (K[]) new Object[capacity];
		valueArray = (V[]) new Object[capacity];
		size = 0;
	}

	public ArrayMap(K[] keyArray, V[] valueArray) {
		this.keyArray = keyArray;
		this.valueArray = valueArray;
		size = keyArray.length;
	}

	public ArrayMap(ArrayMap<K, V> orig) {
		keyArray = orig.keyArray;
		valueArray = orig.valueArray;
		size = orig.size;
	}

	@Override
	public V put(K key, V value) {
		if (size == keyArray.length)
			ensureCapacity(size + 1);
		keyArray[size] = key;
		valueArray[size++] = value;
		return null;
	}

	public int insert(K key, V value) {
		if (size == keyArray.length)
			ensureCapacity(size + 1);
		int index = size++;
		keyArray[index] = key;
		valueArray[index] = value;
		return index;
	}

	public V remove(int index) {
		V element = valueArray[index];
		if (index != --size) {
			System.arraycopy(keyArray, index + 1, keyArray, index, size - index);
			System.arraycopy(valueArray, index + 1, valueArray, index, size - index);
		}
		keyArray[size] = null;
		valueArray[size] = null;
		return element;
	}

	@Override
	public V remove(Object key) {
		int index = indexOfKey(key);
		if (index != -1)
			return remove(index);
		return null;
	}

	@Override
	public V get(Object key) {
		return valueArray[indexOfKey(key)];
	}

	public V get(int index) {
		return valueArray[index];
	}

	public K getKey(Object value) {
		return keyArray[indexOfValue(value)];
	}

	public K getKey(int index) {
		return keyArray[index];
	}

	public int indexOfKey(Object key) {
		int size = this.size;
		for (int i = 0; i < size; i++)
			if (keyArray[i].equals(key))
				return i;
		return -1;
	}

	public int lastIndexOfKey(Object key) {
		for (int i = size; i >= 0; i--)
			if (keyArray[i].equals(key))
				return i;
		return -1;
	}

	public int indexOfValue(Object value) {
		int size = this.size;
		for (int i = 0; i < size; i++)
			if (valueArray[i].equals(value))
				return i;
		return -1;
	}

	public int lastIndexOfValue(Object value) {
		for (int i = size; i >= 0; i--)
			if (valueArray[i].equals(value))
				return i;
		return -1;
	}

	@Override
	public boolean containsKey(Object key) {
		return indexOfKey(key) != -1;
	}

	@Override
	public boolean containsValue(Object value) {
		return indexOfValue(value) != -1;
	}

	@Override
	public void clear() {
		if (size > 0) {
			Arrays.fill(keyArray, 0, size, null);
			Arrays.fill(valueArray, 0, size, null);
			this.size = 0;
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public ArrayMap<K, V> clone() {
		try {
			@SuppressWarnings("unchecked")
			ArrayMap<K, V> clone = (ArrayMap<K, V>) super.clone();
			clone.keyArray = (K[]) keyArray.clone();
			clone.valueArray = (V[]) valueArray.clone();
			clone.size = size;
			return clone;
		} catch (CloneNotSupportedException e) {
		}
		return null;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	protected void ensureCapacity(int minCapacity) {
		int length = keyArray.length;
		if (minCapacity > length)
			resize(Math.max(length * 2, minCapacity));
	}

	@SuppressWarnings("unchecked")
	protected void resize(int size) {
		K[] newArray = (K[]) new Object[size];
		System.arraycopy(keyArray, 0, newArray, 0, this.size);
		keyArray = newArray;

		V[] newArray2 = (V[]) new Object[size];
		System.arraycopy(valueArray, 0, newArray2, 0, this.size);
		valueArray = newArray2;
	}

	@SuppressWarnings("unchecked")
	public void trimToSize() {
		if (size != keyArray.length) {
			K[] newArray = (K[]) new Object[size];
			System.arraycopy(keyArray, 0, newArray, 0, size);
			keyArray = newArray;

			V[] newArray2 = (V[]) new Object[size];
			System.arraycopy(valueArray, 0, newArray2, 0, size);
			valueArray = newArray2;
		}
	}

	public K[] keyArray() {
		return keyArray;
	}

	public V[] valueArray() {
		return valueArray;
	}
}
