package com.pm.aiost.misc.dataAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.RowSetProvider;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pm.aiost.misc.other.DataManager;
import com.pm.aiost.misc.rank.Rank;

@SuppressWarnings("unchecked")
public class FileAccess implements IDataAccess {

	@Override
	public void closeResult(ResultSet resultSet) {
	}

	@Override
	public ResultSet getPlayerCore(UUID uuid, String name) throws SQLException {
		long playerID = DataManager.getOrAddPlayerID(name);
		JSONObject playerJson = DataManager.getPlayer(playerID);
		if (playerJson == null) {
			playerJson = DataManager.createPlayer(playerID);
			playerJson.put("UUID", uuid.toString());
			playerJson.put("player_Name", name);
			playerJson.put("rank_ID", 0L);
			playerJson.put("score", 0L);
			playerJson.put("credits", 5000L);
			playerJson.put("level", 1L);
			playerJson.put("experience", 0L);
			playerJson.put("health", 20.0);
			playerJson.put("max_Health", 20.0);
			playerJson.put("mana", 20.0);
			playerJson.put("max_Mana", 20.0);
			playerJson.put("hunger", 20L);
			playerJson.put("thirst", 20L);
			playerJson.put("mc_Level", 0L);
			playerJson.put("mc_Experience", 0.0);
		} else {
			String oldName = (String) playerJson.get("player_Name");
			if (!name.equals(oldName)) {
				playerJson.put("player_Name", name);
				DataManager.getPlayerNameMap(oldName).remove(oldName);
				DataManager.getPlayerNameMap(name).put(name, playerID);
			}
		}

		RowSetMetaData metaData = new RowSetMetaDataImpl();
		metaData.setColumnCount(2);
		metaData.setColumnName(1, "player_ID");
		metaData.setColumnType(1, Types.BIGINT);
		metaData.setColumnName(2, "rank_ID");
		metaData.setColumnType(2, Types.INTEGER);

		CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
		result.setMetaData(metaData);
		result.moveToInsertRow();

		result.updateLong(1, playerID);
		result.updateInt(2, (int) (long) playerJson.get("rank_ID"));

		result.insertRow();
		result.moveToCurrentRow();
		return result;
	}

	@Override
	public void setRank(long playerID, Rank rank) throws SQLException {
		JSONObject playerJson = DataManager.getPlayer(playerID);
		playerJson.put("rank_ID", (long) rank.id);
	}

	@Override
	public void addCredits(long playerID, int credits) throws SQLException {
		JSONObject playerJson = DataManager.getPlayer(playerID);
		playerJson.put("credits", (long) playerJson.get("credits") + credits);
	}

	@Override
	public void addUnlockable(long playerID, int typeID, short unlockableID) throws SQLException {
		addUnlockable(DataManager.getUnlockables(playerID, typeID), unlockableID);
	}

	@Override
	public void buyUnlockable(long playerID, int typeID, short unlockableID, int price) throws SQLException {
		JSONObject playerJson = DataManager.getPlayer(playerID);
		int credits = (int) (long) playerJson.get("credits");
		if (credits >= price) {
			playerJson.put("credits", (long) credits - price);
			addUnlockable(DataManager.getUnlockables(playerJson, typeID), unlockableID);
		}
	}

	protected void addUnlockable(JSONArray unlockablesJson, short unlockableID) {
		for (int i = 0; i < unlockablesJson.size(); i++) {
			if (unlockableID > (short) (long) unlockablesJson.get(i)) {
				unlockablesJson.add(i, (long) unlockableID);
				return;
			}
		}
		unlockablesJson.add((long) unlockableID);
	}

	@Override
	public ResultSet getUnlockables(long playerID, int typeID) throws SQLException {
		JSONArray unlockablesJson = DataManager.getUnlockables(playerID, typeID);
		RowSetMetaData metaData = new RowSetMetaDataImpl();
		metaData.setColumnCount(1);
		metaData.setColumnName(1, "unlockable_ID");
		metaData.setColumnType(1, Types.SMALLINT);

		CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
		result.setMetaData(metaData);
		for (int i = 0; i < unlockablesJson.size(); i++) {
			result.moveToInsertRow();
			result.updateShort(1, (short) (long) unlockablesJson.get(i));
			result.insertRow();
			result.moveToCurrentRow();
		}
		return result;
	}

