package com.common.wechat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bgj.strategy.StrategyMgr;

public class StrategyConfiguration {
	private static Logger logger = Logger.getLogger(GpphbServlet.class);

	private Map<String, StrategyBean> strates = new HashMap<String, StrategyBean>();
	private static StrategyConfiguration beanConfigration = new StrategyConfiguration();

	public static StrategyConfiguration getInstance() {
		return beanConfigration;
	}

	private StrategyConfiguration() {
		InputStream is = StrategyConfiguration.class
				.getResourceAsStream("strategies.properties");
		String line = null;
		if (is != null) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "UTF-8"));
				while ((line = br.readLine()) != null
						&& !line.trim().equals("")) {
					String[] info = line.split(" ");
					StrategyBean bean = new StrategyBean(info[0], info[1],
							info[2]);
					strates.put(info[0], bean);
				}
				br.close();
				is.close();
			} catch (Exception e) {
				logger.error("Read stragegies file: strategies.properties,"
						+ " throw : ", e);
			}
		}
	}

	public StrategyBean getStrategyBean(String abbre) {
		return strates.get(abbre);
	}

	public boolean isSupportStrategy(String cmd) {
		Set keys = strates.keySet();
		Iterator iKeys = keys.iterator();
		while (iKeys.hasNext()) {
			String abbre = (String) iKeys.next();
			if (cmd.trim().toUpperCase().indexOf(abbre) == 0) {
				return true;
			}
		}
		return false;

	}

	public String getSupportStrategyAbbre(String cmd) {
		Set keys = strates.keySet();
		Iterator iKeys = keys.iterator();
		while (iKeys.hasNext()) {
			String abbre = (String) iKeys.next();
			if (cmd.trim().toUpperCase().indexOf(abbre) == 0) {
				return abbre;
			}
		}
		return null;
	}

	public StrategyMgr getStrategyMgr(String strategy) {
		String mgrClassName = ((StrategyBean) strates.get(strategy))
				.getMgrClass();
		StrategyMgr result = null;
		try {
			Class mgr = Class.forName(mgrClassName);
			Method getInstance = mgr.getMethod("getInstance", null);
			result = (StrategyMgr) getInstance.invoke(null, null);
		} catch (Exception e) {
			logger.error("Get Strategy Mgr throw : ", e);
		}
		return result;
	}

	public String getStrategyDes() {
		Set keys = strates.keySet();
		Iterator iKeys = keys.iterator();
		String result = "";
		while (iKeys.hasNext()) {
			String abbre = (String) iKeys.next();
			String name = strates.get(abbre).getName();
			result += (name + ":" + abbre + "\n");
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(StrategyConfiguration.getInstance()
				.isSupportStrategy("MRZT 2015-01-01"));
		System.out.println(StrategyConfiguration.getInstance()
				.getSupportStrategyAbbre("MRZT 2015-01-01"));
		System.out.println(StrategyConfiguration.getInstance().getStrategyBean(
				"MRZT"));
	}
}
