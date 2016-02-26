package com.bgj.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import org.apache.log4j.Logger;

public class HttpUtil {
	public static Logger logger = Logger.getLogger(HttpUtil.class);

	public final static int RE_TRY_COUNT = 5;

	public static String accessInternet(String stockQuotesURL) {
		String line = null;
		for (int i = 0; i < RE_TRY_COUNT; i++) {
			try {
				line = accessInternetWithThrowsException(stockQuotesURL);
				break;
			} catch (SocketException sex) {
				logger.error("Quote stock info from " + stockQuotesURL
						+ " throw SocketException, Retry for " + (i + 1)
						+ " times");
				pause();
			} catch (Exception ex) {
				logger.error("Quote stock info from " + stockQuotesURL
						+ " throw : ", ex);
				logger.error("Retry for " + (i + 1) + " times");
				pause();
			}
		}
		return line;
	}

	public static void pause() {
		try {
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String accessInternetWithThrowsException(
			String stockQuotesURL) throws Exception {
		String line = null;
		HttpMethod method = new GetMethod(stockQuotesURL);
		HttpClient client = new HttpClient();
		try {
			client.executeMethod(method);
			InputStream in = method.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					"utf-8"));
			line = br.readLine();
			method.releaseConnection();
		} finally {
			method.releaseConnection();
		}
		return line;
	}

	public static String accessInternetForMultiple(String stockQuotesURL) {
		String result = null;
		HttpMethod method = new GetMethod(stockQuotesURL);
		HttpClient client = new HttpClient();
		DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy",
				CookiePolicy.BROWSER_COMPATIBILITY);
		try {
			client.executeMethod(method);
			InputStream in = method.getResponseBodyAsStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					"utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (result == null) {
					result = line;
				} else {
					result = result + " " + line;
				}
			}
			method.releaseConnection();
		} catch (Exception ex) {
			logger.error("Quote stock info from " + stockQuotesURL
					+ " throw : ", ex);
		} finally {
			method.releaseConnection();
		}
		return result;
	}
}
