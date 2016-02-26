package com.bgj.analysis;

import java.util.Date;
import java.util.List;

import com.bgj.dao.GhlhDAO;
import com.bgj.dao.StockdailyinfoVO;
import com.bgj.util.DateUtil;

public class DataAnalystUtil {
	public static List<StockdailyinfoVO> getProcessStockList(Date date) {
		String day = DateUtil.formatDay(date);

		String sql = "SELECT * FROM stockdailyinfo WHERE DATE LIKE '" + day
				+ "%'  AND todayopenprice != 0";

		List<StockdailyinfoVO> result = GhlhDAO.list(sql,
				"com.bgj.dao.StockdailyinfoVO");

		return result;
	}

	public static List<StockdailyinfoVO> getTodayProcessStockList() {
		Date now = new Date();
		return getProcessStockList(now);
	}

}
