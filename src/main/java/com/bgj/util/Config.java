package com.bgj.util;

public class Config {
	private Config() {
	}

	private static Config instance = new Config();
	public static Config getInstance(){
		return instance;
	}
	private boolean devIntegration = true;
	
	public boolean isDevIntegration(){
		return devIntegration;
	}
}
