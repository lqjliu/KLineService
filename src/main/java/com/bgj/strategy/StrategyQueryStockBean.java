package com.bgj.strategy;

import com.bgj.util.MathUtil;

public class StrategyQueryStockBean implements Comparable<StrategyQueryStockBean>{
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

	public double getKpj() {
		return kpj;
	}

	public void setKpj(double kpj) {
		this.kpj = kpj;
	}

	public double getDqj() {
		return dqj;
	}

	public void setDqj(double dqj) {
		this.dqj = dqj;
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

	public double getZf() {
		return zf;
	}

	public void setZf(double zf) {
		this.zf = zf;
	}

	public double getHsl() {
		return hsl;
	}

	public void setHsl(double hsl) {
		this.hsl = hsl;
	}

	private String name;
	private double kpj;
	private double dqj;
	private double zdf;
	private double zde;
	private double zf;
	private double hsl;

	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String toString() {
		return "StockId = " + stockId + " Name = " + name + " Kpj = " + kpj
				+ " Dqj = " + dqj + " Zdf = " + zdf + " Zde = " + zde
				+ " Zf = " + zf + " Hsl = " + hsl + " LatestSpj = " + latestSpj;

	}

	private double latestSpj;

	public double getLatestSpj() {
		return latestSpj;
	}

	public void setLatestSpj(double latestSpj) {
		this.latestSpj = latestSpj;
	}

	public double getLeijiPercentage() {
		double leijiPercentage = MathUtil
				.formatDoubleWith2((getLatestSpj() - getDqj()) / getDqj() * 100);
		return leijiPercentage;
	}

	@Override
	public int compareTo(StrategyQueryStockBean o) {
		if (this.getLeijiPercentage() > o.getLeijiPercentage()) {
			return 1;
		} else if (this.getLeijiPercentage() == o.getLeijiPercentage()) {
			return 0;
		} else {
			return -1;
		}
	}	
	
}
