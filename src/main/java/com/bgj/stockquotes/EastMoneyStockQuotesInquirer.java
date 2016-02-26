package com.bgj.stockquotes;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.bgj.util.HttpUtil;

public class EastMoneyStockQuotesInquirer extends InternetStockQuotesInquirer {
	private static Logger logger = Logger
			.getLogger(EastMoneyStockQuotesInquirer.class);

	protected String getStockQuotesPageURL(String stockId, boolean isSZ) {
		String url = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/CompatiblePage.aspx?Type=f&jsName=js&stk=";
		String url1 = "&Reference=xml&rt=0.7897900253331537";

		if (stockId.startsWith("6")) {
			stockId += "1";
		} else {
			stockId += "2";
		}
		url = url + stockId + url1;
		return url;

	}

	protected String getCharset() {
		return "utf-8";
	}

	protected StockQuotesBean parseStockQuotes(String stockInfo)
			throws StockQuotesException {
		try {
			StockQuotesBean result = new StockQuotesBean();
			stockInfo = stockInfo.substring(stockInfo.indexOf("\"") + 1);
			stockInfo = stockInfo.substring(0, stockInfo.indexOf("\""));
			if ("".equals(stockInfo)) {
				return null;
			}
			Pattern pattern = Pattern.compile(",");
			String[] stockInfoPieces = pattern.split(stockInfo);
			if (stockInfoPieces.length == 0 || stockInfoPieces.length == 1) {
				return null;
			}
			assembleStockQuotesBean(result, stockInfoPieces);

			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new StockQuotesException(
					"There come up with error while parsing " + stockInfo, ex);
		}
	}

	private void assembleStockQuotesBean(StockQuotesBean result,
			String[] stockInfoPieces) {
		result.setStockId(stockInfoPieces[1]);
		result.setName(stockInfoPieces[2]);
		result.setCurrentPrice(Double.parseDouble(stockInfoPieces[3]));
		result.setZde(Double.parseDouble(stockInfoPieces[4]));
		String sZdf = stockInfoPieces[5].substring(0,
				stockInfoPieces[5].length() - 1);
		result.setZdf(Double.parseDouble(sZdf));
		result.setTodayOpen(Double.parseDouble(stockInfoPieces[8]));
		result.setYesterdayClose(Double.parseDouble(stockInfoPieces[9]));
		result.setHighestPrice(Double.parseDouble(stockInfoPieces[10]));
		result.setLowestPrice(Double.parseDouble(stockInfoPieces[11]));
		String sHsl = stockInfoPieces[12].substring(0,
				stockInfoPieces[12].length() - 1);
		result.setHsl(Double.parseDouble(sHsl));
		result.setCjl(Double.parseDouble(stockInfoPieces[17]));

	}

	public StockQuotesBean getZSStockQuotes(String zsID) {
		String content = null;
		StockQuotesBean result = null;
		try {
			result = new StockQuotesBean();
			if (zsID.startsWith("0")) {
				zsID += "1";
			} else {
				zsID += "2";
			}
			String url = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/Index.aspx?Type=z&ids="
					+ zsID + "&jsname=js&Reference=xml&rt=0.06070799150065054";
			content = accessURLWithRetry(url);

			content = content.substring(content.indexOf("\"") + 1);
			content = content.substring(0, content.indexOf("\""));
			if ("".equals(content)) {
				return null;
			}
			Pattern pattern = Pattern.compile(",");
			String[] stockInfoPieces = pattern.split(content);
			if (stockInfoPieces.length == 0 || stockInfoPieces.length == 1) {
				return null;
			}
			assembleStockQuotesBeanForZS(result, stockInfoPieces);
			return result;
		} catch (Exception ex) {
			logger.error("parse content = " + content, ex);
			return result;
		}

	}

	public final int RE_TRY_COUNT = 5;

	public String accessURLWithRetry(String url) {
		String content = HttpUtil.accessInternet(url);
		for (int i = 0; i < RE_TRY_COUNT; i++) {
			if (content.indexOf("Service Unavailable") >= 0) {
				logger.error("Service unavaiable , re-try for " + (i + 1)
						+ " times");
				content = accessURLWithPause(url);
			} else {
				break;
			}
		}
		return content;
	}

	public String accessURLWithPause(String url) {
		String content;
		try {
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		content = HttpUtil.accessInternet(url);
		return content;
	}

	private void assembleStockQuotesBeanForZS(StockQuotesBean result,
			String[] stockInfoPieces) {
		result.setStockId(stockInfoPieces[1]);
		result.setName(stockInfoPieces[2]);
		result.setYesterdayClose(Double.parseDouble(stockInfoPieces[3]));
		result.setTodayOpen(Double.parseDouble(stockInfoPieces[4]));
		result.setCurrentPrice(Double.parseDouble(stockInfoPieces[5]));
		result.setHighestPrice(Double.parseDouble(stockInfoPieces[6]));
		result.setLowestPrice(Double.parseDouble(stockInfoPieces[7]));

		result.setZde(Double.parseDouble(stockInfoPieces[10]));
		String sZdf = stockInfoPieces[11].substring(0,
				stockInfoPieces[11].length() - 1);
		result.setZdf(Double.parseDouble(sZdf));
	}

	public static void main(String[] args) {
		try {
			EastMoneyStockQuotesInquirer internetStockQuotesInquirer = new EastMoneyStockQuotesInquirer();
			StockQuotesBean stockQuotesBean = internetStockQuotesInquirer
					.getStockQuotesBean("600036");
			System.out.println(stockQuotesBean);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
