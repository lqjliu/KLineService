package com.bgj.strategy.zjxg;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.bgj.dao.StockAnalysingAccessor;
import com.bgj.dao.StockAnalysingDataFileAccessor;
import com.bgj.exception.AppExceptionKeys;
import com.bgj.exception.KLineAppException;
import com.bgj.exception.KLineException;
import com.bgj.strategy.CommonStrategyInputBean;
import com.bgj.strategy.CommonStrategyResultBean;
import com.bgj.strategy.StrategyMgr;
import com.bgj.strategy.StrategyQueryStockBean;

public class ZJXGStrategyMgrImpl extends AbstractStrategyMgr implements StrategyMgr {
	private static Logger logger = Logger.getLogger(ZJXGStrategyMgrImpl.class);

	private static ZJXGStrategyMgrImpl instance = new ZJXGStrategyMgrImpl();

	public static ZJXGStrategyMgrImpl getInstance() {
		return instance;
	}

	private ZJXGStrategyMgrImpl() {
	}

	public CommonStrategyResultBean queryZJXGStrategyStock(
			ZJXGInputBean inputBean) throws KLineException {
		return super.queryZJXGStrategyStock(inputBean);
	}

	private void validate(ZJXGInputBean inputBean) throws KLineAppException {
		if (inputBean.getSpanDays() < 0
				|| inputBean.getShortestDaysToLastHighest() < 0
				|| inputBean.getLongestDaysToLastHighest() < 0) {
			throw new KLineAppException(AppExceptionKeys.DAYS_CAN_NOT_MINUS);
		}

		if (inputBean.getSpanDays() != 0
				&& (inputBean.getShortestDaysToLastHighest() != 0
						&& inputBean.getShortestDaysToLastHighest() > inputBean
								.getSpanDays() || inputBean
						.getLongestDaysToLastHighest() != 0
						&& inputBean.getLongestDaysToLastHighest() > inputBean
								.getSpanDays())) {
			throw new KLineAppException(
					AppExceptionKeys.SPAN_DAYS_CAN_NOT_LESS_OTHER_DAYS);
		}

		if (inputBean.getShortestDaysToLastHighest() != 0
				&& inputBean.getLongestDaysToLastHighest() != 0
				&& inputBean.getShortestDaysToLastHighest() > inputBean
						.getLongestDaysToLastHighest()) {
			throw new KLineAppException(
					AppExceptionKeys.LONGEST_DAYS_CAN_NOT_LESS_THAN_SHORTEST_DAYS);
		}

	}

	@Override
	public void validate(CommonStrategyInputBean inputBean)
			throws KLineAppException {
		validate((ZJXGInputBean) inputBean);
	}
	
	public List<StrategyQueryStockBean> queryStocks(Date date, String strategy) throws KLineException{
		String additionalInfo = "40-20-40";
		StockAnalysingAccessor cars = new StockAnalysingDataFileAccessor();
		List<StrategyQueryStockBean> result = cars.query(strategy,
				date, additionalInfo);
		return result;
	}
}
