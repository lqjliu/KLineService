package com.bgj.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.bgj.stockquotes.IndustryQuotesBeans;
import com.bgj.stockquotes.StockQuotesBean;

public class SinaUtil {
	public static Logger logger = Logger.getLogger(SinaUtil.class);

	public static StockQuotesBean parseStockQuotes(String stockInfo) {
		StockQuotesBean result = null;
		try {
			result = new StockQuotesBean();
			Pattern pattern = Pattern.compile(",");
			String[] stockInfoPieces = pattern.split(stockInfo);
			if (stockInfoPieces.length == 0 || stockInfoPieces.length == 1) {
				return null;
			}
			result.setStockId(stockInfoPieces[1]);
			result.setName(stockInfoPieces[2]);
			result.setYesterdayClose(Double.parseDouble(stockInfoPieces[3]));
			result.setTodayOpen(Double.parseDouble(stockInfoPieces[4]));
			result.setCurrentPrice(Double.parseDouble(stockInfoPieces[5]));
			result.setHighestPrice(Double.parseDouble(stockInfoPieces[6]));
			result.setLowestPrice(Double.parseDouble(stockInfoPieces[7]));
			result.setCje(Double.parseDouble(stockInfoPieces[8]));
			result.setCjl(Double.parseDouble(stockInfoPieces[9]));

			result.setZde(Double.parseDouble(stockInfoPieces[10]));
			String sZdf = stockInfoPieces[11].substring(0,
					stockInfoPieces[11].length() - 1);
			result.setZdf(Double.parseDouble(sZdf));

			String sZf = stockInfoPieces[13].substring(0,
					stockInfoPieces[13].length() - 1);
			result.setZf(Double.parseDouble(sZf));

			String sHsl = stockInfoPieces[23].substring(0,
					stockInfoPieces[23].length() - 1);
			result.setHsl(Double.parseDouble(sHsl));
		} catch (Exception ex) {
			logger.error("Parse Stock throw", ex);
		}
		return result;
	}

	public static List<StockQuotesBean> collectData(int pageSize, int marketCode) {
		List<StockQuotesBean> result = new ArrayList<StockQuotesBean>();
		String url = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?"
				+ "type=s&sortType=C&sortRule=-1&pageSize="
				+ pageSize
				+ "&page=1&jsName=quote_123"
				+ "&style="
				+ marketCode
				+ "&_g=0.5124112195048514";
		System.out.println(" url = " + url);
		String content = HttpUtil.accessInternet(url);
		content = content.substring(content.indexOf("[") + 1);
		content = content.substring(0, content.indexOf("]"));
		while (content.indexOf("\",\"") > 0) {
			content = content.substring(1);
			String stockInfo = content.substring(0, content.indexOf("\",\""));
			content = content.substring(content.indexOf("\",\"") + 2);
			StockQuotesBean sqb = SinaUtil.parseStockQuotes(stockInfo);
			result.add(sqb);
		}
		content = content.substring(1);
		StockQuotesBean sqb = SinaUtil.parseStockQuotes(content);
		result.add(sqb);
		return result;
	}

	public static List<IndustryQuotesBeans> collectIndustryData() {
		List<IndustryQuotesBeans> result = new ArrayList<IndustryQuotesBeans>();
		String url = "http://hq2data.eastmoney.com/bk/data/notion.js";
		System.out.println(" url = " + url);
		String content = HttpUtil.accessInternet(url);
		content = content.substring(content.indexOf("[[") + 2);
		content = content.substring(0, content.indexOf("]]"));
		Pattern pattern = Pattern.compile("\",\"");
		String[] industryInfo = pattern.split(content);
		for (int i = 0; i < industryInfo.length; i++) {
			System.out.println(industryInfo[i]);
		}
		return result;
	}

	public static void main(String[] args) {
/*		collectData(200, 20);
		collectData(200, 10);
*/	
		collectIndustryData();
	}

}