	@Override
	public ResultSet getUnlockables(long playerID, int typeID, short startID, short size) throws SQLException {
		JSONArray unlockablesJson = DataManager.getUnlockables(playerID, typeID);
		RowSetMetaData metaData = new RowSetMetaDataImpl();
		metaData.setColumnCount(1);
		metaData.setColumnName(1, "unlockable_ID");
		metaData.setColumnType(1, Types.SMALLINT);

		CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
		result.setMetaData(metaData);
		int maxSize = startID + size;
		if (maxSize > unlockablesJson.size())
			maxSize = unlockablesJson.size();
		for (int i = startID; i < maxSize; i++) {
			result.moveToInsertRow();
			result.updateShort(1, (short) (long) unlockablesJson.get(i));
			result.insertRow();
			result.moveToCurrentRow();
		}
		return result;
	}

	@Override
	public boolean hasUnlockable(long playerID, int typeID, short unlockableID) throws SQLException {
		return DataManager.getUnlockables(playerID, typeID).contains((long) unlockableID);
	}

	@Override
	public void addFriend(long requestPlayerID, long playerID) throws SQLException {
		((JSONArray) DataManager.getFriends(playerID)).add(requestPlayerID);
		((JSONArray) DataManager.getFriends(requestPlayerID)).add(playerID);

		removeFriendRequest(requestPlayerID, playerID);
	}

	@Override
	public void removeFriend(long playerID1, long playerID2) throws SQLException {
		((JSONArray) DataManager.getFriends(playerID1)).remove(playerID2);
		((JSONArray) DataManager.getFriends(playerID2)).remove(playerID1);
	}

	@Override
	public void removeFriend(long playerID, String playerName) throws SQLException {
		removeFriend(playerID, DataManager.getPlayerID(playerName));
	}

	@Override
	public ResultSet getFriends(long playerID, int index) throws SQLException {
		JSONArray friendsJson = ((JSONArray) DataManager.getFriends(playerID));
		RowSetMetaData metaData = new RowSetMetaDataImpl();
		metaData.setColumnCount(5);
		metaData.setColumnName(1, "friend_ID");
		metaData.setColumnType(1, Types.BIGINT);
		metaData.setColumnName(2, "player_Name");
		metaData.setColumnType(2, Types.VARCHAR);
		metaData.setColumnName(3, "rank_ID");
		metaData.setColumnType(3, Types.SMALLINT);
		metaData.setColumnName(4, "level");
		metaData.setColumnType(4, Types.INTEGER);
		metaData.setColumnName(5, "score");
		metaData.setColumnType(5, Types.INTEGER);

		CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
		result.setMetaData(metaData);
		for (int i = 0; i < friendsJson.size(); i++) {
			long friendId = (long) friendsJson.get(i);
			JSONObject friendJson = DataManager.getPlayer(friendId);
			result.moveToInsertRow();
			result.updateLong(1, friendId);
			result.updateString(2, (String) friendJson.get("player_Name"));
			result.updateShort(3, (short) (long) friendJson.get("rank_ID"));
			result.updateInt(4, (int) (long) friendJson.get("level"));
			result.updateInt(5, (int) (long) friendJson.get("score"));
			result.insertRow();
			result.moveToCurrentRow();
		}
		return result;
	}

