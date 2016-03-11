package com.common.wechat;

public class StrategyBean {
	public StrategyBean(String abbre, String mgrClass, String name) {
		this.abbre = abbre;
		this.name = name;
		this.mgrClass = mgrClass;
	}

	private String abbre;

	public String getAbbre() {
		return abbre;
	}

	public void setAbbre(String abbre) {
		this.abbre = abbre;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMgrClass() {
		return mgrClass;
	}

	public void setMgrClass(String mgrClass) {
		this.mgrClass = mgrClass;
	}

	private String name;
	private String mgrClass;

	public String toString() {
		return abbre + " " + name + " " + mgrClass;
	}
}
