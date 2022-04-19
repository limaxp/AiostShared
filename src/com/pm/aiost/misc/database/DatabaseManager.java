package com.pm.aiost.misc.database;

import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;

import com.pm.aiost.misc.log.Logger;

import net.md_5.bungee.config.Configuration;

public class DatabaseManager {

	private static @Nullable Database database;

	private static boolean init(String type, String hostName, int port, String database, String userName,
			String password) {
		if (type.isEmpty() || hostName.isEmpty() || port == 0 || database.isEmpty() || userName.isEmpty()) {
			Logger.log("DatabaseManager: Database connection data incomplete! Using FileDataAccess instead!");
			return false;
		}
		DatabaseManager.database = DatabaseType.create(type, hostName, port, database, userName, password);
		return true;
	}

	public static boolean initConnection(ConfigurationSection config) {
		if (!config.contains("database"))
			createDatabaseEntry(config);

		ConfigurationSection databaseSection = config.getConfigurationSection("database");
		if (databaseSection == null)
			databaseSection = createDatabaseEntry(config);

		String type = databaseSection.getString("type");
		String hostName = databaseSection.getString("hostname");
		int port = databaseSection.getInt("port");
		String database = databaseSection.getString("database");
		String userName = databaseSection.getString("username");
		String password = databaseSection.getString("password");
		return init(type, hostName, port, database, userName, password);
	}

	public static boolean initConnection_(Configuration config) {
		if (!config.contains("database"))
			createDatabaseEntry(config);

		Configuration databaseSection = config.getSection("database");
		if (databaseSection == null)
			databaseSection = createDatabaseEntry(config);

		String type = databaseSection.getString("type");
		String hostName = databaseSection.getString("hostname");
		int port = databaseSection.getInt("port");
		String database = databaseSection.getString("database");
		String userName = databaseSection.getString("username");
		String password = databaseSection.getString("password");
		return init(type, hostName, port, database, userName, password);
	}

	private static ConfigurationSection createDatabaseEntry(ConfigurationSection config) {
		Logger.log("DatabaseManager: Database entry generated! ");
		ConfigurationSection newDatabaseConfiguration = config.createSection("database");
		newDatabaseConfiguration.set("type", "");
		newDatabaseConfiguration.set("hostname", "");
		newDatabaseConfiguration.set("port", "");
		newDatabaseConfiguration.set("database", "");
		newDatabaseConfiguration.set("username", "");
		newDatabaseConfiguration.set("password", "");
		return newDatabaseConfiguration;
	}

	private static Configuration createDatabaseEntry(Configuration config) {
		Logger.log("DatabaseManager: Database entry generated! ");
		Configuration newDatabasConfiguration = new Configuration();
		newDatabasConfiguration.set("type", "");
		newDatabasConfiguration.set("hostname", "");
		newDatabasConfiguration.set("port", "");
		newDatabasConfiguration.set("database", "");
		newDatabasConfiguration.set("username", "");
		newDatabasConfiguration.set("password", "");
		config.set("database", newDatabasConfiguration);
		return newDatabasConfiguration;
	}

	public static @Nullable Database getDatabase() {
		return database;
	}
}
