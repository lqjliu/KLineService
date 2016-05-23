package com.common.wechat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebChatSession {
	private static Map<String, String> sessions = new ConcurrentHashMap<String, String>();
	private static WebChatSession instance = new WebChatSession();

	public static WebChatSession getInstance() {
		return instance;
	}

	private WebChatSession() {

	}

	public void putSession(String wechatID, String currentCMD) {
		sessions.put(wechatID, currentCMD);
	}

	public String getSession(String wechatID) {
		return (String) sessions.get(wechatID);
	}

}
