package com.bgj.strategy;

import java.util.List;

import org.apache.log4j.Logger;

import com.bgj.exception.KLineException;

public class CommonStrategyFacade {
	private static Logger logger = Logger.getLogger(CommonStrategyFacade.class);

	private static CommonStrategyFacade instance = new CommonStrategyFacade();

	public static CommonStrategyFacade getInstance() {
		return instance;
	}

	private CommonStrategyFacade() {
	}

	public CommonStrategyResultBean queryCommonStrategyStock(
			CommonStrategyInputBean commonStrategyInputBean)
			throws KLineException {
		StrategyQueryValidator.validate(commonStrategyInputBean);
		CommonStrategyResultBean result = new CommonStrategyResultBean();
		StrategyMgr stategyMrg = StrategyMgrFactory.getInstance()
				.getCommonStrategyMgr();
		List<StrategyQueryStockBean> list = stategyMrg.queryStocks(
				commonStrategyInputBean.getDate(),
				commonStrategyInputBean.getStrategy());
		result.setStrategyQueryStockBeanList(list);
		result.setDate(commonStrategyInputBean.getDate());
		result.setStrategy(commonStrategyInputBean.getStrategy());
		return result;
	}
}
