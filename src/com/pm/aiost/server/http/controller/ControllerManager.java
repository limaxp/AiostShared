package com.pm.aiost.server.http.controller;

import java.util.HashMap;
import java.util.Map;

import com.pm.aiost.server.http.controller.controller.HomeController;
import com.pm.aiost.server.http.controller.controller.ResourcePackController;

public class ControllerManager {

	private static final Map<String, Controller> CONTROLLER_MAP = new HashMap<String, Controller>();

	public static void initialize() {
//		for (Object obj : ClassUtils.findSubClasses("com.pm.aiost.server.http.controller.controller",
//				Controller.class)) {
//			register((Controller) obj);
//		}
		register(new HomeController());
		register(new ResourcePackController());
	}

	public static void register(Controller controller) {
		CONTROLLER_MAP.put(controller.getClass().getSimpleName().replace("Controller", "").toLowerCase(), controller);
	}

	public static void unregister(Controller controller) {
		CONTROLLER_MAP.remove(controller.getClass().getSimpleName().replace("Controller", "").toLowerCase());
	}

	public static Controller get(String name) {
		return CONTROLLER_MAP.get(name);
	}
}
