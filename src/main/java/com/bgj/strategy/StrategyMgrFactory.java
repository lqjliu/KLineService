package com.bgj.strategy;


public class StrategyMgrFactory {
	private static StrategyMgrFactory instance = new StrategyMgrFactory();

	public static StrategyMgrFactory getInstance() {
		return instance;
	}

	private StrategyMgrFactory() {

	}

	public StrategyMgr getCommonStrategyMgr() {
		return new CommonStrategyMgrImpl();
	}
}
