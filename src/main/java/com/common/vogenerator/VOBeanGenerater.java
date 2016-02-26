package com.common.vogenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class VOBeanGenerater {
	public VOBeanGenerater() {
	}

	public DBParser getDBParser() {
		DBParser dbParser = new DBParser();
		return dbParser;
	}

	public void generateVOBean(String tableName, String packageName1)
			throws Exception {
		Vector columnName = this.getDBParser().getColumnName(tableName);
		Hashtable ht_type = this.getDBParser().getColumnType(tableName);
		List list = this.getDBParser().getColumnProfile(tableName);
		// Hashtable ht_length =
		// this.getDBParser().getColumnDataLength(tableName);
		String fileSep = System.getProperty("file.separator");
		// String packageName1 = "com.system.vo";

		String tableName_lowcase = tableName.toLowerCase();
		String tableName_upcase = tableName.toUpperCase();
		String tableName_firUp = tableName_upcase.substring(0, 1)
				+ tableName_lowcase.substring(1);
		String beanName = tableName_firUp + "VO";

		String currentDir = System.getProperty("user.dir");

		String packageName = this.replacePointToSep(packageName1);
		String packageDir = currentDir + fileSep + "src" + fileSep + "main"
				+ fileSep + "java" + fileSep + packageName;
		if (packageName.equals("")) {
			packageDir = currentDir + fileSep + "src";
		}
		File file = new File(packageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String beanfile = packageDir + fileSep + beanName + ".java";
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				beanfile)));

		out.println("package " + packageName1 + ";");
		out.println();
		out.println("public class " + beanName
				+ " implements java.io.Serializable{");
		out.println("  public static final String TABLE_NAME = \"" + tableName
				+ "\";");

		for (int i = 0; i < columnName.size(); i++) {
			String columnName1 = columnName.elementAt(i).toString();
			String columnName1_lowcase = columnName1.toLowerCase();
			String columnName1_First_Upcase = columnName1.toUpperCase()
					.substring(0, 1) + columnName1_lowcase.substring(1);
			String type = ht_type.get(columnName.elementAt(i)).toString();
			String s_type = "";
			ColumnProfile cp = (ColumnProfile) list.get(i);
			s_type = this.getJavaType(type, cp);

			out.println("  private boolean has" + columnName1_First_Upcase
					+ ";");
			out.println("  public boolean isHas" + columnName1_First_Upcase
					+ "(){");
			out.println("    return has" + columnName1_First_Upcase + ";");
			out.println("  }");

			out.println("  private boolean where" + columnName1_First_Upcase
					+ ";");
			out.println("  public boolean isWhere" + columnName1_First_Upcase
					+ "(){");
			out.println("    return this.where" + columnName1_First_Upcase
					+ ";");
			out.println("  }");
			out.println("  public void setWhere" + columnName1_First_Upcase
					+ "(boolean where" + columnName1_First_Upcase + "){");
			out.println("    this.where" + columnName1_First_Upcase
					+ " = where" + columnName1_First_Upcase + ";");
			out.println("  }");

			out.println("  private " + s_type + " " + columnName1_lowcase + ";");
			out.println("  public void set" + columnName1_First_Upcase + "("
					+ s_type + " " + columnName1_lowcase + "){");
			out.println("    this.has" + columnName1_First_Upcase + " = true;");
			out.println("    this." + columnName1_lowcase + " = "
					+ columnName1_lowcase + ";");
			out.println("  }");
			out.println("  public " + s_type + " get"
					+ columnName1_First_Upcase + "(){");
			out.println("    return " + columnName1_lowcase + ";");
			out.println("  }");

		}

		out.println("}");
		out.close();

	}

	// public String getJavaType(String type) {
	// String s_type = this.getOracleJavaType(type);
	// return s_type;
	// }

	public String getJavaType(String type, ColumnProfile cp) {

		String s_type = this.getMySQLJavaType(type, cp);
		return s_type;
	}

	public String getAccessJavaType(String type) {
		String s_type = null;
		if (type.toLowerCase().equals("datetime")) {
			s_type = "java.util.Date";
		} else if (type.toLowerCase().equals("integer")) {
			s_type = "int";
		} else if (type.toLowerCase().equals("double")) {
			s_type = "double";
		} else if (type.toLowerCase().equals("real")) {
			s_type = "float";
		}

		else {
			s_type = "String";
		}
		return s_type;
	}

	public String getMySQLJavaType(String type, ColumnProfile cp) {
		String s_type = null;
		if (type.toLowerCase().equals("date")
				|| type.toLowerCase().equals("timestamp")) {
			s_type = "java.util.Date";
		} else if (type.equals("int") || type.equals("double")
				|| type.equals("float")) {
			s_type = type;
		} else {
			s_type = "String";
		}
		return s_type;
	}

	public String getOracleJavaType(String type, ColumnProfile cp) {
		String s_type = null;
		if (type.toLowerCase().equals("date")) {
			s_type = "java.util.Date";
		} else if (type.toLowerCase().equals("number")) {
			if (cp.getScale() > 0) {
				s_type = "double";

			} else {
				s_type = "long";
			}
		} else {
			s_type = "String";
		}
		return s_type;
	}

	public void generateMapingSet(String tableName) throws Exception {
		Vector columnName = this.getDBParser().getColumnName(tableName);
		Hashtable ht_type = this.getDBParser().getColumnType(tableName);
		Hashtable ht_length = this.getDBParser().getColumnDataLength(tableName);
		String fileSep = System.getProperty("file.separator");
		String packageName1 = "com.webex.platform.vo.mw.mc";

		String tableName_lowcase = tableName.toLowerCase();
		String tableName_upcase = tableName.toUpperCase();
		String tableName_firUp = tableName_upcase.substring(0, 1)
				+ tableName_lowcase.substring(1);
		String beanName = tableName_firUp + "Temp";

		String currentDir = System.getProperty("user.dir");

		String packageName = this.replacePointToSep(packageName1);
		String packageDir = currentDir + fileSep + "src" + fileSep
				+ packageName;
		if (packageName.equals("")) {
			packageDir = currentDir + fileSep + "src";
		}
		File file = new File(packageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String beanfile = packageDir + fileSep + beanName + ".java";
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				beanfile)));

		// out.println("package " + packageName1 + ";");
		out.println();
		// out.println("public class " + beanName + " {");
		for (int i = 0; i < columnName.size(); i++) {
			String columnName1 = columnName.elementAt(i).toString();
			String columnName1_lowcase = columnName1.toLowerCase();
			String columnName1_First_Upcase = columnName1.toUpperCase()
					.substring(0, 1) + columnName1_lowcase.substring(1);
			String type = ht_type.get(columnName.elementAt(i)).toString();
			String s_type = "";
			String typename = "";
			if (type.equals("date")) {
				s_type = "java.util.Date";
				typename = "Date";
			} else if (type.equals("number")) {
				s_type = "long";
				typename = "Long";
			} else {
				s_type = "String";
				typename = "String";
			}
			// out.println(" private " + s_type + " " + columnName1_lowcase +
			// ";");
			out.println("        vo.set" + columnName1_First_Upcase + "(rs.get"
					+ typename + "(\"" + columnName1_First_Upcase + "\"));");
		}

		// out.println("}");
		out.close();

	}

	public void generateInsertSQL(String tableName) throws Exception {
		Vector columnName = this.getDBParser().getColumnName(tableName);
		Hashtable ht_type = this.getDBParser().getColumnType(tableName);
		Hashtable ht_length = this.getDBParser().getColumnDataLength(tableName);
		String fileSep = System.getProperty("file.separator");
		String packageName1 = "com.webex.platform.vo.mw.mc";

		String tableName_lowcase = tableName.toLowerCase();
		String tableName_upcase = tableName.toUpperCase();
		String tableName_firUp = tableName_upcase.substring(0, 1)
				+ tableName_lowcase.substring(1);
		String beanName = tableName_firUp + "InsertSQL";

		String currentDir = System.getProperty("user.dir");

		String packageName = this.replacePointToSep(packageName1);
		String packageDir = currentDir + fileSep + "src" + fileSep
				+ packageName;
		if (packageName.equals("")) {
			packageDir = currentDir + fileSep + "src";
		}
		File file = new File(packageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String beanfile = packageDir + fileSep + beanName + ".java";
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				beanfile)));

		// out.println("package " + packageName1 + ";");
		out.println();
		// out.println("public class " + beanName + " {");
		String sql1 = "";
		String sql2 = "";
		for (int i = 0; i < columnName.size(); i++) {
			String columnName1 = columnName.elementAt(i).toString();
			sql1 += " " + columnName1 + ",";
			sql2 += " ?,";
		}
		String sql = "insert into " + tableName + "( " + sql1 + " )values( "
				+ sql2 + " )";
		out.println(sql);
		out.close();

	}

	public void generateInsertMaping(String tableName) throws Exception {
		Vector columnName = this.getDBParser().getColumnName(tableName);
		Hashtable ht_type = this.getDBParser().getColumnType(tableName);
		Hashtable ht_length = this.getDBParser().getColumnDataLength(tableName);
		String fileSep = System.getProperty("file.separator");
		String packageName1 = "com.webex.platform.vo.mw.mc";

		String tableName_lowcase = tableName.toLowerCase();
		String tableName_upcase = tableName.toUpperCase();
		String tableName_firUp = tableName_upcase.substring(0, 1)
				+ tableName_lowcase.substring(1);
		String beanName = tableName_firUp + "InsertMappingSQL";

		String currentDir = System.getProperty("user.dir");

		String packageName = this.replacePointToSep(packageName1);
		String packageDir = currentDir + fileSep + "src" + fileSep
				+ packageName;
		if (packageName.equals("")) {
			packageDir = currentDir + fileSep + "src";
		}
		File file = new File(packageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String beanfile = packageDir + fileSep + beanName + ".java";
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				beanfile)));

		// out.println("package " + packageName1 + ";");
		out.println();
		// out.println("public class " + beanName + " {");
		String sql1 = "";
		String sql2 = "";
		for (int i = 0; i < columnName.size(); i++) {
			String columnName1 = columnName.elementAt(i).toString();
			String columnName1_lowcase = columnName1.toLowerCase();
			String columnName1_First_Upcase = columnName1.toUpperCase()
					.substring(0, 1) + columnName1_lowcase.substring(1);
			String type = ht_type.get(columnName.elementAt(i)).toString();
			String s_type = "";
			String typename = "";
			if (type.equals("date")) {
				s_type = "java.util.Date";
				typename = "Date";
			} else if (type.equals("number")) {
				s_type = "long";
				typename = "Long";
			} else {
				s_type = "String";
				typename = "String";
			}
			out.println("pstmt.set" + typename + "(pos++, vo.get"
					+ columnName1_First_Upcase + "());");

		}
		// String sql = "insert into " + tableName + "( "+sql1+" )values(
		// "+sql2+" )";
		// out.println(sql);
		out.close();

	}

	String replacePointToSep(String packageName) {
		String fileSep = System.getProperty("file.separator");
		String package1 = "";
		while (packageName.indexOf(".") > 0) {
			package1 += packageName.substring(0, packageName.indexOf("."));
			package1 += fileSep;
			packageName = packageName.substring(packageName.indexOf(".") + 1);
		}
		package1 += packageName;
		return package1;
	}

	public static void main(String[] args) {
		try {
			VOBeanGenerater vobg = new VOBeanGenerater();
			String[] tablename = { "stockstrategyinfo" };
			for (int i = 0; i < tablename.length; i++) {
				vobg.generateVOBean(tablename[i], "com.bgj.dao");
			}
			System.out.println("Generate Code Successful");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}