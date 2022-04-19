package com.pm.aiost.collection.map;

import java.util.Arrays;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectSet;

public class LongArrayMap<V> extends AbstractLong2ObjectMap<V> implements Long2ObjectMap<V> {

	// TODO: add map index predict functionality

	private static final long serialVersionUID = 6000289806506862817L;

	protected static final int DEFAULT_CAPACITY = 10;

	protected long[] keyArray;
	protected V[] valueArray;
	protected int size;

	public LongArrayMap() {
		this(DEFAULT_CAPACITY);
	}

	@SuppressWarnings("unchecked")
	public LongArrayMap(int capacity) {
		keyArray = new long[capacity];
		valueArray = (V[]) new Object[capacity];
		size = 0;
	}

	public LongArrayMap(long[] keyArray, V[] valueArray) {
		this.keyArray = keyArray;
		this.valueArray = valueArray;
		size = keyArray.length;
	}

	public LongArrayMap(LongArrayMap<V> orig) {
		keyArray = orig.keyArray;
		valueArray = orig.valueArray;
		size = orig.size;
	}

	@Override
	public V put(long key, V value) {
		if (size == keyArray.length)
			ensureCapacity(size + 1);
		keyArray[size] = key;
		valueArray[size++] = value;
		return null;
	}

	public int insert(long key, V value) {
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
		keyArray[size] = 0;
		valueArray[size] = null;
		return element;
	}

	@Override
	public V remove(long key) {
		int index = indexOfKey(key);
		if (index != -1)
			return remove(index);
		return null;
	}

	@Override
	public V get(long key) {
		return valueArray[indexOfKey(key)];
	}

	public V get(int index) {
		return valueArray[index];
	}

	public long getKey(Object value) {
		return keyArray[indexOfValue(value)];
	}

	public long getKey(int index) {
		return keyArray[index];
	}

	public int indexOfKey(long key) {
		int size = this.size;
		for (int i = 0; i < size; i++)
			if (keyArray[i] == key)
				return i;
		return -1;
	}

	public int lastIndexOfKey(long key) {
		for (int i = size; i >= 0; i--)
			if (keyArray[i] == key)
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
	public boolean containsKey(long key) {
		return indexOfKey(key) != -1;
	}

	@Override
	public boolean containsValue(Object value) {
		return indexOfValue(value) != -1;
	}

	@Override
	public void clear() {
		if (size > 0) {
			Arrays.fill(keyArray, 0, size, 0);
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
	public LongArrayMap<V> clone() {
		try {
			@SuppressWarnings("unchecked")
			LongArrayMap<V> clone = (LongArrayMap<V>) super.clone();
			clone.keyArray = keyArray.clone();
			clone.valueArray = (V[]) valueArray.clone();
			clone.size = size;
			return clone;
		} catch (CloneNotSupportedException e) {
		}
		return null;
	}

	@Override
	public ObjectSet<Entry<V>> long2ObjectEntrySet() {
		throw new UnsupportedOperationException();
	}

	protected void ensureCapacity(int minCapacity) {
		int length = keyArray.length;
		if (minCapacity > length)
			resize(Math.max(length * 2, minCapacity));
	}

	@SuppressWarnings("unchecked")
	protected void resize(int size) {
		long[] newArray = new long[size];
		System.arraycopy(keyArray, 0, newArray, 0, this.size);
		keyArray = newArray;

		V[] newArray2 = (V[]) new Object[size];
		System.arraycopy(valueArray, 0, newArray2, 0, this.size);
		valueArray = newArray2;
	}

	@SuppressWarnings("unchecked")
	public void trimToSize() {
		if (size != keyArray.length) {
			long[] newArray = new long[size];
			System.arraycopy(keyArray, 0, newArray, 0, size);
			keyArray = newArray;

			V[] newArray2 = (V[]) new Object[size];
			System.arraycopy(valueArray, 0, newArray2, 0, size);
			valueArray = newArray2;
		}
	}

	public long[] keyArray() {
		return keyArray;
	}

	public V[] valueArray() {
		return valueArray;
	}
}