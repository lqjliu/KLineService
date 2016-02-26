package com.bgj.stockquotes;

public class StockQuotesBean {
	private String stockId;

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public double getZdf() {
		return zdf;
	}

	public void setZdf(double zdf) {
		this.zdf = zdf;
	}

	public double getZde() {
		return zde;
	}

	public void setZde(double zde) {
		this.zde = zde;
	}

	private String name;
	private double currentPrice;
	private double zdf;
	private double zde;

	private double todayOpen;

	public double getTodayOpen() {
		return todayOpen;
	}

	public void setTodayOpen(double todayOpen) {
		this.todayOpen = todayOpen;
	}

	public double getYesterdayClose() {
		return yesterdayClose;
	}

	public void setYesterdayClose(double yesterdayClose) {
		this.yesterdayClose = yesterdayClose;
	}

	public double getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(double highestPrice) {
		this.highestPrice = highestPrice;
	}

	public double getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(double lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	private double yesterdayClose;
	private double highestPrice;
	private double lowestPrice;

	public String toString() {
		return "StockId = " + stockId + " Name = " + name + " CurrentPrice = "
				+ currentPrice + " Zdf = " + zdf + " Zde = " + zde
				+ " TodayOpen = " + todayOpen + " YesterdayClose = "
				+ yesterdayClose + " HighestPrice = " + highestPrice
				+ " LowestPrice = " + lowestPrice
				+ " Hsl = " + hsl;

	}
	
	private double hsl;

	public double getHsl() {
		return hsl;
	}

	public void setHsl(double hsl) {
		this.hsl = hsl;
	}
	
	private double cje;
	public double getCje() {
		return cje;
	}

	public void setCje(double cje) {
		this.cje = cje;
	}

	public double getCjl() {
		return cjl;
	}

	public void setCjl(double cjl) {
		this.cjl = cjl;
	}

	private double cjl;
	
	private double zf;

	public double getZf() {
		return zf;
	}

	public void setZf(double zf) {
		this.zf = zf;
	}
	
}