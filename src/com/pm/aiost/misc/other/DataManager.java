package com.pm.aiost.misc.other;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.pm.aiost.misc.ConfigManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.FileUtils;

@SuppressWarnings("unchecked")
public class DataManager {

	private static final int CACHE_SIZE = 1024;

	protected static final File DATA_DIR;
	protected static final File PLAYER_DIR;
	protected static final File WORLD_DIR;
	protected static final File GAME_DIR;
	protected static final File PLAYER_MAP_DIR;
	protected static final File GAME_MAP_DIR;

	private static final Long2ObjectLinkedOpenHashMap<JSONObject> PLAYER_CACHE = new Long2ObjectLinkedOpenHashMap<JSONObject>();
	private static final Object2ObjectLinkedOpenHashMap<UUID, JSONObject> WORLD_CACHE = new Object2ObjectLinkedOpenHashMap<UUID, JSONObject>();
	private static final Object2ObjectLinkedOpenHashMap<UUID, JSONObject> GAME_CACHE = new Object2ObjectLinkedOpenHashMap<UUID, JSONObject>();

	private static final Object2ObjectLinkedOpenHashMap<String, Long> PLAYER_NAME_MAP = new Object2ObjectLinkedOpenHashMap<String, Long>();

	private static final File GAME_TYPE_META_FILE;
	private static JSONObject gameTypeMeta;

	static {
		FileUtils.createNotExistingFolder(DATA_DIR = new File(ConfigManager.getAiostFolderPath(), "data"));
		FileUtils.createNotExistingFolder(PLAYER_DIR = new File(DATA_DIR, "player"));
		FileUtils.createNotExistingFolder(WORLD_DIR = new File(DATA_DIR, "worlds"));
		FileUtils.createNotExistingFolder(GAME_DIR = new File(DATA_DIR, "games"));

		FileUtils.createNotExistingFolder(PLAYER_MAP_DIR = new File(PLAYER_DIR, "_map"));
		FileUtils.createNotExistingFolder(GAME_MAP_DIR = new File(GAME_DIR, "_map"));

		GAME_TYPE_META_FILE = new File(GAME_MAP_DIR, "type_Meta.json");
		initGameTypeMeta();
	}

	public static void save() {
		for (org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry<JSONObject> entry : PLAYER_CACHE
				.long2ObjectEntrySet())
			savePlayer(entry.getLongKey(), entry.getValue());

		for (Entry<UUID, JSONObject> entry : WORLD_CACHE.entrySet())
			saveWorld(entry.getKey(), entry.getValue());

		for (Entry<UUID, JSONObject> entry : GAME_CACHE.entrySet())
			saveGame(entry.getKey(), entry.getValue());

		saveTypeMeta();
	}

	public static File getPlayerFile(long id) {
		return new File(PLAYER_DIR, Long.toString(id) + ".json");
	}

	public static long generatePlayerID() {
		long id;
		do
			id = new Random().nextLong();
		while (getPlayer(id) != null);
		return id;
	}

	public static JSONObject createPlayer(long id) {
		JSONObject json = new JSONObject();
		if (PLAYER_CACHE.size() >= CACHE_SIZE)
			savePlayer(id, PLAYER_CACHE.removeLast());
		PLAYER_CACHE.put(id, json);
		return json;
	}

	public static JSONObject getPlayer(long id) {
		JSONObject json = PLAYER_CACHE.get(id);
		if (json != null)
			return json;
		try {
			json = (JSONObject) FileUtils.readJson(getPlayerFile(id));
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			Logger.err("DataManager: Error on loading player '" + id + "' file!", e);
			return null;
		} catch (ParseException e) {
			Logger.err("DataManager: Error on parsing player '" + id + "' file!", e);
			return null;
		}
		if (PLAYER_CACHE.size() >= CACHE_SIZE)
			savePlayer(id, PLAYER_CACHE.removeLast());
		PLAYER_CACHE.put(id, json);
		return json;
	}

	private static void savePlayer(long id, JSONObject json) {
		try {
			FileUtils.writeJson(getPlayerFile(id), json);
		} catch (IOException e) {
			Logger.err("DataManager: Error on writing player '" + id + "' file!", e);
		}
	}

