package com.bgj.collecting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.bgj.dao.GhlhDAO;
import com.bgj.dao.StockdailyinfoVO;
import com.bgj.stockquotes.EastMoneyStockQuotesInquirer;
import com.bgj.stockquotes.StockQuotesBean;
import com.bgj.util.Constants;
import com.bgj.util.DateUtil;
import com.bgj.util.EastMoneyUtil;
import com.bgj.util.HttpUtil;
import com.bgj.util.MathUtil;
import com.common.db.ConnectionPool;

public class HistoryDataCollector {
	public static Logger logger = Logger.getLogger(HistoryDataCollector.class);

	public static List<StockdailyinfoVO> collectData(String stockId,
			String name, double ltgb, String beginDate, String endDate) {
		List<StockdailyinfoVO> result = new ArrayList<StockdailyinfoVO>();
		String url = getHistoryDataURL(stockId, beginDate, endDate);

		String content = HttpUtil.accessInternetForMultiple(url);
		if (content == null || content.trim().equals("")) {
			return result;
		}
		Pattern pattern = Pattern.compile(" ");
		String[] dailyStockInfo = pattern.split(content);
		for (int i = 0; i < dailyStockInfo.length; i++) {
			StockdailyinfoVO stockdailyinfoVO = parseDailyStockInfo(stockId,
					name, ltgb, dailyStockInfo[i]);
			result.add(stockdailyinfoVO);
		}
		processYesterdayRelatedInfo(result);

		return result;
	}

	private static String getHistoryDataURL(String stockId, String beginDate,
			String endDate) {
		String marketStockId = null;
		if (stockId.startsWith("6")) {
			marketStockId = "sh" + stockId;
		} else {
			marketStockId = "sz" + stockId;
		}
		String url = "http://biz.finance.sina.com.cn/stock/flash_hq/kline_data.php?"
				+ "&rand=random(10000)&symbol="
				+ marketStockId
				+ "&end_date="
				+ endDate + "&begin_date=" + beginDate + "&type=plain";
		return url;
	}

	private static void processYesterdayRelatedInfo(
			List<StockdailyinfoVO> result) {
		for (int i = result.size() - 1; i >= 0; i--) {
			StockdailyinfoVO currentVO = ((StockdailyinfoVO) result.get(i));
			double yesterdayClosePrice = 0;
			if (i != 0) {
				yesterdayClosePrice = ((StockdailyinfoVO) result.get(i - 1))
						.getCurrentprice();
			} else {
				Date date = currentVO.getDate();
				String sDate = DateUtil.formatDate(date);
				String sql = "SELECT currentPrice FROM stockdailyinfo WHERE stockid = '"
						+ currentVO.getStockid()
						+ "' AND DATE < '"
						+ sDate
						+ "' ORDER BY DATE DESC";
				String price = GhlhDAO.selectSingleValue(sql);
				if (price != null && !price.equals("")) {
					yesterdayClosePrice = Double.parseDouble(price);
				}
			}
			if (yesterdayClosePrice != 0) {
				currentVO.setYesterdaycloseprice(yesterdayClosePrice);
				currentVO
						.setZde(MathUtil.formatDoubleWith2(currentVO
								.getCurrentprice()
								- currentVO.getYesterdaycloseprice()));
				currentVO.setZdf(MathUtil.formatDoubleWith2(currentVO.getZde()
						/ currentVO.getYesterdaycloseprice() * 100));
				currentVO.setZf(MathUtil.formatDoubleWith2((currentVO
						.getHighestprice() - currentVO.getLowestprice())
						/ currentVO.getYesterdaycloseprice() * 100));
			}

		}
	}

