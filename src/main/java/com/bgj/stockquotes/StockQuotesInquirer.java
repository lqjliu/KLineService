package com.bgj.stockquotes;

public interface StockQuotesInquirer {
	StockQuotesBean getStockQuotesBean(String stockId);
	void setTestingInjectStockQuotesBean(StockQuotesBean stockQuotesBean);
	
}