	public static long getOrAddPlayerID(String name) {
		Long id = PLAYER_NAME_MAP.get(name);
		if (id == null) {
			File mapFile = getPlayerNameMapFile(name);
			JSONObject mapJson = getPlayerNameMap(mapFile);
			if (!mapJson.containsKey(name)) {
				mapJson.put(name, id = generatePlayerID());
				try {
					FileUtils.writeJson(mapFile, mapJson);
				} catch (IOException e) {
					Logger.err("DataManager: Error on writing player '" + name + "' map file!", e);
				}
			} else
				id = (Long) mapJson.get(name);

			if (PLAYER_NAME_MAP.size() >= CACHE_SIZE)
				PLAYER_NAME_MAP.removeLast();
			PLAYER_NAME_MAP.put(name, id);
		}
		return id;
	}

	public static long getPlayerID(String name) {
		Long id = PLAYER_NAME_MAP.get(name);
		if (id == null) {
			JSONObject mapJson = getPlayerNameMap(name);
			if (!mapJson.containsKey(name))
				return -1;
			id = (Long) mapJson.get(name);
			if (PLAYER_NAME_MAP.size() >= CACHE_SIZE)
				PLAYER_NAME_MAP.removeLast();
			PLAYER_NAME_MAP.put(name, id);
		}
		return id;
	}

	public static File getPlayerNameMapFile(String name) {
		String fileName = Character.toString(name.charAt(0)) + name.charAt(1) + name.charAt(2);
		File mapFile = new File(PLAYER_MAP_DIR, fileName + ".json");
		if (!mapFile.exists()) {
			try {
				FileUtils.writeJson(mapFile, new JSONObject());
			} catch (IOException e) {
				Logger.err("DataManager: Error on creating player '" + name + "' map file!", e);
			}
		}
		return mapFile;
	}

	public static JSONObject getPlayerNameMap(String name) {
		return getPlayerNameMap(getPlayerNameMapFile(name));
	}

	public static JSONObject getPlayerNameMap(File mapFile) {
		JSONObject json;
		try {
			json = (JSONObject) FileUtils.readJson(mapFile);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			Logger.err("DataManager: Error on loading player map file '" + mapFile.getName() + "'", e);
			return null;
		} catch (ParseException e) {
			Logger.err("DataManager: Error on parsing player map file '" + mapFile.getName() + "'", e);
			return null;
		}
		return json;
	}

	public static JSONArray getUnlockables(long playerID, int typeID) {
		return getUnlockables(getPlayer(playerID), typeID);
	}

	public static JSONArray getUnlockables(JSONObject json, int typeID) {
		JSONArray typesJson = (JSONArray) json.get("unlockableTypes");
		JSONArray valuesJson = (JSONArray) json.get("unlockableValues");
		if (typesJson == null) {
			json.put("unlockableTypes", typesJson = new JSONArray());
			json.put("unlockableValues", valuesJson = new JSONArray());
		}
		int index = typesJson.indexOf((long) typeID);
		if (index == -1) {
			index = typesJson.size();
			typesJson.add((long) typeID);
			valuesJson.add(new JSONArray());
		}
		return (JSONArray) valuesJson.get(index);
	}

	public static JSONArray getPermissions(long playerID) {
		return getPermissions(getPlayer(playerID));
	}

	public static JSONArray getPermissions(JSONObject json) {
		JSONArray permissionsJson = (JSONArray) json.get("permissions");
		if (permissionsJson == null)
			json.put("permissions", permissionsJson = new JSONArray());
		return permissionsJson;
	}

	public static JSONArray getSettings(long playerID) {
		return getSettings(getPlayer(playerID));
	}

	public static JSONArray getSettings(JSONObject json) {
		JSONArray settingsJson = (JSONArray) json.get("settings");
		if (settingsJson == null)
			json.put("settings", settingsJson = new JSONArray());
		return settingsJson;
	}

	public static JSONArray getSettingValues(long playerID) {
		return getSettings(getPlayer(playerID));
	}

	public static JSONArray getSettingValues(JSONObject json) {
		JSONArray settingValuesJson = (JSONArray) json.get("settingValues");
		if (settingValuesJson == null)
			json.put("settingValues", settingValuesJson = new JSONArray());
		return settingValuesJson;
	}

	public static JSONArray getFriends(long playerID) {
		return getFriends(getPlayer(playerID));
	}

