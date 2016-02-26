package com.bgj.strategy.zjxg;

public class LXXDInputBean extends StrategyInputBean {
	private int days;

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	private double lowestPrice;

	public double getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public double getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(double highestPrice) {
		this.highestPrice = highestPrice;
	}

	private double highestPrice;

	@Override
	public String getAdditionalInfo() {
		String additionalInfo = getDays() + "-" + getLowestPrice() + "-"
				+ getHighestPrice();
		return additionalInfo;
	}
}
