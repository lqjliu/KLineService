package com.bgj.dao;

import java.util.Date;
import java.util.List;

import com.bgj.exception.KLineException;
import com.bgj.strategy.StrategyQueryStockBean;

public interface StockAnalysingAccessor {
	public void save(List<StockdailyinfoVO> list, String strategyName, Date date)
			throws KLineException;

	public void save(List<StockdailyinfoVO> list, String strategyName,
			Date date, String additionalInfo) throws KLineException;

	public List<StrategyQueryStockBean> query(String strategyName, Date date)
			throws KLineException;

	public List<StrategyQueryStockBean> query(String strategyName, Date date,
			String additionalInfo) throws KLineException;
}