	public static JSONArray getFriends(JSONObject json) {
		JSONArray freindsJson = ((JSONArray) json.get("friends"));
		if (freindsJson == null)
			json.put("friends", freindsJson = new JSONArray());
		return freindsJson;
	}

	public static JSONArray getFriendRequests(long playerID) {
		return getFriendRequests(getPlayer(playerID));
	}

	public static JSONArray getFriendRequests(JSONObject json) {
		JSONArray freindRequestsJson = ((JSONArray) json.get("friendRequests"));
		if (freindRequestsJson == null)
			json.put("friendRequests", freindRequestsJson = new JSONArray());
		return freindRequestsJson;
	}

	public static JSONArray getFriendRequestDates(long playerID) {
		return getFriendRequestDates(getPlayer(playerID));
	}

	public static JSONArray getFriendRequestDates(JSONObject json) {
		JSONArray freindRequestDatesJson = ((JSONArray) json.get("friendRequestDates"));
		if (freindRequestDatesJson == null)
			json.put("friendRequestDates", freindRequestDatesJson = new JSONArray());
		return freindRequestDatesJson;
	}

	public static JSONArray getSentFriendRequests(long playerID) {
		return getSentFriendRequests(getPlayer(playerID));
	}

	public static JSONArray getSentFriendRequests(JSONObject json) {
		JSONArray freindRequestsJson = ((JSONArray) json.get("sentFriendRequests"));
		if (freindRequestsJson == null)
			json.put("sentFriendRequests", freindRequestsJson = new JSONArray());
		return freindRequestsJson;
	}

	public static JSONArray getSentFriendRequestDates(long playerID) {
		return getSentFriendRequestDates(getPlayer(playerID));
	}

	public static JSONArray getSentFriendRequestDates(JSONObject json) {
		JSONArray sentFreindRequestDatesJson = ((JSONArray) json.get("sentFriendRequestDates"));
		if (sentFreindRequestDatesJson == null)
			json.put("sentFriendRequestDates", sentFreindRequestDatesJson = new JSONArray());
		return sentFreindRequestDatesJson;
	}

	public static File getWorldFile(UUID uuid) {
		return new File(WORLD_DIR, uuid.toString() + ".json");
	}

	public static UUID createWorld() {
		UUID uuid;
		do
			uuid = UUID.randomUUID();
		while (getWorld(uuid) != null);
		if (WORLD_CACHE.size() >= CACHE_SIZE)
			saveWorld(uuid, WORLD_CACHE.removeLast());
		WORLD_CACHE.put(uuid, new JSONObject());
		return uuid;
	}

	public static void deleteWorld(UUID uuid) {
		WORLD_CACHE.remove(uuid);
		getWorldFile(uuid).delete();
	}

	public static JSONObject getWorld(UUID uuid) {
		JSONObject json = WORLD_CACHE.get(uuid);
		if (json != null)
			return json;
		try {
			json = (JSONObject) FileUtils.readJson(getWorldFile(uuid));
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			Logger.err("DataManager: Error on loading world '" + uuid + "' file!", e);
			return null;
		} catch (ParseException e) {
			Logger.err("DataManager: Error on parsing world '" + uuid + "' file!", e);
			return null;
		}
		if (WORLD_CACHE.size() >= CACHE_SIZE)
			saveWorld(uuid, WORLD_CACHE.removeLast());
		WORLD_CACHE.put(uuid, json);
		return json;
	}

	private static void saveWorld(UUID uuid, JSONObject json) {
		try {
			FileUtils.writeJson(getWorldFile(uuid), json);
		} catch (IOException e) {
			Logger.err("DataManager: Error on writing world '" + uuid + "' file!", e);
		}
	}

	public static JSONArray getWorlds(long playerID) {
		return getWorlds(getPlayer(playerID));
	}

	private static JSONArray getWorlds(JSONObject json) {
		JSONArray worldsJson = (JSONArray) json.get("worlds");
		if (worldsJson == null)
			json.put("worlds", worldsJson = new JSONArray());
		return worldsJson;
	}

	public static File getGameFile(UUID uuid) {
		return new File(GAME_DIR, uuid.toString() + ".json");
	}

	public static UUID createGame() {
		UUID uuid;
		do
			uuid = UUID.randomUUID();
		while (getGame(uuid) != null);
		if (GAME_CACHE.size() >= CACHE_SIZE)
			saveWorld(uuid, GAME_CACHE.removeLast());
		GAME_CACHE.put(uuid, new JSONObject());
		return uuid;
	}

