package com.pm.aiost.server.http.annotations;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Repeatable(Routes.class)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Route {
	String value();
}
