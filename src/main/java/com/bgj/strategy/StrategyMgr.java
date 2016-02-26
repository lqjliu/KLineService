package com.bgj.strategy;

import java.util.Date;
import java.util.List;

import com.bgj.exception.KLineException;

public interface StrategyMgr {
	List<StrategyQueryStockBean> queryStocks(Date date, String strategy) throws KLineException;
}
