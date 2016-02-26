package com.common.vogenerator;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.common.db.ConnectionPool;

public class DBAgent_Parse {
	private static ConnectionPool connectionPool = new ConnectionPool();
	private Connection connection = null;

	/**
	 * gain connection from connectionpool
	 */
	public Connection getConnection() throws Exception {
		return this.connectionPool.getConnection();
	}

	/**
	 * commit the Transaction with connection
	 */

	public void commitTransaction() throws Exception {
		if (!this.isInTransaction) {
			throw new Exception("Transaction is not begin");
		}
		this.connection.commit();
		this.isInTransaction = false;
		this.connection.setAutoCommit(true);
		this.connection.close();
	}

	/**
	 * rollback the Transaction with connection
	 */
	public void rollbackTransaction() throws Exception {
		if (!this.isInTransaction) {
			throw new Exception("Transaction is not begin");
		}
		this.connection.rollback();
		this.isInTransaction = false;
		this.connection.setAutoCommit(true);
		this.connection.close();
	}

	/**
	 * overload the finalize of object to close the connection to db
	 * 
	 * @throws Throwable
	 */
	public void finalize() throws Throwable {
		if (this.connection != null) {
			this.connection = null;
		}
	}

	/**
	 * insert Object to corresponding table
	 * 
	 * @param o
	 * @throws SQLException
	 * @throws Exception
	 */
	public void insert(Object o) throws SQLException, Exception {
		Class c = o.getClass();
		Method[] m = c.getMethods();
		// Generate sql
		Vector data = new Vector();
		String insertColumn = "";
		String insertValue = "";
		for (int i = 0; i < m.length; i++) {
			String methodName = m[i].getName();
			if (methodName.indexOf("get") == 0
					&& !methodName.equals("getClass")) {
				String isHasMethod = methodName.replaceFirst("get", "isHas");
				Boolean b = (Boolean) c.getMethod(isHasMethod, null).invoke(o,
						null);
				if (b.booleanValue()) {
					Object value = m[i].invoke(o, null);
					data.addElement(value);
					String columnName = methodName.replaceFirst("get", "");
					insertColumn += (columnName + ",");
					insertValue += "?,";
				}
			}
		}
		String sql = null;
		if (insertColumn.length() > 0) {
			insertColumn = insertColumn.substring(0, insertColumn.length() - 1);
			insertValue = insertValue.substring(0, insertValue.length() - 1);
			String tableName = c.getField("TABLE_NAME").get(o).toString();
			sql = "insert into " + tableName + "(" + insertColumn + ")values("
					+ insertValue + ")";
		}
		// insert data to DB
		Connection con = null;
		PreparedStatement ps = null;
		try {
			if (sql != null) {
				if (this.isInTransaction) {
					con = this.connection;
				} else {
					con = this.getConnection();
				}
				System.out.println("exec sql = " + sql);
				ps = con.prepareStatement(sql);

				for (int i = 0; i < data.size(); i++) {
					Object value = data.elementAt(i);
					if (value instanceof java.util.Date) {
						value = new java.sql.Timestamp(
								((java.util.Date) value).getTime());
					}
					ps.setObject(i + 1, value);
				}
				ps.executeUpdate();
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (!this.isInTransaction && con != null) {
				con.close();
			}
		}
	}

	/**
	 * delete Object of corresponding table
	 * 
	 * @param o
	 * @throws SQLException
	 * @throws Exception
	 */

	public void delete(Object o) throws SQLException, Exception {
		Class c = o.getClass();
		Method[] m = c.getMethods();
		// Generate sql
		Vector whereData = new Vector();
		String whereColumn = "";
		for (int i = 0; i < m.length; i++) {
			String methodName = m[i].getName();
			if (methodName.indexOf("get") == 0
					&& !methodName.equals("getClass")) {
				String isHasMethod = methodName.replaceFirst("get", "isHas");
				Boolean b = (Boolean) c.getMethod(isHasMethod, null).invoke(o,
						null);
				if (b.booleanValue()) {
					Object value = m[i].invoke(o, null);
					String columnName = methodName.replaceFirst("get", "");
					whereData.addElement(value);
					whereColumn += (" " + columnName + "=? and");
				}
			}
		}
		String sql = null;
		if (whereColumn.length() > 0) {
			whereColumn = whereColumn.substring(0, whereColumn.length() - 3);
			String tableName = c.getField("TABLE_NAME").get(o).toString();
			sql = "delete from " + tableName + " where " + whereColumn;
		}
		// delete data from DB
		Connection con = null;
		PreparedStatement ps = null;
		try {
			if (sql != null) {
				if (this.isInTransaction) {
					con = this.connection;
				} else {
					con = this.getConnection();
				}
				System.out.println("exec sql = " + sql);
				ps = con.prepareStatement(sql);
				for (int i = 0; i < whereData.size(); i++) {
					Object value = whereData.elementAt(i);
					if (value instanceof java.util.Date) {
						value = new java.sql.Timestamp(
								((java.util.Date) value).getTime());
					}
					ps.setObject(i + 1, value);

				}
				ps.executeUpdate();
			}
		}

		finally {
			if (ps != null) {
				ps.close();
			}
			if (!this.isInTransaction && con != null) {
				con.close();
			}
		}

	}

	/**
	 * update Object of corresponding table
	 * 
	 * @param o
	 * @throws SQLException
	 * @throws Exception
	 */

	public void update(Object o) throws SQLException, Exception {
		Class c = o.getClass();
		Method[] m = c.getMethods();
		// Generate sql
		Vector updateData = new Vector();
		Vector whereData = new Vector();
		String updateColumn = "";
		String whereColumn = "";
		for (int i = 0; i < m.length; i++) {
			String methodName = m[i].getName();
			if (methodName.indexOf("get") == 0
					&& !methodName.equals("getClass")) {
				String isHasMethod = methodName.replaceFirst("get", "isHas");
				Boolean b = (Boolean) c.getMethod(isHasMethod, null).invoke(o,
						null);
				if (b.booleanValue()) {
					Object value = m[i].invoke(o, null);
					String columnName = methodName.replaceFirst("get", "");
					String isWhere = methodName.replaceFirst("get", "isWhere");
					Boolean isWhereFlag = (Boolean) c.getMethod(isWhere, null)
							.invoke(o, null);

					if (!isWhereFlag.booleanValue()) {
						updateData.addElement(value);
						updateColumn += (columnName + "=?,");
					} else {
						whereData.addElement(value);
						whereColumn += (" " + columnName + "=? and");
					}
				}
			}
		}
		String sql = null;
		if (updateColumn.length() > 0) {
			updateColumn = updateColumn.substring(0, updateColumn.length() - 1);
			if (whereColumn.length() > 0) {
				whereColumn = whereColumn
						.substring(0, whereColumn.length() - 3);
			}
			String tableName = c.getField("TABLE_NAME").get(o).toString();
			sql = "update " + tableName + " set " + updateColumn;
			if (whereColumn.length() > 0) {
				sql += (" where " + whereColumn);
			}
		}
		// update data from DB
		Connection con = null;
		PreparedStatement ps = null;
		try {
			if (sql != null) {
				if (this.isInTransaction) {
					con = this.connection;
				} else {
					con = this.getConnection();
				}
				ps = con.prepareStatement(sql);
				System.out.println("exec sql = " + sql);
				for (int i = 0; i < updateData.size(); i++) {
					Object value = updateData.elementAt(i);
					if (value instanceof java.util.Date) {
						value = new java.sql.Timestamp(
								((java.util.Date) value).getTime());
					}
					ps.setObject(i + 1, value);
				}

				int pos = updateData.size();
				for (int i = 0; i < whereData.size(); i++) {
					Object value = whereData.elementAt(i);
					if (value instanceof java.util.Date) {
						value = new java.sql.Timestamp(
								((java.util.Date) value).getTime());
					}
					ps.setObject(pos + i + 1, value);

				}
				ps.executeUpdate();
			}
		}

		finally {
			if (ps != null) {
				ps.close();
			}
			if (!this.isInTransaction && con != null) {
				con.close();
			}
		}

	}

	public Object select(Object o) throws SQLException, Exception {
		Class c = o.getClass();
		Method[] m = c.getMethods();
		// Generate sql
		Vector whereData = new Vector();
		String whereColumn = "";
		for (int i = 0; i < m.length; i++) {
			String methodName = m[i].getName();
			if (methodName.indexOf("get") == 0
					&& !methodName.equals("getClass")) {
				String isHasMethod = methodName.replaceFirst("get", "isHas");
				Boolean b = (Boolean) c.getMethod(isHasMethod, null).invoke(o,
						null);
				if (b.booleanValue()) {
					Object value = m[i].invoke(o, null);
					String columnName = methodName.replaceFirst("get", "");
					whereData.addElement(value);
					whereColumn += (" " + columnName + "=? and");
				}
			}
		}
		String sql = null;
		if (whereColumn.length() > 0) {
			whereColumn = whereColumn.substring(0, whereColumn.length() - 3);
			String tableName = c.getField("TABLE_NAME").get(o).toString();
			sql = "select * from " + tableName + " where " + whereColumn;
		}
		// update data from DB
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if (sql != null) {
				if (this.isInTransaction) {
					con = this.connection;
				} else {
					con = this.getConnection();
				}
				System.out.println("exec sql = " + sql);
				ps = con.prepareStatement(sql);
				for (int i = 0; i < whereData.size(); i++) {
					Object value = whereData.elementAt(i);
					if (value instanceof java.util.Date) {
						value = new java.sql.Timestamp(
								((java.util.Date) value).getTime());
					}
					ps.setObject(i + 1, value);

				}
				rs = ps.executeQuery();
				int columnCount = rs.getMetaData().getColumnCount();
				if (rs.next()) {
					for (int i = 0; i < columnCount; i++) {
						String columnName = rs.getMetaData()
								.getColumnName(i + 1).toUpperCase();
						String setMethod = "set" + columnName.substring(0, 1)
								+ columnName.substring(1).toLowerCase();
						Object value = rs.getObject(i + 1);
						String getMethod = setMethod.replaceFirst("set", "get");
						Class columnType = c.getMethod(getMethod, null)
								.getReturnType();
						if (columnType.getName().equals("long")) {
							if (value != null) {
								value = new Long(Long.parseLong(value
										.toString()));
							}
						} else if (columnType.getName().equals(
								"java.lang.String")) {
							if (value != null) {
								value = value.toString();
							}
						} else if (columnType.getName()
								.equals("java.util.Date")) {
							if (value != null) {
								value = (java.util.Date) value;
							}
						} else if (columnType.getName().equals("int")) {
							if (value != null) {
								value = Integer.valueOf(value.toString());
							}
						} else if (columnType.getName().equals("double")) {
							if (value != null) {
								value = Double.valueOf(value.toString());
							}
						} else if (columnType.getName().equals("float")) {
							if (value != null) {
								value = Float.valueOf(value.toString());
							}
						}

						Class[] agrType = new Class[1];
						agrType[0] = columnType;
						Object[] agrO = new Object[1];
						agrO[0] = value;
						if (value != null) {
							c.getMethod(setMethod, agrType).invoke(o, agrO);
						}
					}
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (!this.isInTransaction && con != null) {
				con.close();
			}
		}

		return o;
	}

	private boolean isInTransaction = false;

	public void beginTransaction() throws Exception {
		this.connection = this.connectionPool.getConnection();
		this.isInTransaction = true;
		this.connection.setAutoCommit(false);
	}

	public static void main(String[] agrs) {

		try {
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * The following method is used by DBParser /** execute databse query with
	 * no resultset return
	 * **/
	public void executeQuery(String sql) throws Exception {
		Connection dbConnection = this.getConnection();
		Statement stmt = dbConnection.createStatement();
		stmt.executeQuery(sql);
		stmt.close();
	}

	public void executeQuery(Vector sql) throws Exception {
		Connection dbConnection = this.getConnection();
		dbConnection.setAutoCommit(false);
		try {
			Statement stmt = dbConnection.createStatement();
			for (int i = 0; i < sql.size(); i++) {
				stmt.executeQuery(sql.elementAt(i).toString());
			}
			dbConnection.commit();
			stmt.close();
		} catch (SQLException ex) {
			dbConnection.rollback();
			throw ex;

		}
	}

	/**
	 * 
	 * @param sql
	 * @return single value of string
	 * @throws Exception
	 */
	public String selectSingleValue(String sql) throws Exception {
		Connection dbConnection = this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		String value = null;
		if (rs.next()) {
			value = rs.getString(1);
		}
		return value;
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Vector selectSingleRecord(String sql) throws Exception {
		Connection dbConnection = this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columncount = rsmd.getColumnCount();
		Vector vdata = new Vector();
		if (rs.next()) {
			for (int i = 0; i < columncount; i++) {
				vdata.addElement(rs.getObject(i + 1));
			}
		}
		return vdata;
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Vector selectMultiRecord(String sql) throws Exception {
		Connection dbConnection = this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columncount = rsmd.getColumnCount();
		Vector data = new Vector();
		while (rs.next()) {
			Vector temp = new Vector();
			for (int i = 0; i < columncount; i++) {
				temp.addElement(rs.getObject(i + 1));
			}
			data.addElement(temp);
		}
		return data;
	}

	/**
	 * 
	 * @param table
	 * @param swhere
	 * @return record count is 1
	 */
	public Hashtable getDataWithNo(String table, String swhere)
			throws Exception {
		Connection dbConnection = this.getConnection();
		Hashtable bdData = new Hashtable();
		String sql = "select * from " + table + " " + swhere;
		Statement stmt = dbConnection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		String[] columnName = new String[columnCount];
		String[] columnType = new String[columnCount];

		for (int i = 0; i < columnCount; i++) {
			columnName[i] = rsmd.getColumnName(i + 1).toLowerCase();
			columnType[i] = rsmd.getColumnTypeName(i + 1).toLowerCase();
		}

		if (rs.next()) {
			for (int i = 0; i < columnCount; i++) {
				if (columnType[i].equals("date")) {
					java.util.Date date = rs.getDate((i + 1));
					if (date != null) {
						bdData.put(columnName[i], rs.getDate((i + 1)));
					}
				} else {
					String stemp = rs.getString((i + 1));
					stemp = stemp == null ? "" : stemp;
					// stemp = stemp.equals("0") ? "":stemp;
					bdData.put(columnName[i], stemp);
				}
			}
		}
		rs.close();
		stmt.close();
		return bdData;
	}

	/**
	 * 
	 * @param table
	 * @param swhere
	 * @return record count is > 1
	 */
	public Vector getData(String table) throws Exception {
		return this.getData(table, null);
	}

	/**
	 * 
	 * @param table
	 * @param swhere
	 * @return record count is > 1
	 */
	public Vector getData(String table, int begin, int end) throws Exception {
		return this.getData(table, null, begin, end);
	}

	/**
	 * 
	 * @param table
	 * @param swhere
	 * @return record count is > 1
	 */
	public Vector getData(String table, String swhere) throws Exception {
		Vector over = new Vector();
		Connection dbConnection = this.getConnection();
		Hashtable bdData = new Hashtable();
		String sql = "select * from " + table;
		if (swhere != null) {
			sql = sql + " " + swhere;

		}
		Statement stmt = dbConnection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		String[] columnName = new String[columnCount];
		int[] columnTypeNo = new int[columnCount];

		for (int i = 0; i < columnCount; i++) {
			columnName[i] = rsmd.getColumnName(i + 1).toLowerCase();
			columnTypeNo[i] = rsmd.getColumnType(i + 1);
		}

		while (rs.next()) {
			bdData = new Hashtable();
			for (int i = 0; i < columnCount; i++) {
				if (columnTypeNo[i] == 91) {
					java.util.Date date = rs.getDate((i + 1));
					if (date != null) {
						bdData.put(columnName[i], rs.getDate((i + 1)));
					}
				} else {
					String stemp = rs.getString((i + 1));
					stemp = stemp == null ? "" : stemp;
					// stemp = stemp.equals("0") ? "":stemp;
					bdData.put(columnName[i], stemp);
				}
			}
			over.addElement(bdData);

		} // while
		rs.close();
		stmt.close();
		return over;
	}

	/**
	 * 
	 * @param table
	 * @param swhere
	 * @return record count is > 1
	 */
	public Vector getData(String table, String swhere, int begin, int end)
			throws Exception {
		Vector over = new Vector();
		Connection dbConnection = this.getConnection();
		Hashtable bdData = new Hashtable();
		String sql = "select * from " + table;
		if (swhere != null) {
			sql = sql + " " + swhere;

		}
		Statement stmt = dbConnection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		String[] columnName = new String[columnCount];
		int[] columnTypeNo = new int[columnCount];

		for (int i = 0; i < columnCount; i++) {
			columnName[i] = rsmd.getColumnName(i + 1).toLowerCase();
			columnTypeNo[i] = rsmd.getColumnType(i + 1);
		}

		int no = 1;
		while (rs.next()) {
			if (no < begin || no > end) {
				no++;
				continue;
			}
			bdData = new Hashtable();
			for (int i = 0; i < columnCount; i++) {
				if (columnTypeNo[i] == 91) {
					java.util.Date date = rs.getDate((i + 1));
					if (date != null) {
						bdData.put(columnName[i], rs.getDate((i + 1)));
					}
				} else {
					String stemp = rs.getString((i + 1));
					stemp = stemp == null ? "" : stemp;
					// stemp = stemp.equals("0") ? "":stemp;
					bdData.put(columnName[i], stemp);
				}
			}
			over.addElement(bdData);
			no++;
		} // while
		rs.close();
		stmt.close();
		return over;
	}

}