package com.pm.aiost.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pm.aiost.collection.list.FastArrayList;

public class ServerType {

	private static final FastArrayList<ServerType> LIST = new FastArrayList<ServerType>();

	private static final Map<String, ServerType> NAME_MAP = new HashMap<String, ServerType>();

	public static final ServerType NONE = new ServerType("None", 0, 0, 0, 0, 0, false, false, false, false);

	public static final ServerType LOBBY = new ServerType("Lobby", 1024, 0, 5000, 100, 0, false, false, false, false);

	public static final ServerType GAME = new ServerType("Game", 1024, 0, 10000, 100, 2, false, false, false, false);

	public static final ServerType SURVIVAL = new ServerType("Survival", 1024, 0, 29999984, 100, 2, true, true, true,
			true);

	public final int index;
	public final String name;
	public final int ram;
	public final int gameMode;
	public final int maxWorldSize;
	public final int maxPlayer;
	public final int difficulty;
	public final boolean announcePlayerAchievement;
	public final boolean spawnAnimals;
	public final boolean spawnNPC;
	public final boolean spawnMonster;
	private Object eventHandler;

	public ServerType(String name, int ram, int gameMode, int maxWorldSize, int maxPlayer, int difficulty,
			boolean announcePlayerAchievement, boolean spawnAnimals, boolean spawnNPC, boolean spawnMonster) {
		this.index = LIST.insert(this);
		NAME_MAP.put(name.toLowerCase(), this);
		this.name = name;
		this.ram = ram;
		this.gameMode = gameMode;
		this.maxWorldSize = maxWorldSize;
		this.maxPlayer = maxPlayer;
		this.difficulty = difficulty;
		this.announcePlayerAchievement = announcePlayerAchievement;
		this.spawnAnimals = spawnAnimals;
		this.spawnNPC = spawnNPC;
		this.spawnMonster = spawnMonster;
	}

	public void setEventHandler(Object eventHandler) {
		if (this.eventHandler == null)
			this.eventHandler = eventHandler;
	}

	public Object getEventHandler() {
		return eventHandler;
	}

	public static ServerType get(int index) {
		return LIST.get(index);
	}

	public static ServerType get(String name) {
		return NAME_MAP.get(name);
	}

	public static ServerType getIgnoreCase(String name) {
		return NAME_MAP.get(name.toLowerCase());
	}

	public static List<ServerType> values() {
		return Collections.unmodifiableList(LIST);
	}

	public static int size() {
		return LIST.size();
	}
}
