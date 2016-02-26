package com.bgj.analysis;

public class MaBean {
	private double ma5;

	public double getMa5() {
		return ma5;
	}

	public void setMa5(double ma5) {
		this.ma5 = ma5;
	}

	public double getMa10() {
		return ma10;
	}

	public void setMa10(double ma10) {
		this.ma10 = ma10;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	private double ma10;
	private double currentPrice;

	private String stockId;

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	private boolean noEnoughData;

	public boolean isNoEnoughData() {
		return noEnoughData;
	}

	public void setNoEnoughData(boolean noEnoughData) {
		this.noEnoughData = noEnoughData;
	}

	public String toString() {
		if (this.noEnoughData) {
			return "StockId: " + stockId + " : No Enough Data to calculate MA";
		} else {
			return "StockId: " + stockId + " CurrentPrice:" + currentPrice
					+ " MA5:" + ma5 + " MA10:" + ma10;
		}
	}
}