	@Override
	public byte addFriendRequest(long playerID, long requestPlayerID) throws SQLException {
		JSONObject playerJson = DataManager.getPlayer(playerID);
		JSONArray friendRequests = (JSONArray) DataManager.getFriendRequests(playerJson);
		if (friendRequests.contains(requestPlayerID))
			return 3;

		JSONArray friendsJson = ((JSONArray) DataManager.getFriends(playerJson));
		if (friendsJson.contains(requestPlayerID))
			return 4;

		JSONArray sentFriendRequests = (JSONArray) DataManager.getSentFriendRequests(playerJson);
		if (sentFriendRequests.contains(requestPlayerID)) {
			addFriend(requestPlayerID, playerID);
			return 1;
		}

		JSONObject requestPlayerJson = DataManager.getPlayer(requestPlayerID);
		String date = LocalDateTime.now().toString();
		friendRequests.add(requestPlayerID);
		((JSONArray) DataManager.getFriendRequestDates(playerJson)).add(date);
		((JSONArray) DataManager.getSentFriendRequests(requestPlayerJson)).add(playerID);
		((JSONArray) DataManager.getSentFriendRequestDates(requestPlayerJson)).add(date);
		return 0;
	}

	@Override
	public byte addFriendRequest(String playerName, long requestPlayerID) throws SQLException {
		long playerID = DataManager.getPlayerID(playerName);
		if (playerID == -1)
			return 2;
		return addFriendRequest(playerID, requestPlayerID);
	}

	@Override
	public void removeFriendRequest(long playerID, long requestPlayerID) throws SQLException {
		JSONObject playerJson = DataManager.getPlayer(playerID);
		JSONArray friendRequestsJson = (JSONArray) DataManager.getFriendRequests(playerJson);
		int index = friendRequestsJson.indexOf(requestPlayerID);
		friendRequestsJson.remove(index);
		((JSONArray) DataManager.getFriendRequestDates(playerJson)).remove(index);

		JSONObject requestPlayerJson = DataManager.getPlayer(requestPlayerID);
		JSONArray sentFriendRequestsJson = (JSONArray) DataManager.getSentFriendRequests(requestPlayerJson);
		index = sentFriendRequestsJson.indexOf(playerID);
		sentFriendRequestsJson.remove(index);
		((JSONArray) DataManager.getSentFriendRequestDates(requestPlayerJson)).remove(index);
	}

	@Override
	public void removeFriendRequest(long playerID, String playerName) throws SQLException {
		removeFriendRequest(playerID, DataManager.getPlayerID(playerName));
	}

	@Override
	public ResultSet getFriendRequests(long playerID, int index) throws SQLException {
		JSONArray friendRequestsJson = (JSONArray) DataManager.getFriendRequests(playerID);
		JSONArray friendRequestDatesJson = (JSONArray) DataManager.getFriendRequestDates(playerID);
		RowSetMetaData metaData = new RowSetMetaDataImpl();
		metaData.setColumnCount(6);
		metaData.setColumnName(1, "request_date");
		metaData.setColumnType(1, Types.TIMESTAMP);
		metaData.setColumnName(2, "request_player_ID");
		metaData.setColumnType(2, Types.VARCHAR);
		metaData.setColumnName(3, "player_Name");
		metaData.setColumnType(3, Types.SMALLINT);
		metaData.setColumnName(4, "rank_ID");
		metaData.setColumnType(4, Types.INTEGER);
		metaData.setColumnName(5, "level");
		metaData.setColumnType(5, Types.INTEGER);
		metaData.setColumnName(6, "score");
		metaData.setColumnType(6, Types.INTEGER);

		CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
		result.setMetaData(metaData);
		for (int i = index; i < friendRequestsJson.size(); i++) {
			long requestPlayerID = (long) friendRequestsJson.get(i);
			JSONObject playerJson = DataManager.getPlayer(requestPlayerID);
			result.moveToInsertRow();
			result.updateTimestamp(1, Timestamp.valueOf(LocalDateTime.parse((String) friendRequestDatesJson.get(i))));
			result.updateLong(2, requestPlayerID);
			result.updateString(3, (String) playerJson.get("player_Name"));
			result.updateByte(4, (byte) (long) playerJson.get("rank_ID"));
			result.updateInt(5, (int) (long) playerJson.get("level"));
			result.updateInt(6, (int) (long) playerJson.get("score"));
			result.insertRow();
			result.moveToCurrentRow();
		}
		return result;
	}

