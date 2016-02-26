package com.bgj.dao;

import java.util.Date;
import java.util.List;

import com.bgj.exception.KLineException;
import com.bgj.strategy.StrategyQueryStockBean;

public class StockAnalysingDBAccessor implements
		StockAnalysingAccessor {

	public void save(List<StockdailyinfoVO> list, String strategyName, Date date)
			throws KLineException {
		// TODO Auto-generated method stub

	}

	public List<StrategyQueryStockBean> query(String strategyName,
			Date date) throws KLineException {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(List<StockdailyinfoVO> list, String strategyName,
			Date date, String additionalInfo) throws KLineException {
		// TODO Auto-generated method stub
		
	}

	public List<StrategyQueryStockBean> query(String strategyName, Date date,
			String additionalInfo) throws KLineException {
		// TODO Auto-generated method stub
		return null;
	}

}
