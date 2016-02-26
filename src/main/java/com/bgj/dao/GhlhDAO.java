package com.bgj.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.bgj.stockquotes.StockQuotesBean;
import com.common.db.DBAgentOO;

public class GhlhDAO {
	private static Logger logger = Logger.getLogger(GhlhDAO.class);

	public static void create(Object object) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		try {
			dbAgentOO.insert(object);
		} catch (Exception ex) {
			logger.error("insert " + object + " throw", ex);
		}
	}

	public static void edit(Object object) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		try {
			dbAgentOO.update(object);
		} catch (Exception ex) {
			logger.error("update " + object + " throw", ex);
		}
	}

	public static Object get(Object object) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		Object result = null;
		try {
			result = dbAgentOO.select(object);
		} catch (Exception ex) {
			logger.error("get " + object + " throw", ex);
		}
		return result;
	}

	public static void remove(Object object) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		try {
			dbAgentOO.delete(object);
		} catch (Exception ex) {
			logger.error("get " + object + " throw", ex);
		}
	}

	public static List list(String sql, String className) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		List result = null;
		try {
			result = dbAgentOO.selectData(sql, className);
		} catch (Exception ex) {
			logger.error("list with SQL = " + sql + " throw", ex);
		}
		return result;
	}

	public static Object get(String sql, String className) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		List result = null;
		try {
			result = dbAgentOO.selectData(sql, className);
		} catch (Exception ex) {
			logger.error("list with SQL = " + sql + " throw", ex);
		}
		if (result.size() != 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public static List list(String sql, String className, int startPos, int size) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		List result = null;
		try {
			result = dbAgentOO.selectData(sql, className, startPos, size);
		} catch (Exception ex) {
			logger.error("list with SQL = " + sql + " throw", ex);
		}
		return result;
	}

	public static List list(String sql) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		List result = null;
		try {
			result = dbAgentOO.selectSingleList(sql);
		} catch (Exception ex) {
			logger.error("list with SQL = " + sql + " throw", ex);
		}
		return result;
	}

	public static String selectSingleValue(String sql) {
		DBAgentOO dbAgentOO = new DBAgentOO();
		String result = null;
		try {
			result = dbAgentOO.selectSingleValue(sql);
		} catch (Exception ex) {
			logger.error("list with SQL = " + sql + " throw", ex);
		}
		return result;
	}

	public static void createStockDailyIinfo(StockQuotesBean sqb, Date date) {
		if (sqb != null) {
			StockdailyinfoVO stockdailyinfoVO = new StockdailyinfoVO();
			stockdailyinfoVO.setStockid(sqb.getStockId());
			stockdailyinfoVO.setDate(date);
			stockdailyinfoVO.setName(sqb.getName());
			stockdailyinfoVO.setTodayopenprice(sqb.getTodayOpen());
			stockdailyinfoVO.setCurrentprice(sqb.getCurrentPrice());
			stockdailyinfoVO.setHighestprice(sqb.getHighestPrice());
			stockdailyinfoVO.setLowestprice(sqb.getLowestPrice());
			stockdailyinfoVO.setYesterdaycloseprice(sqb.getYesterdayClose());
			stockdailyinfoVO.setZde(sqb.getZde());
			stockdailyinfoVO.setZdf(sqb.getZdf());
			stockdailyinfoVO.setHsl(sqb.getHsl());
			stockdailyinfoVO.setCje(sqb.getCje());
			stockdailyinfoVO.setCjl(sqb.getCjl());
			stockdailyinfoVO.setZf(sqb.getZf());
			stockdailyinfoVO.setCreatedtime(new Date());
			stockdailyinfoVO.setLastmodifiedtime(new Date());
			create(stockdailyinfoVO);
		}
	}

}
