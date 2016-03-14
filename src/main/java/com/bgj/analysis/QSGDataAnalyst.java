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

public class QSGDataAnalyst {
	public static void analyse(Date date) throws KLineException {
		String strategyName = "QSG";
		int days = 2;
		int percentage = 20;
		String day = DateUtil.formatDay(date);
		String previousDay = DateUtil.formatDay(DateUtil
				.getPreviousMarketOpenDay(date, days));

		String sql = "select b.* from (select * from stockdailyinfo where date like '"
				+ previousDay
				+ "%') a, (select * from stockdailyinfo where date like '"
				+ day
				+ "%') b where a.stockid=b.stockid and (b.currentPrice - a.yesterdaycloseprice)/a.yesterdaycloseprice * 100 > "
				+ percentage;

		List<StockdailyinfoVO> list = GhlhDAO.list(sql,
				"com.bgj.dao.StockdailyinfoVO");
		if (list != null && list.size() > 0) {
			StockAnalysingAccessor sars = new StockAnalysingDataFileAccessor();
			sars.save(list, strategyName, date);
		}
	}

	public static void main(String[] args) {
		Date now = new Date();
		// try {
		// //analyse(now);
		// } catch (KLineException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		analyseMultipleDay();

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
