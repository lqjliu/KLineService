package com.bgj.stockquotes;

public class IndustryQuotesBeans {
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getZdf() {
		return zdf;
	}
	public void setZdf(double zdf) {
		this.zdf = zdf;
	}
	public double getZsz() {
		return zsz;
	}
	public void setZsz(double zsz) {
		this.zsz = zsz;
	}
	public double getHsl() {
		return hsl;
	}
	public void setHsl(double hsl) {
		this.hsl = hsl;
	}
	public double getSzjs() {
		return szjs;
	}
	public void setSzjs(double szjs) {
		this.szjs = szjs;
	}
	public double getXdjs() {
		return xdjs;
	}
	public void setXdjs(double xdjs) {
		this.xdjs = xdjs;
	}
	public String getLzgpid() {
		return lzgpid;
	}
	public void setLzgpid(String lzgpid) {
		this.lzgpid = lzgpid;
	}
	public String getLzgp() {
		return lzgp;
	}
	public void setLzgp(String lzgp) {
		this.lzgp = lzgp;
	}
	public double getLzgpzdf() {
		return lzgpzdf;
	}
	public void setLzgpzdf(double lzgpzdf) {
		this.lzgpzdf = lzgpzdf;
	}
	private double zdf;
	private double zsz;
	private double hsl;
	private double szjs;
	private double xdjs;
	private String lzgpid;
	private String lzgp;
	private double lzgpzdf;
	
	public String toString(){
		return "name = " + name + " zdf = " + zdf;
	}
}