	private static StockdailyinfoVO parseDailyStockInfo(String stockId,
			String name, double ltgb, String dailyStockInfo) {
		Pattern pattern;
		pattern = Pattern.compile(",");
		String[] stockInfo = pattern.split(dailyStockInfo);
		StockdailyinfoVO stockdailyinfoVO = new StockdailyinfoVO();
		stockdailyinfoVO.setStockid(stockId);
		stockdailyinfoVO.setDate(DateUtil.parseDay(stockInfo[0]));
		stockdailyinfoVO.setName(name);
		stockdailyinfoVO.setTodayopenprice(Double.parseDouble(stockInfo[1]));
		stockdailyinfoVO.setHighestprice(Double.parseDouble(stockInfo[2]));
		stockdailyinfoVO.setCurrentprice(Double.parseDouble(stockInfo[3]));
		stockdailyinfoVO.setLowestprice(Double.parseDouble(stockInfo[4]));

		if (ltgb != 0) {
			double hsl = MathUtil.formatDoubleWith2(Double
					.parseDouble(stockInfo[5]) / ltgb * 100);
			stockdailyinfoVO.setHsl(hsl);
		}
		stockdailyinfoVO.setCjl(Integer.parseInt(stockInfo[5]));
		stockdailyinfoVO.setCreatedtime(Calendar.getInstance().getTime());
		stockdailyinfoVO.setLastmodifiedtime(Calendar.getInstance().getTime());
		return stockdailyinfoVO;
	}

	public static void collectHistoryData(String beginDate, String endDate,
			int startNo) {
		int[] marketCode = { Constants.SH_MARKET_CODE, Constants.SZ_MARKET_CODE };
		int count = Constants.SZ_STOCK_COUNT;
		int no = 0;
		for (int i = 0; i < marketCode.length; i++) {
			List<StockQuotesBean> list = EastMoneyUtil.collectData(count,
					marketCode[i]);
			for (int k = 0; k < list.size(); k++) {
				no++;
				if (no >= startNo) {
					StockQuotesBean sqb = list.get(k);
					System.out.println(" Processing : " + sqb.getStockId()
							+ " " + sqb.getName() + " no = " + no);
					try {
						collectDailyInfo(beginDate, endDate, sqb);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	private static void collectDailyInfo(String beginDate, String endDate,
			StockQuotesBean sqb) {
		int ltsz = (int) (sqb.getCjl() / (sqb.getHsl() / 100));
		List<StockdailyinfoVO> list1SV = collectData(sqb.getStockId(),
				sqb.getName(), ltsz, beginDate, endDate);
		for (int j = 0; j < list1SV.size(); j++) {
			GhlhDAO.create(list1SV.get(j));
		}
	}

	public static void main(String[] args) {
		StockQuotesBean sqb = new StockQuotesBean();
		sqb.setStockId("600036");
		sqb.setName("ÕÐÉÌÒøÐÐ");
		sqb.setCjl(5436800);
		sqb.setHsl(2.64);
		collectDailyInfo("20150414", "20150415", sqb);
	}

	public static void main1(String[] args) {
		if (args == null || args.length < 3) {
			System.out.println("Please input the parameters!");
			System.exit(0);
		}
		String db_url = "kline123.mysql.rds.aliyuncs.com";
		db_url = args[0];
		if (db_url == null && db_url.equals("")) {
			System.out.println("Please input the db url!");
			System.exit(0);
		}
		System.out.println("db_url = " + db_url);
		ConnectionPool.setDBURL(db_url);
		String beginDate = args[1];
		String endDate = args[2];

		String stockId = null; // "600036";
		if (stockId != null && stockId.length() == 6) {
			collectCertainStock(beginDate, endDate, stockId);
		} else {
			collectHistoryData(beginDate, endDate, 0);
		}
	}

	private static void collectCertainStock(String beginDate, String endDate,
			String stockId) {
		EastMoneyStockQuotesInquirer internetStockQuotesInquirer = new EastMoneyStockQuotesInquirer();
		StockQuotesBean stockQuotesBean = internetStockQuotesInquirer
				.getStockQuotesBean(stockId);
		collectDailyInfo(beginDate, endDate, stockQuotesBean);
	}
}