	public static void deleteGame(UUID uuid) {
		GAME_CACHE.remove(uuid);
		getGameFile(uuid).delete();
	}

	public static JSONObject getGame(UUID uuid) {
		JSONObject json = GAME_CACHE.get(uuid);
		if (json != null)
			return json;
		try {
			json = (JSONObject) FileUtils.readJson(getGameFile(uuid));
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			Logger.err("DataManager: Error on loading game '" + uuid + "' file!", e);
			return null;
		} catch (ParseException e) {
			Logger.err("DataManager: Error on parsing game '" + uuid + "' file!", e);
			return null;
		}
		if (GAME_CACHE.size() >= CACHE_SIZE)
			saveWorld(uuid, GAME_CACHE.removeLast());
		GAME_CACHE.put(uuid, json);
		return json;
	}

	private static void saveGame(UUID uuid, JSONObject json) {
		try {
			FileUtils.writeJson(getGameFile(uuid), json);
		} catch (IOException e) {
			Logger.err("DataManager: Error on writing game '" + uuid + "' file!", e);
		}
	}

	public static JSONArray getGames(long playerID) {
		return getGames(getPlayer(playerID));
	}

	private static JSONArray getGames(JSONObject json) {
		JSONArray gamesJson = (JSONArray) json.get("games");
		if (gamesJson == null)
			json.put("games", gamesJson = new JSONArray());
		return gamesJson;
	}

	private static void initGameTypeMeta() {
		if (!GAME_TYPE_META_FILE.exists()) {
			gameTypeMeta = new JSONObject();
		} else {
			try {
				gameTypeMeta = (JSONObject) FileUtils.readJson(GAME_TYPE_META_FILE);
			} catch (IOException e) {
				Logger.err("DataManager: Error on loading game type meta file!", e);
			} catch (ParseException e) {
				Logger.err("DataManager: Error on parsing game type meta file!", e);
			}
		}
	}

	private static void saveTypeMeta() {
		try {
			FileUtils.writeJson(GAME_TYPE_META_FILE, gameTypeMeta);
		} catch (IOException e) {
			Logger.err("DataManager: Error on writing games type meta file!", e);
		}
	}

	public static File getGamesPerTypeFile(int type, int offset) {
		File typeFile = new File(GAME_MAP_DIR, Integer.toString(type));
		if (!typeFile.exists())
			typeFile.mkdir();
		File mapFile = new File(typeFile, Integer.toString(offset) + ".json");
		if (!mapFile.exists()) {
			try {
				FileUtils.writeJson(mapFile, new JSONArray());
			} catch (IOException e) {
				Logger.err("DataManager: Error on creating game type file '" + type + '_' + offset + "'", e);
			}
		}
		return mapFile;
	}

	public static void addGamePerType(int type, String uuid) {
		String typeKey = Integer.toString(type);
		File gameTypeFile;
		if (gameTypeMeta.containsKey(typeKey)) {
			long typeGameSize = (long) gameTypeMeta.get(typeKey);
			gameTypeFile = getGamesPerTypeFile(type, (int) typeGameSize / 28);
			gameTypeMeta.put(typeKey, typeGameSize + 1);
		} else {
			gameTypeFile = getGamesPerTypeFile(type, 0);
			gameTypeMeta.put(typeKey, 1L);
		}
		JSONArray gameTypeJson = getGamesPerType(gameTypeFile);
		gameTypeJson.add(uuid);
		saveGamesPerType(gameTypeFile, gameTypeJson);
	}

	public static JSONArray getGamesPerType(int type, int offset) {
		return getGamesPerType(getGamesPerTypeFile(type, offset));
	}

	public static JSONArray getGamesPerType(File mapFile) {
		JSONArray json;
		try {
			json = (JSONArray) FileUtils.readJson(mapFile);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			Logger.err("DataManager: Error on loading games type file '" + mapFile.getName() + "'", e);
			return null;
		} catch (ParseException e) {
			Logger.err("DataManager: Error on parsing games type file '" + mapFile.getName() + "'", e);
			return null;
		}
		return json;
	}

	private static void saveGamesPerType(File file, JSONArray json) {
		try {
			FileUtils.writeJson(file, json);
		} catch (IOException e) {
			Logger.err("DataManager: Error on writing game type file!", e);
		}
	}
}
