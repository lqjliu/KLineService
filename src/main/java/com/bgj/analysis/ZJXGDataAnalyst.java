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

public class ZJXGDataAnalyst {
	public static void analyse(Date date) throws KLineException {
		List<StockdailyinfoVO> meetList = new ArrayList<StockdailyinfoVO>();
		String strategyName = "LSXG";

		int dayCount = 40;

		int lastPeakShortestDays = 20;
		int lastPeakLongestDays = 40;
		String day = DateUtil.formatDay(date);

		List<StockdailyinfoVO> list = DataAnalystUtil.getProcessStockList(date);

		int no = 0;
		for (int k = 0; k < list.size(); k++) {
			StockdailyinfoVO sqb = (StockdailyinfoVO) list.get(k);
			no++;
			//System.out.println(" no = " + no);

			String sql1 = " SELECT COUNT(*) FROM ( "
					+ "  SELECT MAX(a.currentprice) maxcurrentprice, a.stockid FROM "
					+ "  (SELECT stockid, DATE, currentprice FROM stockdailyinfo WHERE stockid = '"
					+ sqb.getStockid() + "' AND DATE < '" + day
					+ "' AND todayopenprice != 0 " + "   ORDER BY DATE DESC "
					+ getDayCountCondition(dayCount)
					+ ") a ) b  WHERE b.maxcurrentprice <= "
					+ sqb.getCurrentprice();
			
			//System.out.println("sql = " + sql1);
			int num = getRecordCount(sql1);
			if (num == 1) {
				boolean isMeet = true;
				if (lastPeakShortestDays > 0 || lastPeakLongestDays > 0) {
					isMeet = isIn2PeakRange(sqb, date, dayCount,
							lastPeakShortestDays, lastPeakLongestDays);
				}
				if (isMeet) {
					meetList.add(sqb);
				}
			}
		}
		StockAnalysingAccessor sars = new StockAnalysingDataFileAccessor();
		String additionalInfo = dayCount + "-" + lastPeakShortestDays + "-"
				+ lastPeakLongestDays;
		sars.save(meetList, strategyName, date, additionalInfo);
	}

	private static String getDayCountCondition(int dayCount) {
		String dayCountCondition = "";
		if (dayCount > 0) {
			dayCountCondition = " LIMIT 0, " + (dayCount - 1);
		}
		return dayCountCondition;
	}

	private static boolean isIn2PeakRange(StockdailyinfoVO sqb, Date date,
			int dayCount, int lastPeakShortestDays, int lastPeakLongestDays) {
		String day = DateUtil.formatDay(date);
		String nextDay = DateUtil.formatDay(DateUtil.getNextDay(date));

		String sql = " SELECT COUNT(stockid) FROM stockdailyinfo WHERE DATE > "
				+ " (SELECT b.Date FROM "
				+ " (SELECT MAX(a.currentprice) maxcurrentprice, a.stockid, DATE FROM "
				+ " (SELECT stockid, DATE, currentprice FROM stockdailyinfo WHERE stockid = '"
				+ sqb.getStockid() + "' AND DATE < '" + day
				+ "' AND todayopenprice != 0 " + " ORDER BY currentPrice DESC "
				+ getDayCountCondition(dayCount) + ") a )b) AND stockid = "
				+ sqb.getStockid() + " AND DATE < '" + nextDay + "' ";
		//System.out.println("sql1 = " + sql);
		int lastPeakDays = getRecordCount(sql);
		if (lastPeakShortestDays == 0) {
			lastPeakShortestDays = lastPeakLongestDays;
		}
		if (lastPeakLongestDays == 0) {
			lastPeakLongestDays = lastPeakShortestDays;
		}
		if (lastPeakDays >= lastPeakShortestDays
				&& lastPeakDays <= lastPeakLongestDays) {
			return true;
		}
		return false;
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
