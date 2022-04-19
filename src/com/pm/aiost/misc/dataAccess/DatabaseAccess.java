package com.pm.aiost.misc.dataAccess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.UUID;

import com.pm.aiost.misc.database.Database;
import com.pm.aiost.misc.database.DatabaseManager;
import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.rank.Rank;

public class DatabaseAccess implements IDataAccess {

	@Override
	public void closeResult(ResultSet resultSet) {
		try {
			Statement statement = resultSet.getStatement();
			Connection con = statement.getConnection();
			resultSet.close();
			statement.close();
			con.close();
		} catch (SQLException e) {
			Logger.err("DatabaseDataAccess: Error on closing ResultSet!", e);
		}
	}

	@Override
	public ResultSet getPlayerCore(UUID uuid, String name) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getPlayerCore(?, ?)");
		statement.setString(1, uuid.toString());
		statement.setString(2, name);
		return statement.executeQuery();
	}

	@Override
	public void setRank(long playerID, Rank rank) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "setRank(?, ?)")) {
			statement.setLong(1, playerID);
			statement.setByte(2, rank.id);
			statement.execute();
		}
	}

	@Override
	public void addCredits(long playerID, int credits) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "addCredits(?, ?)")) {
			statement.setLong(1, playerID);
			statement.setInt(2, credits);
			statement.execute();
		}
	}

	@Override
	public void addUnlockable(long playerID, int typeID, short unlockableID) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "addUnlockable(?, ?, ?)")) {
			statement.setLong(1, playerID);
			statement.setInt(2, typeID);
			statement.setShort(3, unlockableID);
			statement.execute();
		}
	}

	@Override
	public void buyUnlockable(long playerID, int typeID, short unlockableID, int price) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "buyUnlockable(?, ?, ?, ?)")) {
			statement.setLong(1, playerID);
			statement.setInt(2, typeID);
			statement.setShort(3, unlockableID);
			statement.setInt(4, price);
			statement.execute();
		}
	}

	@Override
	public ResultSet getUnlockables(long playerID, int typeID) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getUnlockables(?, ?)");
		statement.setLong(1, playerID);
		statement.setInt(2, typeID);
		return statement.executeQuery();
	}

	@Override
	public ResultSet getUnlockables(long playerID, int typeID, short startID, short size) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getUnlockablesRegion(?, ?, ?, ?)");
		statement.setLong(1, playerID);
		statement.setInt(2, typeID);
		statement.setShort(3, startID);
		statement.setShort(4, size);
		return statement.executeQuery();
	}

	@Override
	public boolean hasUnlockable(long playerID, int typeID, short unlockableID) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "hasUnlockable(?, ?, ?, ?)");
		statement.setLong(1, playerID);
		statement.setInt(2, typeID);
		statement.setShort(3, unlockableID);
		statement.execute();
		return statement.getBoolean(4);
	}

	@Override
	public void addFriend(long requestPlayerID, long playerID) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "addFriend(?, ?)")) {
			statement.setLong(1, requestPlayerID);
			statement.setLong(2, playerID);
			statement.execute();
		}
	}

	@Override
	public void removeFriend(long playerID1, long playerID2) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "removeFriend(?, ?)")) {
			statement.setLong(1, playerID1);
			statement.setLong(2, playerID2);
			statement.execute();
		}
	}

	@Override
	public void removeFriend(long playerID, String playerName) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "removeFriendPerName(?, ?)")) {
			statement.setLong(1, playerID);
			statement.setString(2, playerName);
			statement.execute();
		}
	}

	@Override
	public ResultSet getFriends(long playerID, int index) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getFriends(?, ?)");
		statement.setLong(1, playerID);
		statement.setInt(2, index);
		return statement.executeQuery();
	}

	@Override
	public byte addFriendRequest(long playerID, long requestPlayerID) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "addFriendRequest(?, ?, ?)")) {
			statement.setLong(1, playerID);
			statement.setLong(2, requestPlayerID);
			statement.registerOutParameter(3, Types.TINYINT);
			statement.execute();
			return statement.getByte(3);
		} catch (SQLIntegrityConstraintViolationException e) {
			return 3;
		}
	}

	@Override
	public byte addFriendRequest(String playerName, long requestPlayerID) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "addFriendRequestPerName(?, ?, ?)")) {
			statement.setString(1, playerName);
			statement.setLong(2, requestPlayerID);
			statement.registerOutParameter(3, Types.TINYINT);
			statement.execute();
			return statement.getByte(3);
		} catch (SQLIntegrityConstraintViolationException e) {
			return 3;
		}
	}

	@Override
	public void removeFriendRequest(long playerID, long requestPlayerID) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "removeFriendRequest(?, ?)")) {
			statement.setLong(1, playerID);
			statement.setLong(2, requestPlayerID);
			statement.execute();
		}
	}

	@Override
	public void removeFriendRequest(long playerID, String playerName) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "removeFriendRequestPerName(?, ?)")) {
			statement.setLong(1, playerID);
			statement.setString(2, playerName);
			statement.execute();
		}
	}

	@Override
	public ResultSet getFriendRequests(long playerID, int index) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getFriendRequests(?, ?)");
		statement.setLong(1, playerID);
		statement.setInt(2, index);
		return statement.executeQuery();
	}

	@Override
	public ResultSet getSendFriendRequests(long playerID, int index) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getSendFriendRequests(?, ?)");
		statement.setLong(1, playerID);
		statement.setInt(2, index);
		return statement.executeQuery();
	}

	@Override
	public UUID addPlayerWorld(long playerID, long locationID, String name, byte environment, int type,
			boolean generateStructures) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "addPlayerWorld(?, ?, ?, ?, ?, ?, ?)")) {
			statement.setLong(1, playerID);
			statement.setLong(2, locationID);
			statement.setString(3, name);
			statement.setByte(4, environment);
			statement.setInt(5, type);
			statement.setBoolean(6, generateStructures);
			statement.registerOutParameter(7, Types.BINARY);
			statement.execute();
			return UUID.fromString(statement.getString(7));
		}
	}

	@Override
	public void removePlayerWorld(UUID uuid) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "removePlayerWorld(?)")) {
			statement.setString(1, uuid.toString());
			statement.execute();
		}
	}

	@Override
	public ResultSet getPlayerWorlds(long playerID, int limit, int offset) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getPlayerWorlds(?, ?, ?)");
		statement.setLong(1, playerID);
		statement.setInt(2, limit);
		statement.setInt(3, offset);
		return statement.executeQuery();
	}

	@Override
	public void renamePlayerWorld(UUID uuid, String newName) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "renamePlayerWorld(?, ?)")) {
			statement.setString(1, uuid.toString());
			statement.setString(2, newName);
			statement.execute();
		}
	}

	@Override
	public void updatePlayerWorldLastSaveDate(UUID uuid) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "updatePlayerWorldLastSaveDate(?)")) {
			statement.setString(1, uuid.toString());
			statement.execute();
		}
	}

	@Override
	public UUID addGame(UUID worldID, String name, int type) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "addGame(?, ?, ?, ?)")) {
			statement.setString(1, worldID.toString());
			statement.setString(2, name);
			statement.setInt(3, type);
			statement.registerOutParameter(4, Types.BINARY);
			statement.execute();
			return UUID.fromString(statement.getString(4));
		}
	}

	@Override
	public void updateGame(UUID uuid) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "updateGame(?)")) {
			statement.setString(1, uuid.toString());
			statement.execute();
		}
	}

	@Override
	public void updateGameRate(UUID uuid, int amount, int value) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		try (Connection connection = database.getConnection();
				CallableStatement statement = database.prepareCall(connection, "updateGameRate(?, ?, ?)")) {
			statement.setString(1, uuid.toString());
			statement.setInt(2, amount);
			statement.setInt(3, value);
			statement.execute();
		}
	}

	@Override
	public ResultSet getGames(int type, byte orderBy, int offset) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getGamesPerType(?, ?, ?)");
		statement.setInt(1, type);
		statement.setByte(2, orderBy);
		statement.setInt(3, offset);
		return statement.executeQuery();
	}

	@Override
	public ResultSet getGames(int type, String name, byte orderBy, int offset) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getGamesPerTypeAndName(?, ?, ?, ?)");
		statement.setInt(1, type);
		statement.setString(2, name);
		statement.setByte(3, orderBy);
		statement.setInt(4, offset);
		return statement.executeQuery();
	}

	@Override
	public ResultSet getGamesPerPlayer(long playerID, byte orderBy, int offset) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getGamesPerPlayer(?, ?, ?)");
		statement.setLong(1, playerID);
		statement.setByte(2, orderBy);
		statement.setInt(3, offset);
		return statement.executeQuery();
	}

	@Override
	public ResultSet getGamesPerPlayer(int type, long playerID, byte orderBy, int offset) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getGamesPerTypeAndPlayer(?, ?, ?, ?)");
		statement.setInt(1, type);
		statement.setLong(2, playerID);
		statement.setByte(3, orderBy);
		statement.setInt(4, offset);
		return statement.executeQuery();
	}

	@Override
	public ResultSet getGamesPerPlayer(int type, String playerName, byte orderBy, int offset) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getGamesPerTypeAndPlayerName(?, ?, ?, ?)");
		statement.setInt(1, type);
		statement.setString(2, playerName);
		statement.setByte(3, orderBy);
		statement.setInt(4, offset);
		return statement.executeQuery();
	}

	@Override
	public ResultSet getGames(String name, long playerID, byte orderBy, int offset) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getGamesPerNameAndPlayer(?, ?, ?, ?)");
		statement.setString(1, name);
		statement.setLong(2, playerID);
		statement.setByte(3, orderBy);
		statement.setInt(4, offset);
		return statement.executeQuery();
	}

	@Override
	public ResultSet getGames(int type, String name, long playerID, byte orderBy, int offset) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection,
				"getGamesPerTypeAndNameAndPlayer(?, ?, ?, ?, ?)");
		statement.setInt(1, type);
		statement.setString(2, name);
		statement.setLong(3, playerID);
		statement.setByte(4, orderBy);
		statement.setInt(5, offset);
		return statement.executeQuery();
	}

	@Override
	public ResultSet getGames(int type, String name, String playerName, byte orderBy, int offset) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection,
				"getGamesPerTypeAndNameAndPlayerName(?, ?, ?, ?, ?)");
		statement.setInt(1, type);
		statement.setString(2, name);
		statement.setString(3, playerName);
		statement.setByte(4, orderBy);
		statement.setInt(5, offset);
		return statement.executeQuery();
	}

	@Override
	public ResultSet getGame(UUID uuid) throws SQLException {
		Database database = DatabaseManager.getDatabase();
		Connection connection = database.getConnection();
		CallableStatement statement = database.prepareCall(connection, "getGamePerUUID(?)");
		statement.setString(1, uuid.toString());
		return statement.executeQuery();
	}
}
