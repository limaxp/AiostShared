package com.pm.aiost.misc.rank;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.collection.list.IdentityArrayList;
import com.pm.aiost.misc.rank.Rank.Level;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

public class Ranks {

	private static final List<Rank> RANKS = new IdentityArrayList<Rank>();
	private static final List<Rank> RANKS_VIEW = Collections.unmodifiableList(RANKS);
	private static final Map<String, Rank> NAME_MAP = new HashMap<String, Rank>();

	public static final Rank NONE = a(new Rank("none", "", Level.NONE, ChatColor.GRAY.ordinal()));

	public static final Rank ADMIN = a(new Rank("admin", "[admin] ", Level.ADMIN, ChatColor.RED.ordinal()));

	public static final Rank OWNER = a(new Rank("owner", "[owner] ", Level.MAX, ChatColor.BLACK.ordinal()));

	public static Rank a(Rank rank) {
		register(rank);
		return rank;
	}

	public static void register(Rank rank) {
		RANKS.add(rank);
		NAME_MAP.put(rank.name, rank);
	}

	public static void register_(Configuration ranksSection) {
		Collection<String> rankNames = ranksSection.getKeys();
		Configuration rankSection;
		for (String rankName : rankNames) {
			rankSection = ranksSection.getSection(rankName);
			register(new Rank(rankName, rankSection.getString("prefix"), rankSection.getInt("level"),
					ChatColor.valueOf(rankSection.getString("color").toUpperCase()).ordinal()));
		}
	}

	public static void register(ConfigurationSection ranksSection) {
		Set<String> rankNames = ranksSection.getKeys(false);
		ConfigurationSection rankSection;
		for (String rankName : rankNames) {
			rankSection = ranksSection.getConfigurationSection(rankName);
			register(new Rank(rankName, rankSection.getString("prefix"), rankSection.getInt("level"),
					ChatColor.valueOf(rankSection.getString("color").toUpperCase()).ordinal()));
		}
	}

	public static Rank get(int id) {
		return RANKS.get(id);
	}

	public static Rank get(String name) {
		return NAME_MAP.get(name);
	}

	public static Rank getOrDefault(String name, Rank rank) {
		return NAME_MAP.getOrDefault(name, rank);
	}

	public static Rank getIgnoreCase(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}

	public static Rank getIgnoreCaseOrDefault(String name, Rank rank) {
		return NAME_MAP.getOrDefault(name.toLowerCase(), rank);
	}

	public static int size() {
		return RANKS.size();
	}

	public static List<Rank> values() {
		return RANKS_VIEW;
	}
}
