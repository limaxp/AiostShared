package com.pm.aiost.misc.other;

import java.util.Collection;

public class Arguments {

	public static final Arguments EMPTY = new Arguments();

	public String[] names;
	public Object[] args;

	public Arguments() {
	}

	public Arguments(Collection<String> names, Collection<Object> args) {
		int length = names.size();
		this.names = names.toArray(new String[length]);
		this.args = args.toArray(new Object[length]);
	}

	public Arguments(String[] names, Object[] args) {
		this.names = names;
		this.args = args;
	}

	public Arguments(String name, Object arg) {
		this.names = new String[] { name };
		this.args = new Object[] { arg };
	}

	public int size() {
		return args.length;
	}

	public boolean isEmpty() {
		return args.length > 0;
	}
}
