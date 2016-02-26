package com.common.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 
 * 
 * @author Robin
 * @version 1.0
 */

public class DBAgentOO {
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
	 * rollback the Transaction with connection
	 */
	public void releaseTransaction() throws Exception {
		if (this.isInTransaction) {
			this.connection.rollback();
			this.isInTransaction = false;
			this.connection.setAutoCommit(true);
			this.connection.close();
		}
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
				// System.out.println("exec sql = " + sql);
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
				// System.outprintln("exec sql = " + sql);
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
				// System.outprintln("exec sql = " + sql);
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
					getObjectFromRS(o, c, rs, columnCount);
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

	private void getObjectFromRS(Object o, Class c, ResultSet rs,
			int columnCount) throws SQLException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		for (int i = 0; i < columnCount; i++) {
			String columnName = rs.getMetaData().getColumnLabel(i + 1)
					.toUpperCase();

			String setMethod = "set" + columnName.substring(0, 1)
					+ columnName.substring(1).toLowerCase();
			Object value = rs.getObject(i + 1);
			String getMethod = setMethod.replaceFirst("set", "get");
			Class columnType = c.getMethod(getMethod, null).getReturnType();
			if (columnType.getName().equals("long")) {
				if (value != null) {
					value = new Long(Long.parseLong(value.toString()));
				}
			} else if (columnType.getName().equals("java.lang.String")) {
				if (value != null) {
					value = value.toString();
				}
			} else if (columnType.getName().equals("java.util.Date")) {
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
	}

	public Vector selectData(String sql) throws Exception {
		Connection conn = null;
		if (this.isInTransaction) {
			conn = this.connection;
		} else {
			conn = this.getConnection();
		}
		Statement st = conn.createStatement();
		Vector data = new Vector();
		try {
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Vector temp = new Vector();
				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
					Object o_temp = rs.getObject(i + 1);
					if (o_temp == null) {
						temp.addElement("");
					} else {
						temp.addElement(o_temp);
					}
				}
				data.addElement(temp);
			}
			rs.close();
			st.close();
		} finally {
			if (st != null) {
				st.close();
			}
			if (!this.isInTransaction && conn != null) {
				conn.close();
			}
		}

		return data;

	}

	public List selectDataList(String sql) throws Exception {
		// System.outprintln("sql = " + sql);
		Connection conn = null;
		if (this.isInTransaction) {
			conn = this.connection;
		} else {
			conn = this.getConnection();
		}
		Statement st = conn.createStatement();
		List data = new ArrayList();
		try {
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				List temp = new ArrayList();
				for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
					Object o_temp = rs.getObject(i + 1);
					if (o_temp == null) {
						temp.add("");
					} else {
						temp.add(o_temp);
					}
				}
				data.add(temp);
			}
			rs.close();
		} finally {
			if (st != null) {
				st.close();
			}
			if (!this.isInTransaction && conn != null) {
				conn.close();
			}
		}

		return data;

	}

	public List<Object> selectData(String sql, String className)
			throws Exception {
		return selectData(sql, className, 0, 0);
	}

	public List<Object> selectData(String sql, String className, int startPos,
			int size) throws Exception {

		// System.out.println("sql = " + sql);
		Connection conn = null;
		if (this.isInTransaction) {
			conn = this.connection;
		} else {
			conn = this.getConnection();
		}
		Statement st = conn.createStatement();
		List<Object> data = new ArrayList<Object>();
		try {
			ResultSet rs = st.executeQuery(sql);
			if (startPos != 1 && startPos != 0) {
				rs.absolute(startPos);
			}
			int count = 0;
			while (rs.next()) {
				Class c = Class.forName(className);
				Object o = c.getConstructor(null).newInstance(null);
				int columnCount = rs.getMetaData().getColumnCount();
				this.getObjectFromRS(o, c, rs, columnCount);
				data.add(o);
				count++;
				if (size > 0 && count >= size) {
					break;
				}
			}
			rs.close();
			st.close();
		} finally {
			if (st != null) {
				st.close();
			}
			if (!this.isInTransaction && conn != null) {
				conn.close();
			}
		}

		return data;
	}

	public String selectSingleValue(String sql) throws Exception {
		String result = null;

		List data = selectDataList(sql);
		if (data.size() == 0) {
			return null;
		} else {
			List v = (List) data.get(0);
			Object o = v.get(0);
			if (o == null) {
				return null;
			} else {
				return o.toString();
			}
		}
	}

	public List selectSingleList(String sql) throws Exception {
		String result = null;
		List data = selectDataList(sql);
		if (data.size() == 0) {
			return null;
		} else {
			List result1 = new ArrayList();
			for (int i = 0; i < data.size(); i++) {
				List v = (List) data.get(i);
				Object o = v.get(0);
				result1.add(o);
			}
			return result1;
		}
	}

	private boolean isInTransaction = false;

	public void beginTransaction() throws Exception {
		this.connection = connectionPool.getConnection();
		this.isInTransaction = true;
		this.connection.setAutoCommit(false);
	}

}