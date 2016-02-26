package com.common.vogenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.common.db.DBAgentOO;

public class DBParser {

	public DBParser() {
	}

	/**
	 * return the column type of comments in hashtable, key: columnName,value :
	 * label
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Hashtable getColumnType(String tableName) throws Exception {
		String tablename1 = tableName.toUpperCase();
		String sql = "select * from " + tablename1;
		DBAgentOO dbAgent = new DBAgentOO();

		Connection con = dbAgent.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		Hashtable ht = new Hashtable();
		int columnCount = rsmd.getColumnCount();

		for (int i = 0; i < columnCount; i++) {
			String name = rsmd.getColumnLabel(i + 1).toLowerCase();
			String type = rsmd.getColumnTypeName(i + 1).toLowerCase();

			System.out.println("column name = " + name + "  type = " + type);
			ht.put(name, type);
		}
		return ht;
	}

	public List getColumnProfile(String tableName) throws Exception {
		String tablename1 = tableName.toUpperCase();
		String sql = "select * from " + tablename1;
		DBAgentOO dbAgent = new DBAgentOO();

		Connection con = dbAgent.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();

		int columnCount = rsmd.getColumnCount();
		List result = new ArrayList();
		for (int i = 0; i < columnCount; i++) {
			ColumnProfile cp = new ColumnProfile();
			String name = rsmd.getColumnLabel(i + 1).toLowerCase();
			String type = rsmd.getColumnTypeName(i + 1).toLowerCase();
			cp.setColumnName(name);
			cp.setColumnType(type);
			cp.setPrecision(rsmd.getPrecision(i + 1));
			cp.setScale(i + 1);
			result.add(cp);
		}
		return result;
	}

	/**
	 * return the column type of comments in hashtable, key: columnName,value :
	 * label
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Vector getColumnName(String tableName) throws Exception {
		String tablename1 = tableName.toUpperCase();
		String sql = "select * from " + tablename1;
		DBAgentOO dbAgent = new DBAgentOO();

		Connection con = dbAgent.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		Vector v_name = new Vector();
		int columnCount = rsmd.getColumnCount();

		for (int i = 0; i < columnCount; i++) {
			String name = rsmd.getColumnLabel(i + 1).toLowerCase();
			v_name.addElement(name);
		}
		return v_name;
	}

	/**
	 * return the column isNullable in hashtable, key: columnName,value : flag
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Hashtable getColumnIsNullable(String tableName) throws Exception {
		String tablename1 = tableName.toUpperCase();
		String sql = "select * from " + tablename1;
		DBAgentOO dbAgent = new DBAgentOO();

		Connection con = dbAgent.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		Hashtable ht = new Hashtable();
		int columnCount = rsmd.getColumnCount();

		for (int i = 0; i < columnCount; i++) {
			String name = rsmd.getColumnLabel(i + 1).toLowerCase();
			String isnullflag = rsmd.isNullable(i + 1) + "";
			ht.put(name, isnullflag);
		}
		return ht;
	}

	/**
	 * return the column label of comments in hashtable, key: columnName,value :
	 * label
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Hashtable getColumnLabel(String tableName) throws Exception {
		String tablename1 = tableName.toUpperCase();
		String sql = "select column_name,comments from user_col_comments where table_name='"
				+ tablename1 + "'";
		DBAgent_Parse dbAgent = new DBAgent_Parse();
		Vector data = dbAgent.selectMultiRecord(sql);

		sql = "select count(*) from user_tab_columns where table_name='"
				+ tablename1 + "'";
		int count = Integer.parseInt(dbAgent.selectSingleValue(sql));

		if (data.size() != count)
			throw new Exception("Table : " + tableName
					+ " column comments count not equals column count");
		Hashtable ht = new Hashtable();
		for (int i = 0; i < data.size(); i++) {
			Vector temp = (Vector) data.elementAt(i);
			ht.put(temp.elementAt(0).toString().toLowerCase(), temp
					.elementAt(1).toString());
		}
		return ht;
	}

	/**
	 * return the column data length of comments in hashtable, key:
	 * columnName,value : label
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public Hashtable getColumnDataLength(String tableName) throws Exception {
		String tablename1 = tableName.toUpperCase();
		String sql = "select * from user_tab_columns where table_name='"
				+ tablename1 + "'";

		DBAgent_Parse dbAgent = new DBAgent_Parse();
		Vector data = dbAgent.selectMultiRecord(sql);

		Hashtable ht = new Hashtable();
		for (int i = 0; i < data.size(); i++) {
			Vector temp = (Vector) data.elementAt(i);
			ht.put(temp.elementAt(1).toString().toLowerCase(), temp
					.elementAt(5).toString());
		}
		return ht;
	}

	/**
	 * return table label of comments
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public String getTableLabel(String tableName) throws Exception {
		String tablename1 = tableName.toUpperCase();
		String sql = "select comments from user_tab_comments where table_name='"
				+ tablename1 + "'";
		DBAgent_Parse dbAgent = new DBAgent_Parse();
		String tablecomment = dbAgent.selectSingleValue(sql);
		if (tablecomment == null)
			throw new Exception(" Table : " + tableName + " have not comments");
		return tablecomment;
	}

	/**
	 * get Table reference columnname and tablename
	 * 
	 * @param args
	 *            , vector No.1 child column, No.2 father table, No.3 father
	 *            column
	 */
	public Vector getReferenceColumn(String tableName) throws Exception {
		String tablename1 = tableName.toUpperCase();
		String sql = " select a.c_child,b.t_father,b.c_father from ("
				+ " select user_cons_columns.table_name t_chlid,user_cons_columns.column_name c_child, "
				+ " user_constraints.r_constraint_name aa from user_cons_columns,user_constraints  "
				+ " where user_cons_columns.constraint_name=user_constraints.constraint_name "
				+ " and constraint_type='R' and user_cons_columns.table_name='"
				+ tablename1
				+ "' ) a,"
				+ " ("
				+ " select user_cons_columns.constraint_name aa,user_cons_columns.table_name t_father,"
				+ " user_cons_columns.column_name c_father from user_cons_columns ,user_constraints  where "
				+ " user_cons_columns.constraint_name=user_constraints.constraint_name "
				+ " and constraint_type='P') b " + " where a.aa = b.aa";
		Vector temp = new DBAgent_Parse().selectMultiRecord(sql);
		for (int i = 0; i < temp.size(); i++) {
			Vector temp1 = (Vector) temp.elementAt(i);
			for (int j = 0; j < temp1.size(); j++) {
				String s_temp = temp1.elementAt(j).toString().toLowerCase();
				temp1.insertElementAt(s_temp, j);
				temp1.removeElementAt(j + 1);
			}
		}
		return temp;
	}

	public static void main(String[] args) {
		try {
			DBParser DBParser1 = new DBParser();
			Hashtable ht = DBParser1.getColumnIsNullable("customer");
			Enumeration en = ht.keys();
			while (en.hasMoreElements()) {
				String key = en.nextElement().toString();
				System.out.println(key + " : " + ht.get(key).toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}