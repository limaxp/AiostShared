package com.pm.aiost.server.http.controller.controller;

import com.pm.aiost.server.http.Response;
import com.pm.aiost.server.http.annotations.Method;
import com.pm.aiost.server.http.annotations.Route;
import com.pm.aiost.server.http.controller.Controller;

public class HomeController extends Controller {

	@Route("")
	@Route("index.html")
	@Route("Home")
	@Route("Home/Index")
	@Method("GET")
	public void index() {
		Response.sendPage("home/index.html");
	}

	@Route("Add")
	@Route("Home/Add")
	@Method("GET")
	public void add(int a, int b) {
		Response.sendInt(a + b);
	}

	@Route("HelloWorld")
	@Route("Home/HelloWorld")
	@Method("GET")
	public void helloWorld() {
		Response.sendString("Hello World");
	}

	@Route("Home/image")
	@Method("GET")
	public void image() {
		Response.sendResource("image/jpg", "./resource/imageKS-6-1.jpg");
	}
}
