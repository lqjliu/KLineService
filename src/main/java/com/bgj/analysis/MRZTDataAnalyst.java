package com.bgj.analysis;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bgj.dao.StockAnalysingDataFileAccessor;
import com.bgj.dao.GhlhDAO;
import com.bgj.dao.StockdailyinfoVO;
import com.bgj.dao.StockAnalysingAccessor;
import com.bgj.exception.KLineException;
import com.bgj.util.Constants;
import com.bgj.util.DateUtil;
import com.common.db.ConnectionPool;

public class MRZTDataAnalyst {
	public static void analyse(Date date) throws KLineException {
		String strategyName = "MRZT";
		int newStockDays = 60;
		String sql = "select b.* from "
				+ " (select stockid, count(stockid) days from stockdailyinfo where DATE < '"
				+ DateUtil.formatDay(date) + "' group by stockid)a,"
				+ " (SELECT * FROM stockdailyinfo WHERE zdf >= "
				+ Constants.ZT_THRESHOLD
				+ " AND highestPrice != lowestPrice and DATE like '"
				+ DateUtil.formatDay15(date) + "%' ORDER BY zdf DESC) b"
				+ " where a.stockid = b.stockid and a.days > " + newStockDays;

		System.out.println("sql = " + sql);
		List<StockdailyinfoVO> list = GhlhDAO.list(sql,
				"com.bgj.dao.StockdailyinfoVO");
		if (list != null && list.size() > 0) {
			StockAnalysingAccessor sars = new StockAnalysingDataFileAccessor();
			sars.save(list, strategyName, date);
		}

	}

	public static void main(String[] args) {
		Date now = new Date();
		try {
			analyse(now);
		} catch (KLineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// analyseMultipleDay();

	}

	private static void analyseMultipleDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 9);
		calendar.set(Calendar.DAY_OF_MONTH, 22);
		Date eachDay = calendar.getTime();
		Date now = new Date();
		while (eachDay.before(now)) {
			System.out.println("processing : " + eachDay);
			try {
				analyse(eachDay);
			} catch (KLineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			eachDay = DateUtil.getNextDay(eachDay);
		}
	}
}
