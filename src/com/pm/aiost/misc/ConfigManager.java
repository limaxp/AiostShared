package com.pm.aiost.misc;

import java.io.File;
import java.io.IOException;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.FileUtils;

public abstract class ConfigManager {

	private static String configFolderPath;
	private static String aiostFolderPath;

	protected static File initResource(String configName) {
		String filePath = configFolderPath + File.separator + configName;
		File file = new File(filePath);
		if (!file.exists())
			createResourceFile(configName, filePath);
		return file;
	}

	private static void createResourceFile(String resourceName, String path) {
		Logger.log("ConfigManager: Resource file '" + resourceName + "' is missing, start copy to " + path);
		try {
			FileUtils.writeResourceFile(ConfigManager.class.getClassLoader(), resourceName, path);
		} catch (IOException e) {
			Logger.err("ConfigManager: Could not write resource file '" + resourceName + "'" + " to " + path, e);
		}
	}

	public static void initAiostFolderPath(String aiostFolderPath) {
		if (ConfigManager.aiostFolderPath != null)
			return;

		String configFolderPath = aiostFolderPath + File.separator + "config";
		FileUtils.createNotExistingFolder(new File(aiostFolderPath));
		FileUtils.createNotExistingFolder(new File(configFolderPath));
		ConfigManager.aiostFolderPath = aiostFolderPath;
		ConfigManager.configFolderPath = configFolderPath;
	}

	public static String getAiostFolderPath() {
		return aiostFolderPath;
	}

	public static String getConfigFolderPath() {
		return configFolderPath;
	}
}