	@Override
	public ResultSet getSendFriendRequests(long playerID, int index) throws SQLException {
		JSONArray friendRequestsJson = (JSONArray) DataManager.getSentFriendRequests(playerID);
		JSONArray friendRequestDatesJson = (JSONArray) DataManager.getSentFriendRequestDates(playerID);
		RowSetMetaData metaData = new RowSetMetaDataImpl();
		metaData.setColumnCount(6);
		metaData.setColumnName(1, "request_date");
		metaData.setColumnType(1, Types.TIMESTAMP);
		metaData.setColumnName(2, "player_ID");
		metaData.setColumnType(2, Types.VARCHAR);
		metaData.setColumnName(3, "player_Name");
		metaData.setColumnType(3, Types.SMALLINT);
		metaData.setColumnName(4, "rank_ID");
		metaData.setColumnType(4, Types.INTEGER);
		metaData.setColumnName(5, "level");
		metaData.setColumnType(5, Types.INTEGER);
		metaData.setColumnName(6, "score");
		metaData.setColumnType(6, Types.INTEGER);

		CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
		result.setMetaData(metaData);
		for (int i = index; i < friendRequestsJson.size(); i++) {
			long requestPlayerID = (long) friendRequestsJson.get(i);
			JSONObject playerJson = DataManager.getPlayer(requestPlayerID);
			result.moveToInsertRow();
			result.updateTimestamp(1, Timestamp.valueOf(LocalDateTime.parse((String) friendRequestDatesJson.get(i))));
			result.updateLong(2, requestPlayerID);
			result.updateString(3, (String) playerJson.get("player_Name"));
			result.updateByte(4, (byte) (long) playerJson.get("rank_ID"));
			result.updateInt(5, (int) (long) playerJson.get("level"));
			result.updateInt(6, (int) (long) playerJson.get("score"));
			result.insertRow();
			result.moveToCurrentRow();
		}
		return result;
	}

	@Override
	public UUID addPlayerWorld(long playerID, long locationID, String name, byte environment, int type,
			boolean generateStructures) throws SQLException {
		UUID uuid = DataManager.createWorld();
		JSONObject worldJson = (JSONObject) DataManager.getWorld(uuid);
		worldJson.put("location_ID", (long) locationID);
		worldJson.put("name", name);
		worldJson.put("owner_ID", playerID);
		worldJson.put("environment", (long) environment);
		worldJson.put("type", (long) type);
		worldJson.put("generateStructures", generateStructures);
		worldJson.put("last_save_date", LocalDateTime.now().toString());

		((JSONArray) DataManager.getWorlds(playerID)).add(uuid.toString());
		return uuid;
	}

	@Override
	public void removePlayerWorld(UUID uuid) throws SQLException {
		JSONObject worldJson = (JSONObject) DataManager.getWorld(uuid);
		DataManager.getWorlds((long) worldJson.get("owner_ID")).remove(uuid.toString());
		DataManager.deleteWorld(uuid);
	}

	@Override
	public ResultSet getPlayerWorlds(long playerID, int limit, int offset) throws SQLException {
		JSONArray worldsJson = (JSONArray) DataManager.getWorlds(playerID);
		RowSetMetaData metaData = new RowSetMetaDataImpl();
		metaData.setColumnCount(7);
		metaData.setColumnName(1, "uuid");
		metaData.setColumnType(1, Types.VARCHAR);
		metaData.setColumnName(2, "location_ID");
		metaData.setColumnType(2, Types.INTEGER);
		metaData.setColumnName(3, "name");
		metaData.setColumnType(3, Types.VARCHAR);
		metaData.setColumnName(4, "environment");
		metaData.setColumnType(4, Types.TINYINT);
		metaData.setColumnName(5, "type");
		metaData.setColumnType(5, Types.INTEGER);
		metaData.setColumnName(6, "generateStructures");
		metaData.setColumnType(6, Types.BOOLEAN);
		metaData.setColumnName(7, "last_save_date");
		metaData.setColumnType(7, Types.TIMESTAMP);

		CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
		result.setMetaData(metaData);
		int lastIndex = offset + limit;
		for (int i = offset; i < worldsJson.size() && i < lastIndex; i++) {
			String uuidString = (String) worldsJson.get(i);
			UUID uuid = UUID.fromString(uuidString);
			JSONObject worldJson = DataManager.getWorld(uuid);
			result.moveToInsertRow();
			result.updateString(1, uuidString);
			result.updateInt(2, (int) (long) worldJson.get("location_ID"));
			result.updateString(3, (String) worldJson.get("name"));
			result.updateByte(4, (byte) (long) worldJson.get("environment"));
			result.updateInt(5, (int) (long) worldJson.get("type"));
			result.updateBoolean(6, (boolean) worldJson.get("generateStructures"));
			result.updateTimestamp(7, Timestamp.valueOf(LocalDateTime.parse((String) worldJson.get("last_save_date"))));
			result.insertRow();
			result.moveToCurrentRow();
		}
		return result;
	}

