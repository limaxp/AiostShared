package com.pm.aiost.main;

import com.pm.aiost.misc.other.DataManager;

import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin {

	private static BungeePlugin plugin;

	@Override
	public void onEnable() {
		plugin = this;
	}

	@Override
	public void onDisable() {
		DataManager.save();
	}

	public static BungeePlugin getPlugin() {
		return plugin;
	}
}
