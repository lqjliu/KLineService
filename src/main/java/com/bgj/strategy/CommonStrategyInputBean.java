package com.bgj.strategy;

import java.util.Date;

public class CommonStrategyInputBean {
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
	
}
