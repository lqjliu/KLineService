package com.bgj.strategy.zjxg;

import org.apache.log4j.Logger;

import com.bgj.exception.AppExceptionKeys;
import com.bgj.exception.KLineAppException;
import com.bgj.exception.KLineException;
import com.bgj.strategy.CommonStrategyInputBean;
import com.bgj.strategy.CommonStrategyResultBean;

public class LXXDStrategyMgrImpl extends AbstractStrategyMgr {
	private static Logger logger = Logger.getLogger(LXXDStrategyMgrImpl.class);

	private static LXXDStrategyMgrImpl instance = new LXXDStrategyMgrImpl();

	public static LXXDStrategyMgrImpl getInstance() {
		return instance;
	}

	private LXXDStrategyMgrImpl() {
	}

	public CommonStrategyResultBean queryZJXGStrategyStock(
			LXXDInputBean inputBean) throws KLineException {
		return super.queryZJXGStrategyStock(inputBean);
	}

	private void validate(LXXDInputBean inputBean) throws KLineAppException {
		if (inputBean.getDays() <= 0) {
			throw new KLineAppException(AppExceptionKeys.DAYS_CAN_NOT_MINUS);
		}

		if (inputBean.getLowestPrice() < 0 || inputBean.getHighestPrice() < 0) {
			throw new KLineAppException(AppExceptionKeys.PRICE_CAN_NOT_MINUS);
		}

		if (inputBean.getLowestPrice() > inputBean.getHighestPrice()) {
			throw new KLineAppException(
					AppExceptionKeys.HIGHEST_PRICE_CAN_NOT_LESS_THAN_LOWEST_PRICE);
		}

	}

	@Override
	public void validate(CommonStrategyInputBean inputBean)
			throws KLineAppException {
		validate((LXXDInputBean) inputBean);
	}
}
