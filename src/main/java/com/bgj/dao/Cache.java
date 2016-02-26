package com.bgj.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bgj.strategy.StrategyQueryStockBean;
import com.bgj.util.Constants;
import com.bgj.util.DateUtil;

public class Cache {
	private static Map<String, Cache> cacheMap = new HashMap<String, Cache>();
	static {
		for (int i = 0; i < Constants.STRATEGIES.length; i++) {
			Cache cache = new Cache();
			cacheMap.put(Constants.STRATEGIES[i], cache);
		}
	}

	private static Cache instance = new Cache();
	private Map<String, List<StrategyQueryStockBean>> stockMap = null;

	public static Cache getInstance(String strategy) {
		return cacheMap.get(strategy);
	}

	private Cache() {
		stockMap = new HashMap<String, List<StrategyQueryStockBean>>();
	}

	public void putStockIntoCache(Date date, String additionalInfo,
			List<StrategyQueryStockBean> list) {
		String key = DateUtil.formatDay(date);
		if (additionalInfo != null && !additionalInfo.equals("")) {
			key += additionalInfo;
		}
		synchronized (this) {
			stockMap.put(key, list);
		}
	}

	public List<StrategyQueryStockBean> getStockFromCache(Date date,
			String additionalInfo) {
		String key = DateUtil.formatDay(date);
		if (additionalInfo != null && !additionalInfo.equals("")) {
			key += additionalInfo;
		}
		return stockMap.get(key);
	}

}
