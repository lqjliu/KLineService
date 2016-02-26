package com.common.util;

import com.common.db.DBAgentOO;

public class IDGenerator {
	public static int generateId(String table) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		int result = 0;
		try {
			String id = dbAgentOO.selectSingleValue("select max(id) from "
					+ table);
			if (id != null && !id.equals("")) {
				result = Integer.parseInt(id);
			}
			result++;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