	@Override
	public void renamePlayerWorld(UUID uuid, String newName) throws SQLException {
		JSONObject worldJson = (JSONObject) DataManager.getWorld(uuid);
		worldJson.put("name", newName);
	}

	@Override
	public void updatePlayerWorldLastSaveDate(UUID uuid) throws SQLException {
		JSONObject worldJson = (JSONObject) DataManager.getWorld(uuid);
		worldJson.put("last_save_date", LocalDateTime.now().toString());
	}

	@Override
	public UUID addGame(UUID worldID, String name, int type) throws SQLException {
		UUID uuid = DataManager.createGame();
		String uuidString = uuid.toString();
		JSONObject gameJson = (JSONObject) DataManager.getGame(uuid);
		gameJson.put("world_uuid", worldID.toString());
		gameJson.put("name", name);
		gameJson.put("type", (long) type);
		gameJson.put("rate_Amount", 0L);
		gameJson.put("rate_Value", 0L);
		gameJson.put("rate", 0.0);
		String date = LocalDateTime.now().toString();
		gameJson.put("release_date", date);
		gameJson.put("update_date", date);

		JSONObject worldJson = (JSONObject) DataManager.getWorld(worldID);
		long playerID = (long) worldJson.get("owner_ID");

		((JSONArray) DataManager.getGames(playerID)).add(uuidString);
		DataManager.addGamePerType(type, uuidString);
		return uuid;
	}

	@Override
	public void updateGame(UUID uuid) throws SQLException {
		JSONObject gameJson = (JSONObject) DataManager.getGame(uuid);
		gameJson.put("update_date", LocalDateTime.now().toString());
	}

	@Override
	public void updateGameRate(UUID uuid, int amount, int value) throws SQLException {
		JSONObject gameJson = (JSONObject) DataManager.getGame(uuid);
		long rateAmount, rateValue;
		gameJson.put("rate_Amount", rateAmount = (long) gameJson.get("rate_Amount") + amount);
		gameJson.put("rate_Value", rateValue = (long) gameJson.get("rate_Value") + value);
		gameJson.put("rate", (double) rateValue / rateAmount);
	}

	@Override
	public ResultSet getGames(int type, byte orderBy, int offset) throws SQLException {
		JSONArray gamesJson = (JSONArray) DataManager.getGamesPerType(type, offset);
		CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
		result.setMetaData(getGameMeta());
		for (int i = 0; i < gamesJson.size() && i < 28; i++)
			getGame(UUID.fromString((String) gamesJson.get(i)), result);
		return result;
	}

	@Override
	public ResultSet getGames(int type, String name, byte orderBy, int offset) throws SQLException {
		return getGames(type, orderBy, offset);
	}

	@Override
	public ResultSet getGamesPerPlayer(long playerID, byte orderBy, int offset) throws SQLException {
		JSONArray gamesJson = (JSONArray) DataManager.getGames(playerID);
		CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
		result.setMetaData(getGameMeta());
		int i = offset * 28;
		int lastIndex = i + 28;
		for (; i < gamesJson.size() && i < lastIndex; i++)
			getGame(UUID.fromString((String) gamesJson.get(i)), result);
		return result;
	}

