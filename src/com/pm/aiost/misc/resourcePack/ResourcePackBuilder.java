package com.pm.aiost.misc.resourcePack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

import com.pm.aiost.misc.ConfigManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.FileUtils;

import net.md_5.bungee.config.Configuration;

public class ResourcePackBuilder {

	private static final int PACK_FORMAT = 5;

	public static void checkResourcePack(FileConfiguration itemsConfig) {
		File resourcePackFolder = getResourcePackFolder();
		if (resourcePackFolder == null)
			return;

		if (hasChangedItemsConfig(resourcePackFolder))
			writeResourcePack(itemsConfig, resourcePackFolder);
		updateResourcePackZip(resourcePackFolder);
	}

	public static void checkResourcePack_(Configuration itemsConfig) {
		File resourcePackFolder = getResourcePackFolder();
		if (resourcePackFolder == null)
			return;

		if (hasChangedItemsConfig(resourcePackFolder))
			writeResourcePack_(itemsConfig, resourcePackFolder);
		updateResourcePackZip(resourcePackFolder);
	}

	private static File getResourcePackFolder() {
		File resourcePackFolder = new File(ConfigManager.getAiostFolderPath(), "resourcePack");
		if (!resourcePackFolder.exists() || resourcePackFolder.getTotalSpace() == 0) {
			resourcePackFolder.mkdir();
			Logger.warn("ResourcePackBuilder: No resource pack folder found! Cancel resource pack creation");
			return null;
		}
		return resourcePackFolder;
	}

	private static boolean hasChangedItemsConfig(File resourcePackFolder) {
		File itemConfigFile = new File(ConfigManager.getConfigFolderPath(), "Items.yml");
		if (itemConfigFile.lastModified() > resourcePackFolder.lastModified()) {
			Logger.log("ResourcePackBuilder: Items config was modified!");
			return true;
		}
		Logger.log("ResourcePackBuilder: Resource pack folder is up to date");
		return false;
	}

	private static void writeResourcePack(FileConfiguration itemList, File resourcePackFolder) {
		Logger.log("ResourcePackBuilder: Start updating resource pack...");
		new ResourcePackWriter(resourcePackFolder.getAbsolutePath()).writeItemJasons(itemList);
		resourcePackFolder.setLastModified(System.currentTimeMillis());
		Logger.log("ResourcePackBuilder: Finished updating resource pack!");
	}

	private static void writeResourcePack_(Configuration itemList, File resourcePackFolder) {
		Logger.log("ResourcePackBuilder: Start updating resource pack...");
		new ResourcePackWriter(resourcePackFolder.getAbsolutePath()).writeItemJasons_(itemList);
		resourcePackFolder.setLastModified(System.currentTimeMillis());
		Logger.log("ResourcePackBuilder: Finished updating resource pack!");
	}

	private static void updateResourcePackZip(File resourcePackFolder) {
		File resourcePackZip = new File(ConfigManager.getAiostFolderPath(), "resourcePack.zip");
		if (!resourcePackZip.exists())
			createResourcePackZip(resourcePackFolder, resourcePackZip);

		else if (resourcePackFolder.lastModified() > resourcePackZip.lastModified()) {
			Logger.log("ResourcePackBuilder: Resource pack zip needs update!");
			resourcePackZip.delete();
			createResourcePackZip(resourcePackFolder, resourcePackZip);
		}

		else
			Logger.log("ResourcePackBuilder: Resource pack zip is up to date");
	}

	private static void createResourcePackZip(File resourcePackFolder, File resourcePackZip) {
		Logger.log("ResourcePackBuilder: Start resource pack zip creation...");
		try {
			FileUtils.zipDir(resourcePackFolder, resourcePackZip, "/", false);
		} catch (IOException e) {
			Logger.err("ResourcePackBuilder: Error on resource pack zip creation!", e);
		}
		Logger.log("ResourcePackBuilder: Finished resource pack zip creation!");
	}

	public static void checkDefaultPack() {
		File defaultPackZip = new File(ConfigManager.getAiostFolderPath(), "defaultPack.zip");
		if (defaultPackZip.exists())
			Logger.log("ResourcePackBuilder: Default ResourcePack found!");
		else {
			Logger.log("ResourcePackBuilder: Default ResourcePack was not found! Start creation...");
			createDefaultPack(defaultPackZip);
			Logger.log("ResourcePackBuilder: Finished default resource pack creation...");
		}
	}

	private static void createDefaultPack(File defaultPackZip) {
		File defaultPackFolder = new File(ConfigManager.getAiostFolderPath(), "defaultPack");
		defaultPackFolder.mkdir();
		createMcmeta(defaultPackFolder);
		createResourcePackZip(defaultPackFolder, defaultPackZip);
		deleteDefaultPackFolder(defaultPackFolder);
	}

	private static void createMcmeta(File defaultPackFolder) {
		File mcmetaFile = new File(defaultPackFolder, "pack.mcmeta");
		try {
			if (mcmetaFile.createNewFile())
				writeMcmeta(mcmetaFile);
		} catch (IOException e) {
			Logger.err("ResourcePackBuilder: Error on pack.mcmeta creation", e);
		}
	}

	private static void writeMcmeta(File mcmetaFile) {
		try {
			FileUtils.writeFile(mcmetaFile.getAbsolutePath(), "{\r\n" + "  \"pack\": {\r\n" + "    \"pack_format\": "
					+ PACK_FORMAT + ",\r\n" + "    \"description\": \"Default resourcepack\"\r\n" + "  }\r\n" + "}");
		} catch (FileNotFoundException e) {
			Logger.err("ResourcePackBuilder: Error on writing pack.mcmeta", e);
		}
	}

	private static void deleteDefaultPackFolder(File defaultPackFolder) {
		try {
			FileUtils.delete(defaultPackFolder);
		} catch (IOException e) {
			Logger.err("ResourcePackBuilder: Error on deleting default pack folder", e);
		}
	}
}