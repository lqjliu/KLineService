package com.bgj.stockquotes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;



public abstract class InternetStockQuotesInquirer implements
		StockQuotesInquirer {
	private static Logger logger = Logger
			.getLogger(InternetStockQuotesInquirer.class);

	private static StockQuotesInquirer instance = new SinaStockQuotesInquirer();

	public static StockQuotesInquirer getInstance() {
		return instance;
	}

	private static StockQuotesInquirer eastMoneyInstance = new EastMoneyStockQuotesInquirer();

	public static StockQuotesInquirer getEastMoneyInstance() {
		return eastMoneyInstance;
	}

	public StockQuotesBean getStockQuotesBean(String stockId) {
		if (testingInjectStockQuotesBean != null) {
			return testingInjectStockQuotesBean;
		}
		StockQuotesBean result = null;
		try {
			boolean isSZ = isFromShenzhenMarket(stockId);
			String url = getStockQuotesPageURL(stockId, isSZ);
			String data = getStockQuotesInfoFromInternet(url);
			result = parseStockQuotes(data);
		} catch (StockQuotesException ex) {
			logger.error("getStockQuotesBean throw : ", ex);
		}

		if (result == null) {
			if (this.getClass().equals(SinaStockQuotesInquirer.class)) {
				logger.error("There didn't get stock through sina, so access through EastMOney");
				result = InternetStockQuotesInquirer.getEastMoneyInstance()
						.getStockQuotesBean(stockId);
			}
		}
		return result;
	}

	private StockQuotesBean testingInjectStockQuotesBean;

	public void setTestingInjectStockQuotesBean(StockQuotesBean stockQuotesBean) {
		this.testingInjectStockQuotesBean = stockQuotesBean;
	}

	private boolean isFromShenzhenMarket(String stockId) {
		boolean result = false;
		if (stockId.indexOf("0") == 0 || stockId.indexOf("3") == 0) {
			result = true;
		}
		return result;
	}

	protected String getCharset() {
		return "gb2312";
	}

	private String getStockQuotesInfoFromInternet(String url)
			throws StockQuotesException {
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		try {
			client.executeMethod(method);
			InputStream in = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					getCharset()));
			String result = br.readLine();
			method.releaseConnection();
			return result;
		} catch (Exception ex) {
			logger.error("access url = " + url + " throw exception: ", ex);
			throw new StockQuotesException(
					"There is an exception while reading quotes from sohu ", ex);

		} finally {
			method.releaseConnection();
		}
	}

	protected abstract String getStockQuotesPageURL(String stockID, boolean isSZ);

	protected abstract StockQuotesBean parseStockQuotes(String stockInfo)
			throws StockQuotesException;


}
