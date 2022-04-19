package com.pm.aiost.misc.utils.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

	private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

	public static Class<?> getClass(String path) throws ClassNotFoundException {
		return Class.forName(path);
	}

	public static Class<?> getClass(String pkg, String name) throws ClassNotFoundException {
		return Class.forName(pkg + "." + name);
	}

	public static Class<?> getClass(String pkg, String outer, String inner) throws ClassNotFoundException {
		return Class.forName(pkg + '.' + outer + '$' + inner);
	}

	public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException, SecurityException {
		Field field = clazz.getDeclaredField(name);
		field.setAccessible(true);
		return field;
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
			throws NoSuchMethodException, SecurityException {
		Method method = clazz.getMethod(name, parameterTypes);
		method.setAccessible(true);
		return method;
	}

	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes)
			throws NoSuchMethodException, SecurityException {
		Constructor<T> method = clazz.getConstructor(parameterTypes);
		method.setAccessible(true);
		return method;
	}

	public static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz, Class<?>... parameterTypes)
			throws NoSuchMethodException, SecurityException {
		Constructor<T> method = clazz.getDeclaredConstructor(parameterTypes);
		method.setAccessible(true);
		return method;
	}

	public static <T> T newInstance(Constructor<T> constructor, Object... args)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return constructor.newInstance(args);
	}

	public static MethodHandle unreflectMethod(Class<?> clazz, String name, Class<?>... parameterTypes)
			throws IllegalAccessException, NoSuchMethodException, SecurityException {
		return LOOKUP.unreflect(getMethod(clazz, name, parameterTypes));
	}

	public static MethodHandle unreflectMethod(Method method) throws IllegalAccessException {
		return LOOKUP.unreflect(method);
	}

	public static MethodHandle unreflectConstructor(Class<?> clazz, Class<?>... parameterTypes)
			throws IllegalAccessException, NoSuchMethodException, SecurityException {
		return LOOKUP.unreflectConstructor(getDeclaredConstructor(clazz, parameterTypes));
	}

	public static MethodHandle unreflectConstructor(Constructor<?> constructor) throws IllegalAccessException {
		return LOOKUP.unreflectConstructor(constructor);
	}

	public static MethodHandle unreflectGetter(Class<?> clazz, String name)
			throws IllegalAccessException, NoSuchFieldException, SecurityException {
		return LOOKUP.unreflectGetter(getField(clazz, name));
	}

	public static MethodHandle unreflectGetter(Field field) throws IllegalAccessException {
		return LOOKUP.unreflectGetter(field);
	}

	public static MethodHandle unreflectSetter(Class<?> clazz, String name)
			throws IllegalAccessException, NoSuchFieldException, SecurityException {
		return LOOKUP.unreflectSetter(getField(clazz, name));
	}

	public static MethodHandle unreflectSetter(Field field) throws IllegalAccessException {
		return LOOKUP.unreflectSetter(field);
	}

	public static MethodHandle findConstructor(Class<?> clazz, MethodType type)
			throws NoSuchMethodException, IllegalAccessException {
		return LOOKUP.findConstructor(clazz, type);
	}

	public static MethodHandle findGetter(Class<?> clazz, String name, Class<?> type)
			throws NoSuchFieldException, IllegalAccessException {
		return LOOKUP.findGetter(clazz, name, type);
	}

	public static MethodHandle findSetter(Class<?> clazz, String name, Class<?> type)
			throws NoSuchFieldException, IllegalAccessException {
		return LOOKUP.findSetter(clazz, name, type);
	}
}