package com.bgj.stockquotes;

import java.util.regex.Pattern;

public class SohuStockQuotesInquirer extends InternetStockQuotesInquirer {
	protected String getStockQuotesPageURL(String stockID, boolean isSZ) {
		String postStockID = stockID.substring(3);
		String type = "cn";
		if (isSZ) {
			type = "zs";
		}
		String url = "http://hq.stock.sohu.com/" + type + "/" + postStockID
				+ "/" + type + "_" + stockID + "-1.html";
		System.out.println("url = " + url);
		return url;
	}

	private final static int STOCKID_POSITION = 0;
	private final static int NAME_POSITION = 1;
	private final static int CURRENT_PRICE_POSITION = 2;
	private final static int ZDF_POSITION = 3;
	private final static int ZDE_POSITION = 4;

	protected StockQuotesBean parseStockQuotes(String stockInfo)
			throws StockQuotesException {
		try {
			stockInfo = stockInfo.substring(stockInfo.indexOf("[") + 1,
					stockInfo.indexOf("]"));
			stockInfo = stockInfo.substring(stockInfo.indexOf("[") + 2);

			StockQuotesBean result = new StockQuotesBean();
			Pattern pattern = Pattern.compile("[',]+");
			String[] stockInfoPieces = pattern.split(stockInfo);
			result.setStockId(stockInfoPieces[STOCKID_POSITION].substring(3));
			result.setName(stockInfoPieces[NAME_POSITION]);
			result.setCurrentPrice(Double
					.parseDouble(stockInfoPieces[CURRENT_PRICE_POSITION]));
			result.setZdf(Double.parseDouble(stockInfoPieces[ZDF_POSITION]
					.substring(0, stockInfoPieces[ZDF_POSITION].length() - 1)));
			result.setZde(Double.parseDouble(stockInfoPieces[ZDE_POSITION]));
			return result;
		} catch (Exception ex) {

			ex.printStackTrace();
			throw new StockQuotesException(
					"There come up with error while parsing " + stockInfo, ex);
		}
	}

	public static void main(String[] args) {
		try {
			StockQuotesInquirer internetStockQuotesInquirer = new SohuStockQuotesInquirer();
			StockQuotesBean stockQuotesBean = internetStockQuotesInquirer
					.getStockQuotesBean("600036");
			System.out.println(stockQuotesBean);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
