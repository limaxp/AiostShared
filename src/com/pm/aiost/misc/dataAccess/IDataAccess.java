package com.pm.aiost.misc.dataAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.pm.aiost.misc.rank.Rank;

public interface IDataAccess {

	public void closeResult(ResultSet resultSet);

	public ResultSet getPlayerCore(UUID uuid, String name) throws SQLException;

	public void setRank(long playerID, Rank rank) throws SQLException;

	public void addCredits(long playerID, int credits) throws SQLException;

	public void addUnlockable(long playerID, int typeID, short unlockableID) throws SQLException;

	public void buyUnlockable(long playerID, int typeID, short unlockableID, int price) throws SQLException;
	
	public ResultSet getUnlockables(long playerID, int typeID) throws SQLException;

	public ResultSet getUnlockables(long playerID, int typeID, short startID, short size) throws SQLException;

	public boolean hasUnlockable(long playerID, int typeID, short unlockableID) throws SQLException;

	public void addFriend(long requestPlayerID, long playerID) throws SQLException;

	public void removeFriend(long playerID1, long playerID2) throws SQLException;

	public void removeFriend(long playerID, String playerName) throws SQLException;

	public ResultSet getFriends(long playerID, int index) throws SQLException;

	public byte addFriendRequest(long playerID, long requestPlayerID) throws SQLException;

	public byte addFriendRequest(String playerName, long requestPlayerID) throws SQLException;

	public void removeFriendRequest(long playerID, long requestPlayerID) throws SQLException;

	public void removeFriendRequest(long playerID, String playerName) throws SQLException;

	public ResultSet getFriendRequests(long playerID, int index) throws SQLException;

	public ResultSet getSendFriendRequests(long playerID, int index) throws SQLException;

	public UUID addPlayerWorld(long playerID, long locationID, String name, byte environment, int type,
			boolean generateStructures) throws SQLException;

	public void removePlayerWorld(UUID uuid) throws SQLException;

	public ResultSet getPlayerWorlds(long playerID, int limit, int offset) throws SQLException;

	public void renamePlayerWorld(UUID uuid, String newName) throws SQLException;

	public void updatePlayerWorldLastSaveDate(UUID uuid) throws SQLException;

	public UUID addGame(UUID worldID, String name, int type) throws SQLException;

	public void updateGame(UUID uuid) throws SQLException;

	public void updateGameRate(UUID uuid, int amount, int value) throws SQLException;

	public ResultSet getGames(int type, byte orderBy, int offset) throws SQLException;

	public ResultSet getGames(int type, String name, byte orderBy, int offset) throws SQLException;

	public ResultSet getGamesPerPlayer(long playerID, byte orderBy, int offset) throws SQLException;

	public ResultSet getGamesPerPlayer(int type, long playerID, byte orderBy, int offset) throws SQLException;

	public ResultSet getGamesPerPlayer(int type, String playerName, byte orderBy, int offset) throws SQLException;

	public ResultSet getGames(String name, long playerID, byte orderBy, int offset) throws SQLException;

	public ResultSet getGames(int type, String name, long playerID, byte orderBy, int offset) throws SQLException;

	public ResultSet getGames(int type, String name, String playerName, byte orderBy, int offset) throws SQLException;

	public ResultSet getGame(UUID uuid) throws SQLException;
}
