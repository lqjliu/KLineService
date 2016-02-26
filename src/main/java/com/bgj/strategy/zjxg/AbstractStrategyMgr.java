package com.bgj.strategy.zjxg;

import java.util.List;

import com.bgj.dao.StockAnalysingAccessor;
import com.bgj.dao.StockAnalysingDataFileAccessor;
import com.bgj.exception.KLineAppException;
import com.bgj.exception.KLineException;
import com.bgj.strategy.CommonStrategyInputBean;
import com.bgj.strategy.CommonStrategyResultBean;
import com.bgj.strategy.StrategyQueryStockBean;
import com.bgj.strategy.StrategyQueryValidator;

public abstract class AbstractStrategyMgr {

	public String getAdditionalInfo(StrategyInputBean inputBean) {
		return inputBean.getAdditionalInfo();
	}

	public CommonStrategyResultBean queryZJXGStrategyStock(
			StrategyInputBean inputBean) throws KLineException {
		StrategyQueryValidator.validate(inputBean);
		validate(inputBean);
		String additionalInfo = this.getAdditionalInfo(inputBean);
		CommonStrategyResultBean result = new CommonStrategyResultBean();
		StockAnalysingAccessor cars = new StockAnalysingDataFileAccessor();
		List<StrategyQueryStockBean> list = cars.query(inputBean.getStrategy(),
				inputBean.getDate(), additionalInfo);

		result.setStrategyQueryStockBeanList(list);
		result.setDate(inputBean.getDate());
		result.setStrategy(inputBean.getStrategy());
		return result;
	}

	public abstract void validate(CommonStrategyInputBean inputBean)
			throws KLineAppException;
}
