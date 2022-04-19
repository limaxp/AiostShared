package com.pm.aiost.misc.database;

import java.util.HashMap;
import java.util.Map;

import com.pm.aiost.misc.database.types.MySQL;
import com.pm.aiost.misc.database.types.SQLServer;

public class DatabaseType<T extends Database> {

	private static Map<String, DatabaseType<? extends Database>> types;

	static {
		types = new HashMap<String, DatabaseType<? extends Database>>();
	}

	public static final DatabaseType<MySQL> MYSQL = a("mysql", MySQL::new);

	public static final DatabaseType<MySQL> MARIA_DB = a("mariaDB", MySQL::new);

	public static final DatabaseType<SQLServer> SQL_SERVER = a("sqlserver", SQLServer::new);

	public final String name;
	public final DatabaseConstructor<T> constructor;

	public DatabaseType(String name, DatabaseConstructor<T> constructor) {
		this.name = name;
		this.constructor = constructor;
		types.put(name.toLowerCase(), this);
	}

	public T create(String hostname, int port, String database, String username, String password) {
		return constructor.get(hostname, port, database, username, password);
	}

	protected static <T extends Database> DatabaseType<T> a(String name, DatabaseConstructor<T> supplier) {
		return new DatabaseType<T>(name, supplier);
	}

	public static <T extends Database> T create(DatabaseType<T> type, String hostname, int port, String database,
			String username, String password) {
		return type.constructor.get(hostname, port, database, username, password);
	}

	public static Database create(String typeName, String hostname, int port, String database, String username,
			String password) {
		return types.get(typeName.toLowerCase()).create(hostname, port, database, username, password);
	}

	public interface DatabaseConstructor<T extends Database> {

		public T get(String hostname, int port, String database, String username, String password);
	}
}
