package com.bgj.strategy;

import java.util.Date;
import java.util.List;

public class CommonStrategyResultBean {
	private Date date;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	private String strategy;
	

	private List<StrategyQueryStockBean> strategyQueryStockBeanList;
	public List<StrategyQueryStockBean> getStrategyQueryStockBeanList() {
		return strategyQueryStockBeanList;
	}
	public void setStrategyQueryStockBeanList(
			List<StrategyQueryStockBean> strategyQueryStockBeanList) {
		this.strategyQueryStockBeanList = strategyQueryStockBeanList;
	}
}
