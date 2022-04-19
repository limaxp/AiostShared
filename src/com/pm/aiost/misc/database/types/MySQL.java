package com.pm.aiost.misc.database.types;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pm.aiost.misc.database.Database;
import com.pm.aiost.misc.database.DatabaseType;

public class MySQL extends Database {

	public MySQL(String hostname, int port, String username, String password) {
		super(hostname, port, username, password);
	}

	public MySQL(String hostname, int port, String database, String username, String password) {
		super(hostname, port, database, username, password);
	}

	@Override
	protected Connection openConnection() throws SQLException, ClassNotFoundException {
		String connectionURL = "jdbc:mysql://" + hostname + ":" + port;
		if (database != null)
			connectionURL = connectionURL + "/" + database;

		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(connectionURL, user, password);
	}

	@Override
	public CallableStatement prepareCall(Connection connection, String procedure) throws SQLException {
		return super.prepareCall(connection, "CALL " + procedure);
	}

	@Override
	public boolean callExec(String procedure) {
		return super.callExec("CALL " + procedure);
	}

	@Override
	public ArrayList<Object[]> call(String procedure) {
		return super.call("CALL " + procedure);
	}

	@Override
	public ArrayList<Object[]> call(String procedure, List<Object> args) {
		return super.call("CALL " + procedure, args);
	}

	@Override
	public DatabaseType<? extends MySQL> getType() {
		return DatabaseType.MYSQL;
	}
}
