package com.pm.aiost.main;

import org.bukkit.plugin.java.JavaPlugin;

import com.pm.aiost.misc.other.DataManager;

public class SpigotPlugin extends JavaPlugin {

	private static SpigotPlugin plugin;

	@Override
	public void onEnable() {
		plugin = this;
	}

	@Override
	public void onDisable() {
		DataManager.save();
	}

	public static SpigotPlugin getPlugin() {
		return plugin;
	}
}