package com.pm.aiost.server.http.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.pm.aiost.server.http.annotations.Route;
import com.pm.aiost.server.http.annotations.Routes;

public abstract class Controller {

	private final Map<String, Method> methodMap;

	public Controller() {
		methodMap = new HashMap<String, Method>();
		loadMethods();
	}

	private void loadMethods() {
		for (Method methods : this.getClass().getMethods()) {
			if (methods.isAnnotationPresent(Routes.class) || methods.isAnnotationPresent(Route.class)) {
				for (Route route : methods.getAnnotationsByType(Route.class))
					methodMap.put(route.value().toLowerCase(), methods);
			}
		}
	}

	public Object invoke(String method, String url, Object[] args) {
		Method meth = methodMap.get(url);
		if (meth != null) {
			String methString = null;
			if (meth.isAnnotationPresent(com.pm.aiost.server.http.annotations.Method.class))
				methString = meth.getAnnotation(com.pm.aiost.server.http.annotations.Method.class).value();
			else
				methString = "GET";
			if (methString.equals(method)) {
				try {
					if (args != null)
						return meth.invoke(this, args);
					else
						return meth.invoke(this);
				} catch (IllegalAccessException | IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.getTargetException().printStackTrace();
				}
			}
		}
		return null;
	}
}
