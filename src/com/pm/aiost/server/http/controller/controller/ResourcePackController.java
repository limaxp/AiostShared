package com.pm.aiost.server.http.controller.controller;

import java.io.File;

import com.pm.aiost.server.http.HttpServer;
import com.pm.aiost.server.http.Response;
import com.pm.aiost.server.http.annotations.Method;
import com.pm.aiost.server.http.annotations.Route;
import com.pm.aiost.server.http.controller.Controller;

public class ResourcePackController extends Controller {

	@Route("ResourcePack/ResourcePack.zip")
	@Method("GET")
	public void resourcePack() {
		File file = new File(HttpServer.ROOT + File.separator + "Plugins" + File.separator + "aiost",
				"resourcePack.zip");
		if (file.exists())
			Response.sendFile(file);
	}

	@Route("ResourcePack/DefaultPack.zip")
	@Method("GET")
	public void defaultPack() {
		File file = new File(HttpServer.ROOT + File.separator + "Plugins" + File.separator + "aiost",
				"defaultPack.zip");
		if (file.exists())
			Response.sendFile(file);
	}
}
