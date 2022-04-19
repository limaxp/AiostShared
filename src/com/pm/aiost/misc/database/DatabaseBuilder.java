package com.pm.aiost.misc.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.pm.aiost.misc.ConfigManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.FileUtils;

public class DatabaseBuilder {

	private static final String BUILD_QUERRY_PATH = ConfigManager.getAiostFolderPath() + File.separator + "db.sql";

	public static void buildDatabase() {
		Logger.log("DatabaseBuilder: start building database...");
		DatabaseManager.getDatabase().batch(ScriptReader.scriptToBatchList(loadBuildQuerry()));
		Logger.log("DatabaseBuilder: database building complete!");
	}

	private static String loadBuildQuerry() {
		File buildQueryFile = new File(BUILD_QUERRY_PATH);
		String buildQuerry = null;
		if (buildQueryFile.exists()) {
			try {
				buildQuerry = new String(FileUtils.readFile(BUILD_QUERRY_PATH), Charset.defaultCharset());
			} catch (IOException e) {
				Logger.err("DatabaseBuilder: could not load db.sql file!", e);
			}
		} else {
			try (InputStream in = DatabaseBuilder.class.getClassLoader().getResourceAsStream("db.sql")) {
				buildQuerry = FileUtils.streamToString(in);
			} catch (IOException e) {
				Logger.err("DatabaseBuilder: could not load db.sql resource file!", e);
			}

			try {
				FileUtils.writeFile(BUILD_QUERRY_PATH, buildQuerry);
			} catch (FileNotFoundException e) {
				Logger.err("DatabaseBuilder: could not write db.sql file!", e);
			}
		}
		return buildQuerry;
	}
}
