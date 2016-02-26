package com.common.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * 
 * @author Robin
 * @version 1.0
 */

public class ConnectionPool {
	Connection connection = null;

	public ConnectionPool() {
	}

	public Connection getConnection_JNDI() throws Exception {
		Context initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:/comp/env");
		DataSource ds = (DataSource) envContext.lookup("jdbc/myoracle");
		Connection conn = ds.getConnection();
		return conn;
	}

	public Connection getConnection_Oracle() throws SQLException {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Connection con = null;
		try {
			File file = new File("oracle.properties");
			BufferedReader data = new BufferedReader(new FileReader(file));
			String url = data.readLine();
			url = url.substring("url".length() + 1);
			String username = data.readLine();
			username = username.substring("username".length() + 1);
			String password = data.readLine();
			password = password.substring("password".length() + 1);
			con = DriverManager.getConnection(url, username, password);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return con;
	}

	public Connection getConnection_Access1() throws SQLException {
		String urlmdb = "";
		try {
			String access = getClass().getResource("ZhangJi.mdb").toString();
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			URL url_mdb = getClass().getResource("ZhangJi.mdb");
			String mdb = url_mdb.getFile();
			mdb = mdb.substring(1);
			urlmdb = "jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ="
					+ mdb + ";readonly=false";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Connection connection = DriverManager.getConnection(urlmdb, "", "");
		return connection;
	}

	public Connection getConnection_Access() throws SQLException {
		String urlmdb = "";
		try {
			// String access = getClass().getResource("ZhangJi.mdb").toString();
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			// URL url_mdb = getClass().getResource("ZhangJi.mdb");
			// String mdb = url_mdb.getFile();
			// String mdb ="E:\\workspace\\MySystemSummary_bak\\ZhangJi.mdb";
			String mdb = "stock.mdb";

			urlmdb = "jdbc:odbc:DRIVER=Microsoft Access Driver (*.mdb);DBQ="
					+ mdb + ";readonly=false";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Connection connection = DriverManager.getConnection(urlmdb, "", "");
		return connection;
	}

	public Connection getConnectionFromMySQLPool() throws SQLException {
		try {
			InitialContext ic = new InitialContext();
			DataSource myDS = (DataSource) ic
					.lookup("java:comp/env/jdbc/carudb");
			Connection result = myDS.getConnection();
			return result;
		} catch (Exception ex) {
			System.out
					.println("Getting single connection through accessing db directly");
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
			String url = "jdbc:mysql://localhost:3306/carucrm?user=root&password=pass&useUnicode=true&characterEncoding=UTF-8";
			Connection connection = DriverManager.getConnection(url);
			System.out
					.println("Got single connection through accessing db directly");
			return connection;
		}
	}

	public static String DB_URL = "localhost";
	public static String DB_FULL_URL = null;

	public Connection getMySQLSingleConnect() {
		Connection result = null;
		try {
			String db_url = "jdbc:mysql://"
					+ DB_URL
					+ ":3306/bgj?user=bgj&password=KLine123&useUnicode=true&characterEncoding=UTF-8";
			
			if(DB_FULL_URL != null){
				db_url = DB_FULL_URL;
			}
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			result = DriverManager.getConnection(db_url);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public Connection getConnection() throws SQLException {
		return getMySQLSingleConnect();
	}

	public static void setDBURL(String db_url) {
		if (db_url != null && !db_url.equals("")) {
			DB_URL = db_url;
		}
	}

	public static void setFULLDBURL(String db_url) {
		if (db_url != null && !db_url.equals("")) {
			DB_FULL_URL = db_url;
		}
	}

}
