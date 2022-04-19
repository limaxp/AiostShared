package com.pm.aiost.misc.rank;

import net.md_5.bungee.api.ChatColor;

public class Rank {

	private static byte runningID;

	public final byte id;
	public final String name;
	public final String prefix;
	public final byte level;
	public final int color;

	public Rank(String name, String prefix, int level, int color) {
		this.id = runningID++;
		this.name = name.toLowerCase();
		this.prefix = prefix;
		this.level = (byte) level;
		this.color = color;
	}

	public boolean hasRank(byte level) {
		return this.level >= level;
	}

	public boolean hasRank(Rank rank) {
		return this.level >= rank.level;
	}

	public boolean isAdmin() {
		return level >= Ranks.ADMIN.level;
	}

	public boolean isOwner() {
		return level == Ranks.OWNER.level;
	}

	public String prefix(String text) {
		return ChatColor.values()[color] + prefix + text;
	}

	public static class Level {

		public static final byte NONE = 0;
		public static final byte MAX = Byte.MAX_VALUE;
		public static final byte ADMIN = MAX - 1;
	}
}