	@Override
	public ResultSet getGamesPerPlayer(int type, long playerID, byte orderBy, int offset) throws SQLException {
		return getGamesPerPlayer(playerID, orderBy, offset);
	}

	@Override
	public ResultSet getGamesPerPlayer(int type, String playerName, byte orderBy, int offset) throws SQLException {
		return getGamesPerPlayer(DataManager.getPlayerID(playerName), orderBy, offset);
	}

	@Override
	public ResultSet getGames(String name, long playerID, byte orderBy, int offset) throws SQLException {
		return getGamesPerPlayer(playerID, orderBy, offset);
	}

	@Override
	public ResultSet getGames(int type, String name, long playerID, byte orderBy, int offset) throws SQLException {
		return getGames(type, orderBy, offset);
	}

	@Override
	public ResultSet getGames(int type, String name, String playerName, byte orderBy, int offset) throws SQLException {
		return getGames(type, orderBy, offset);
	}

	@Override
	public ResultSet getGame(UUID uuid) throws SQLException {
		CachedRowSet result = RowSetProvider.newFactory().createCachedRowSet();
		result.setMetaData(getGameMeta());
		return getGame(uuid, result);
	}

	protected ResultSet getGame(UUID uuid, ResultSet result) throws SQLException {
		JSONObject gameJson = DataManager.getGame(uuid);
		UUID worldUUID = UUID.fromString((String) gameJson.get("world_uuid"));
		JSONObject worldJson = DataManager.getWorld(worldUUID);
		long ownerID = (long) worldJson.get("owner_ID");
		JSONObject playerJson = DataManager.getPlayer(ownerID);

		result.moveToInsertRow();
		result.updateString(1, uuid.toString());
		result.updateString(2, (String) gameJson.get("name"));
		result.updateInt(3, (int) (long) gameJson.get("type"));
		result.updateLong(4, ownerID);
		result.updateString(5, (String) playerJson.get("player_Name"));
		result.updateByte(6, (byte) (long) worldJson.get("environment"));
		result.updateInt(7, (int) (long) worldJson.get("type"));
		result.updateBoolean(8, (boolean) worldJson.get("generateStructures"));
		result.updateInt(9, (int) (long) gameJson.get("rate_Amount"));
		result.updateDouble(10, (double) gameJson.get("rate"));
		result.updateTimestamp(11, Timestamp.valueOf(LocalDateTime.parse((String) gameJson.get("release_date"))));
		result.updateTimestamp(12, Timestamp.valueOf(LocalDateTime.parse((String) gameJson.get("update_date"))));
		result.insertRow();
		result.moveToCurrentRow();
		return result;
	}

	protected RowSetMetaData getGameMeta() throws SQLException {
		RowSetMetaData metaData = new RowSetMetaDataImpl();
		metaData.setColumnCount(12);
		metaData.setColumnName(1, "uuid");
		metaData.setColumnType(1, Types.VARCHAR);
		metaData.setColumnName(2, "name");
		metaData.setColumnType(2, Types.VARCHAR);
		metaData.setColumnName(3, "type");
		metaData.setColumnType(3, Types.INTEGER);
		metaData.setColumnName(4, "owner_ID");
		metaData.setColumnType(4, Types.BIGINT);
		metaData.setColumnName(5, "player_Name");
		metaData.setColumnType(5, Types.VARCHAR);
		metaData.setColumnName(6, "environment");
		metaData.setColumnType(6, Types.TINYINT);
		metaData.setColumnName(7, "pw.type");
		metaData.setColumnType(7, Types.INTEGER);
		metaData.setColumnName(8, "generateStructures");
		metaData.setColumnType(8, Types.BOOLEAN);
		metaData.setColumnName(9, "rate_Amount");
		metaData.setColumnType(9, Types.INTEGER);
		metaData.setColumnName(10, "rate");
		metaData.setColumnType(10, Types.DOUBLE);
		metaData.setColumnName(11, "release_date");
		metaData.setColumnType(11, Types.TIMESTAMP);
		metaData.setColumnName(12, "update_date");
		metaData.setColumnType(12, Types.TIMESTAMP);
		return metaData;

	}
}
