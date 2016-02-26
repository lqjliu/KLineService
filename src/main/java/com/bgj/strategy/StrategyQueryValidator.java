package com.bgj.strategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bgj.dao.Cache;
import com.bgj.exception.AppExceptionKeys;
import com.bgj.exception.KLineAppException;
import com.bgj.util.Constants;
import com.bgj.util.StockMarketUtil;

public class StrategyQueryValidator {
	private static List<String> strategies = new ArrayList<String>();
	static {
		for (int i = 0; i < Constants.STRATEGIES.length; i++) {
			strategies.add(Constants.STRATEGIES[i]);
		}

	}

	public static void validate(CommonStrategyInputBean csib)
			throws KLineAppException {
		Date date = csib.getDate();
		if (date == null) {
			throw new KLineAppException(AppExceptionKeys.DATE_IS_NULL);
		}
		if (csib.getStrategy() == null) {
			throw new KLineAppException(AppExceptionKeys.STRATEGY_IS_NULL);
		}
		if (!strategies.contains(csib.getStrategy())) {
			throw new KLineAppException(AppExceptionKeys.INVALID_STRATEGY);
		}

		String restCause = StockMarketUtil.getMarketRestCause(date);
		if (restCause != null) {
			throw new KLineAppException(restCause);
		}
		if (StockMarketUtil.isComingDay(date)) {
			throw new KLineAppException(AppExceptionKeys.DATE_IS_IN_FUTURE);
		}
		String noDataCauseOfToday = StockMarketUtil.getTodayNoDataCause(date);
		if (noDataCauseOfToday != null) {
			throw new KLineAppException(noDataCauseOfToday);
		}
	}
}
