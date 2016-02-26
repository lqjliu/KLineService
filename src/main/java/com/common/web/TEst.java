package com.common.web;

import java.lang.reflect.Method;

import com.bgj.strategy.CommonStrategyInputBean;
import com.bgj.util.DateUtil;

public class TEst {

	public TEst() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {
			Class mgr = null;
			Method getInstance = null;
			String inputClass = null;
			System.out.println("BCD");

			mgr = Class.forName("com.bgj.strategy.CommonStrategyFacade");
			getInstance = mgr.getMethod("getInstance", null);

			Method jobMethod = mgr.getMethod("queryCommonStrategyStock",
					new Class[] { CommonStrategyInputBean.class });
			Object mgrImpl = getInstance.invoke(null, null);
			CommonStrategyInputBean userRegisterBean = new CommonStrategyInputBean();
			userRegisterBean.setDate(DateUtil.getDate(2014, 8, 13));
			userRegisterBean.setStrategy("ZTB");

			Object o = jobMethod.invoke(mgrImpl,
					new Object[] { userRegisterBean });
			System.out.println("o = " + o);
			System.out.println(getInstance.getName());
			System.out.println("mgrImpl = " + mgrImpl);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
