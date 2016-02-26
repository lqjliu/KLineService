package com.bgj.analysis;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bgj.dao.GhlhDAO;
import com.bgj.dao.StockAnalysingAccessor;
import com.bgj.dao.StockAnalysingDataFileAccessor;
import com.bgj.dao.StockdailyinfoVO;
import com.bgj.exception.KLineException;
import com.bgj.util.DateUtil;

public class LXXDDataAnalyst {
	public static void analyse(Date date) throws KLineException {
		List<StockdailyinfoVO> meetList = new ArrayList<StockdailyinfoVO>();
		String strategyName = "LXXD";
		String day = DateUtil.formatDay(date);
		int dayCount = 5;
		double priceRange1 = 9;
		double priceRange2 = 25;

		String sql = "SELECT * FROM stockdailyinfo WHERE DATE LIKE '" + day
				+ "%'  AND todayopenprice != 0 AND zdf < 0";

		List<StockdailyinfoVO> list = GhlhDAO.list(sql,
				"com.bgj.dao.StockdailyinfoVO");

		int no = 0;
		if (list.size() == 0) {
			return;
		}
		for (int k = 0; k < list.size(); k++) {
			StockdailyinfoVO sqb = (StockdailyinfoVO) list.get(k);
			no++;
			//System.out.println(" no = " + no);

			String sql1 = " SELECT COUNT(a.stockid) FROM ( "
					+ " SELECT * FROM stockdailyinfo WHERE stockid = '"
					+ sqb.getStockid() + "' AND DATE < '" + day
					+ "'  AND todayopenprice != 0 ORDER BY DATE DESC LIMIT 0,"
					+ (dayCount - 1) + ") a WHERE a.zdf >=0";

			int num = getRecordCount(sql1);
			if (num == 0 && sqb.getCurrentprice() > priceRange1
					&& sqb.getCurrentprice() < priceRange2) {
				meetList.add(sqb);
			}
		}
		StockAnalysingAccessor sars = new StockAnalysingDataFileAccessor();
		String additionalInfo = dayCount + "-" + priceRange1 + "-"
				+ priceRange2;
		sars.save(meetList, strategyName, date, additionalInfo);
	}

	private static int getRecordCount(String sql1) {
		String sNum = GhlhDAO.selectSingleValue(sql1);

		int num = 0;
		if (sNum != null && !sNum.equals("")) {
			num = Integer.parseInt(sNum);
		}
		return num;
	}

	public static void main(String[] args) {
		Date now = new Date();
		// Calendar calendar = Calendar.getInstance();
		// calendar.set(Calendar.MONTH, 10);
		// calendar.set(Calendar.DAY_OF_MONTH, 6);
		// now = calendar.getTime();
		// try {
		// analyse(now);
		// } catch (KLineException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		analyseMultipleDay();

	}

	private static void analyseMultipleDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 9);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
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
