package com.pm.aiost.misc.other.key;

public class IndexKey {

	public final int index;

	public IndexKey(int index) {
		this.index = index;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}

	@Override
	public int hashCode() {
		return index;
	}
}
