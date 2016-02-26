package com.bgj.authentication;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class TicketLib {
	private Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();
	private static TicketLib instance = new TicketLib();

	public static TicketLib getInstance() {
		return instance;
	}

	private TicketLib() {
	}

	public void putTicket(String credential, Session session) {
		sessionMap.put(credential, session);
	}

	public Session getTicket(String credential) {
		Object oSession = (Session)sessionMap.get(credential);
		if (oSession != null) {
			return (Session)oSession;
		} else {
			return null;
		}
	}

	public void removeTicket(String credential) {
		sessionMap.remove(credential);
	}
}
