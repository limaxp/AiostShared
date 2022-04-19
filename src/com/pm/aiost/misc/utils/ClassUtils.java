package com.pm.aiost.misc.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.pm.aiost.misc.log.Logger;

public class ClassUtils {

	public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
		try {
			return clazz.getMethod(name, parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.err("ClassUtils: No method found for name '" + name + "'", e);
		}
		return null;
	}

	public static Vector<Class<?>> getClassList(ClassLoader CL)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class<?> CL_class = CL.getClass();
		while (CL_class != ClassLoader.class) {
			CL_class = CL_class.getSuperclass();
		}
		Field ClassLoader_classes_field = CL_class.getDeclaredField("classes");
		ClassLoader_classes_field.setAccessible(true);
		@SuppressWarnings("unchecked")
		Vector<Class<?>> classes = (Vector<Class<?>>) ClassLoader_classes_field.get(CL);
		return classes;
	}

	/**
	 * DOES NOT WORK IN SERVER APPLICATIONS!!
	 * 
	 * Attempts to list all the classes in the specified package as determined by
	 * the context class loader
	 * 
	 * @param pckgname the package name to search
	 * @return a list of classes that exist within that package
	 * @throws ClassNotFoundException if something went wrong
	 */
	public static ArrayList<Class<?>> getPackageClasses(String pckgname) throws ClassNotFoundException {
		final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

		try {
			final ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null)
				throw new ClassNotFoundException("Can't get class loader.");

			final Enumeration<URL> resources = cld.getResources(pckgname.replace('.', '/'));
			URLConnection connection;
			for (URL url = null; resources.hasMoreElements() && ((url = resources.nextElement()) != null);) {
				try {
					connection = url.openConnection();

					if (connection instanceof JarURLConnection) {
						checkJarFile((JarURLConnection) connection, pckgname, cld, classes);
					} else if (connection instanceof HttpURLConnection) {
						try {
							checkDirectory(new File(URLDecoder.decode(url.getPath(), "UTF-8")), pckgname, cld, classes);
						} catch (final UnsupportedEncodingException ex) {
							throw new ClassNotFoundException(
									pckgname + " does not appear to be a valid package (Unsupported encoding)", ex);
						}
					} else
						throw new ClassNotFoundException(
								pckgname + " (" + url.getPath() + ") does not appear to be a valid package");
				} catch (final IOException ioex) {
					throw new ClassNotFoundException(
							"IOException was thrown when trying to get all resources for " + pckgname, ioex);
				}
			}
		} catch (final NullPointerException ex) {
			throw new ClassNotFoundException(
					pckgname + " does not appear to be a valid package (Null pointer exception)", ex);
		} catch (final IOException ioex) {
			throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname,
					ioex);
		}

		return classes;
	}

	private static void checkDirectory(File directory, String pckgname, ClassLoader cld, ArrayList<Class<?>> classes)
			throws ClassNotFoundException {
		File tmpDirectory;

		if (directory.exists() && directory.isDirectory()) {
			final String[] files = directory.list();

			for (final String file : files) {
				if (file.endsWith(".class")) {
					try {
						classes.add(Class.forName(pckgname + '.' + file.substring(0, file.length() - 6), false, cld));
					} catch (final NoClassDefFoundError e) {
						// do nothing. this class hasn't been found by the
						// loader, and we don't care.
					}
				} else if ((tmpDirectory = new File(directory, file)).isDirectory()) {
					checkDirectory(tmpDirectory, pckgname + "." + file, cld, classes);
				}
			}
		}
	}

	private static void checkJarFile(JarURLConnection connection, String pckgname, ClassLoader cld,
			ArrayList<Class<?>> classes) throws ClassNotFoundException, IOException {
		final JarFile jarFile = connection.getJarFile();
		final Enumeration<JarEntry> entries = jarFile.entries();
		String name;

		for (JarEntry jarEntry = null; entries.hasMoreElements() && ((jarEntry = entries.nextElement()) != null);) {
			name = jarEntry.getName();
			if (name.contains(".class")) {
				name = name.substring(0, name.length() - 6).replace('/', '.');
				if (name.contains(pckgname)) {
					classes.add(Class.forName(name, false, cld));
				}
			}
		}
	}
}
