package com.pm.aiost.misc.other.key;

public class Key {

	public final byte[] key;
	private int hash;

	public Key(byte[] key) {
		this.key = key;
	}

	public boolean equals(String string) {
		int length = key.length;
		if (length != string.length())
			return false;

		for (int i = 0; i < length; i++) {
			if (key[i] != (byte) string.charAt(i))
				return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}

	@Override
	public int hashCode() {
		int h = hash;
		if (h == 0 && key.length > 0) {
			byte val[] = key;
			for (int i = 0; i < key.length; i++)
				h = 31 * h + val[i];
			hash = h;
		}
		return h;

	}
}
