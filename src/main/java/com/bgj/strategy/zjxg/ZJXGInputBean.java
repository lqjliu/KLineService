package com.bgj.strategy.zjxg;

public class ZJXGInputBean extends StrategyInputBean {
	private int longestDaysToLastHighest;

	public int getLongestDaysToLastHighest() {
		return longestDaysToLastHighest;
	}

	public void setLongestDaysToLastHighest(int longestDaysToLastHighest) {
		this.longestDaysToLastHighest = longestDaysToLastHighest;
	}

	public int getShortestDaysToLastHighest() {
		return shortestDaysToLastHighest;
	}

	public void setShortestDaysToLastHighest(int shortestDaysToLastHighest) {
		this.shortestDaysToLastHighest = shortestDaysToLastHighest;
	}

	public int getSpanDays() {
		return spanDays;
	}

	public void setSpanDays(int spanDays) {
		this.spanDays = spanDays;
	}

	private int shortestDaysToLastHighest;
	private int spanDays;

	@Override
	public String getAdditionalInfo() {
		String additionalInfo = getSpanDays() + "-"
				+ getShortestDaysToLastHighest() + "-"
				+ getLongestDaysToLastHighest();
		return additionalInfo;
	}
}
