package com.common.web;

public class RequestContext {
	private static ThreadLocal<Token> requestContext = new ThreadLocal<Token>();
	public static void putToken(Token token){
		requestContext.set(token);
	}
	public static Token getToken(){
		return (Token)requestContext.get();
	}
	public static void cleanToken(){
		requestContext.set(null);
	}
	
}
