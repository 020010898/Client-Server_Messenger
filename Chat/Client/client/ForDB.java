package client;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class ForDB {

	private static String servername = "localhost";
	private static Integer portnumber = 3306;
	private static String dbname = "users_bd";
	private static String username = "root";
	private static String kennwort = "";

	public static Connection getConnection() {
		Connection cnx = null;

		MysqlDataSource datasource = new MysqlDataSource();

		datasource.setServerName(servername);
		datasource.setUser(username);
		datasource.setPassword(kennwort);
		datasource.setDatabaseName(dbname);
		datasource.setPortNumber(portnumber);

		try {
			cnx = datasource.getConnection();
		} catch (SQLException e) {
			Logger.getLogger(" Get Connection -> " + ForDB.class.getName()).log(Level.SEVERE, null, e);
		}

		return cnx;
	}

}
