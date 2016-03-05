package com.common.wechat;

import java.io.File;
import java.util.Date;

import javax.net.ssl.SSLContext;

import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;

public class GpphbConfig implements WxMpConfigStorage{

	private static final String APP_ID = "wxe7870f8ea319d599";
	private static final String APP_SECRET = "bfb0ea07cee591ee4b79e9804accaaf9";
	private static final String TOKEN = "M2E0M2IzYzItODNiMi00YTc0LTliZDE";
	
	private String accessToken;
	private int expiresIn;
	private Date tokenInitTime = null;
	
	@Override
	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public boolean isAccessTokenExpired() {
		if(tokenInitTime == null){
			return true;
		}else{
			Date tokenExpireTime = new Date(tokenInitTime.getTime() + expiresIn * 60 * 1000);
			return !tokenExpireTime.after(new Date());
		}
	}

	@Override
	public void expireAccessToken() {
		this.tokenInitTime = null;
	}

	@Override
	public void updateAccessToken(WxAccessToken accessToken) {
		this.updateAccessToken(accessToken.getAccessToken(),accessToken.getExpiresIn());
	}

	@Override
	public void updateAccessToken(String accessToken, int expiresIn) {
		this.tokenInitTime = new Date();
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
	}

	@Override
	public String getJsapiTicket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isJsapiTicketExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void expireJsapiTicket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAppId() {
		return APP_ID;
	}

	@Override
	public String getSecret() {
		return APP_SECRET;
	}

	@Override
	public String getPartnerId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPartnerKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToken() {
		return TOKEN;
	}

	@Override
	public String getAesKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getExpiresTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getOauth2redirectUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHttp_proxy_host() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHttp_proxy_port() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHttp_proxy_username() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHttp_proxy_password() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getTmpDirFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SSLContext getSSLContext() {
		// TODO Auto-generated method stub
		return null;
	}

}
