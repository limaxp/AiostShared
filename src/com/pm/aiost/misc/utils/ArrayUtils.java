package com.pm.aiost.misc.utils;

import java.util.List;

public class ArrayUtils {

	public static String[] toStringArray(Object... objects) {
		int size = objects.length;
		String[] arr = new String[size];
		for (int i = 0; i < size; i++)
			arr[i] = String.valueOf(objects[i]);
		return arr;
	}

	public static int[] toIntArray(List<Integer> list) {
		int size = list.size();
		int[] arr = new int[size];
		for (int i = 0; i < size; i++)
			arr[i] = list.get(i);
		return arr;
	}

	public static float[] toFloatArray(List<Float> list) {
		int size = list.size();
		float[] arr = new float[size];
		for (int i = 0; i < size; i++)
			arr[i] = list.get(i);
		return arr;
	}

	public static String[] combine(String[] arr, List<String> list) {
		int arrLength = arr.length;
		String[] newArr = new String[arrLength + list.size()];
		int i;
		for (i = 0; i < arrLength; i++)
			newArr[i] = arr[i];
		for (String o : list)
			newArr[i++] = o;
		return newArr;
	}

	public static Object[] combine(Object[] arr, List<Object> list) {
		int arrLength = arr.length;
		Object[] newArr = new Object[arrLength + list.size()];
		int i;
		for (i = 0; i < arrLength; i++)
			newArr[i] = arr[i];
		for (Object o : list)
			newArr[i++] = o;
		return newArr;
	}

	public static <T> boolean contains(T[] arr, T o) {
		int size = arr.length;
		for (int j = 0; j < size; j++)
			if (arr[j].equals(o))
				return true;
		return false;
	}

	public static <T> boolean equals(T[] first, T[] second) {
		int size = first.length;
		if (size != second.length)
			return false;
		for (int i = 0; i < size; i++)
			if (first[i] != second[i])
				return false;
		return true;
	}

	public static <T> boolean deepEquals(T[] first, T[] second) {
		int size = first.length;
		if (size != second.length)
			return false;
		for (int i = 0; i < size; i++)
			if (!first[i].equals(second[i]))
				return false;
		return true;
	}
}
