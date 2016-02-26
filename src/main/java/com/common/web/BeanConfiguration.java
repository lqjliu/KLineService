package com.common.web;

import java.util.HashMap;
import java.util.Map;

public class BeanConfiguration {
	private Map beanMap = new HashMap();
	private static BeanConfiguration beanConfigration = new BeanConfiguration();

	public static BeanConfiguration getInstance() {
		return beanConfigration;
	}

	private BeanConfiguration() {
		beanMap.put("QueryCommonStrategyStock",
				"com.bgj.strategy.CommonStrategyInputBean");
		beanMap.put("QueryCommonStrategyStock.mgr",
				"com.bgj.strategy.CommonStrategyFacade");
		beanMap.put("QueryCommonStrategyStock.api", "queryCommonStrategyStock");
		beanMap.put("QueryCommonStrategyStock.authentication", "false");

		beanMap.put("LoginThroughWeChat", "com.bgj.authentication.LoginBean");
		beanMap.put("LoginThroughWeChat.mgr",
				"com.bgj.authentication.LoginMgrImpl");
		beanMap.put("LoginThroughWeChat.api", "loginWithWeChat");
		beanMap.put("LoginThroughWeChat.authentication", "false");

		beanMap.put("QueryCommonStrategyStockNeedAuthentication",
				"com.bgj.strategy.CommonStrategyInputBean");
		beanMap.put("QueryCommonStrategyStockNeedAuthentication.mgr",
				"com.bgj.strategy.CommonStrategyFacade");
		beanMap.put("QueryCommonStrategyStockNeedAuthentication.api",
				"queryCommonStrategyStock");
		beanMap.put(
				"QueryCommonStrategyStockNeedAuthentication.authentication",
				"true");

		beanMap.put("QueryZJXGStrategyStock",
				"com.bgj.strategy.zjxg.ZJXGInputBean");
		beanMap.put("QueryZJXGStrategyStock.mgr",
				"com.bgj.strategy.zjxg.ZJXGStrategyMgrImpl");
		beanMap.put("QueryZJXGStrategyStock.api", "queryZJXGStrategyStock");
		beanMap.put("QueryZJXGStrategyStock.authentication", "true");


		beanMap.put("QueryLXXDStrategyStock",
				"com.bgj.strategy.zjxg.LXXDInputBean");
		beanMap.put("QueryLXXDStrategyStock.mgr",
				"com.bgj.strategy.zjxg.LXXDStrategyMgrImpl");
		beanMap.put("QueryLXXDStrategyStock.api", "queryZJXGStrategyStock");
		beanMap.put("QueryLXXDStrategyStock.authentication", "true");

		
		
		beanMap.put("CheckSymbolDuplicated", "java.lang.String");
		beanMap.put("CheckSymbolDuplicated.mgr",
				"com.carucrm.biz.register.RegisterMgrImpl");
		beanMap.put("CheckSymbolDuplicated.api", "isSymbolDuplicated");
		beanMap.put("CheckSymbolDuplicated.authentication", "false");

		beanMap.put("CheckContactMobileDuplicated", "java.lang.String");
		beanMap.put("CheckContactMobileDuplicated.mgr",
				"com.carucrm.biz.register.RegisterMgrImpl");
		beanMap.put("CheckContactMobileDuplicated.api",
				"isContactMobileDuplicated");
		beanMap.put("CheckContactMobileDuplicated.authentication", "false");

		beanMap.put("CheckAccountNameDuplicated", "java.lang.String");
		beanMap.put("CheckAccountNameDuplicated.mgr",
				"com.carucrm.biz.register.RegisterMgrImpl");
		beanMap.put("CheckAccountNameDuplicated.api", "isAccountNameDuplicated");
		beanMap.put("CheckAccountNameDuplicated.authentication", "false");

		beanMap.put("CheckAccountEmailDuplicated", "java.lang.String");
		beanMap.put("CheckAccountEmailDuplicated.mgr",
				"com.carucrm.biz.register.RegisterMgrImpl");
		beanMap.put("CheckAccountEmailDuplicated.api",
				"isAccountEmailDuplicated");
		beanMap.put("CheckAccountEmailDuplicated.authentication", "false");

		beanMap.put("CheckAccountContactMobileDuplicated", "java.lang.String");
		beanMap.put("CheckAccountContactMobileDuplicated.mgr",
				"com.carucrm.biz.register.RegisterMgrImpl");
		beanMap.put("CheckAccountContactMobileDuplicated.api",
				"isAccountContactMobileDuplicated");
		beanMap.put("CheckAccountContactMobileDuplicated.authentication",
				"false");

		beanMap.put("GetCorporationRegisteration.mgr",
				"com.carucrm.biz.register.RegisterMgrImpl");
		beanMap.put("GetCorporationRegisteration.api",
				"getCorporationRegisteration");
		beanMap.put("GetCorporationRegisteration.authentication", "true");

		beanMap.put("UpdateCorporationRegisteration",
				"com.carucrm.biz.register.CorporationRegisterBean");
		beanMap.put("UpdateCorporationRegisteration.mgr",
				"com.carucrm.biz.register.RegisterMgrImpl");
		beanMap.put("UpdateCorporationRegisteration.api",
				"updateCorporationRegisteration");
		beanMap.put("UpdateCorporationRegisteration.authentication", "true");

		beanMap.put("ChangePassword",
				"com.carucrm.biz.account.ChangePasswordBean");
		beanMap.put("ChangePassword.mgr",
				"com.carucrm.biz.account.AccountMgrImpl");
		beanMap.put("ChangePassword.api", "changePassword");
		beanMap.put("ChangePassword.authentication", "true");

		beanMap.put("AddCustomer", "com.carucrm.biz.customer.CustomerBean");
		beanMap.put("AddCustomer.mgr",
				"com.carucrm.biz.customer.CustomerMgrImpl");
		beanMap.put("AddCustomer.api", "addCustomer");
		beanMap.put("AddCustomer.authentication", "true");

		beanMap.put("CheckCustomerMobileDuplicated", "java.lang.String");
		beanMap.put("CheckCustomerMobileDuplicated.mgr",
				"com.carucrm.biz.customer.CustomerMgrImpl");
		beanMap.put("CheckCustomerMobileDuplicated.api", "isMobileDuplicated");
		beanMap.put("CheckCustomerMobileDuplicated.authentication", "true");

		//
		beanMap.put("EditCustomer", "com.carucrm.biz.customer.CustomerBean");
		beanMap.put("EditCustomer.mgr",
				"com.carucrm.biz.customer.CustomerMgrImpl");
		beanMap.put("EditCustomer.api", "editCustomer");
		beanMap.put("EditCustomer.authentication", "true");

		beanMap.put("GetCustomer", "com.carucrm.biz.customer.CustomerIDBean");
		beanMap.put("GetCustomer.mgr",
				"com.carucrm.biz.customer.CustomerMgrImpl");
		beanMap.put("GetCustomer.api", "getCustomer");
		beanMap.put("GetCustomer.authentication", "true");

		beanMap.put("DeleteCustomer", "com.carucrm.biz.customer.CustomerIDBean");
		beanMap.put("DeleteCustomer.mgr",
				"com.carucrm.biz.customer.CustomerMgrImpl");
		beanMap.put("DeleteCustomer.api", "deleteCustomer");
		beanMap.put("DeleteCustomer.authentication", "true");

		beanMap.put("SuggestCustomer", "java.lang.String");
		beanMap.put("SuggestCustomer.mgr",
				"com.carucrm.biz.customer.CustomerMgrImpl");
		beanMap.put("SuggestCustomer.api", "suggestCustomer");
		beanMap.put("SuggestCustomer.authentication", "true");

		beanMap.put("GetCertainCustomer",
				"com.carucrm.biz.customer.CustomerIDBean");
		beanMap.put("GetCertainCustomer.mgr",
				"com.carucrm.biz.customer.CustomerMgrImpl");
		beanMap.put("GetCertainCustomer.api", "getCertainCustomer");
		beanMap.put("GetCertainCustomer.authentication", "true");

		beanMap.put("GetLatestCustomer",
				"com.carucrm.biz.customer.LatestCustomerInputBean");
		beanMap.put("GetLatestCustomer.mgr",
				"com.carucrm.biz.customer.CustomerMgrImpl");
		beanMap.put("GetLatestCustomer.api", "getLatestCustomer");
		beanMap.put("GetLatestCustomer.authentication", "true");

		beanMap.put("SearchCustomer",
				"com.carucrm.biz.customer.SearchCustomerInputBean");
		beanMap.put("SearchCustomer.mgr",
				"com.carucrm.biz.customer.CustomerMgrImpl");
		beanMap.put("SearchCustomer.api", "searchCustomer");
		beanMap.put("SearchCustomer.authentication", "true");

		beanMap.put("AddConsume", "com.carucrm.biz.consume.ConsumeBean");
		beanMap.put("AddConsume.mgr", "com.carucrm.biz.consume.ConsumeMgrImpl");
		beanMap.put("AddConsume.api", "addConsume");
		beanMap.put("AddConsume.authentication", "true");

		beanMap.put("EditConsume", "com.carucrm.biz.consume.ConsumeBean");
		beanMap.put("EditConsume.mgr", "com.carucrm.biz.consume.ConsumeMgrImpl");
		beanMap.put("EditConsume.api", "editConsume");
		beanMap.put("EditConsume.authentication", "true");

		beanMap.put("GetConsume", "com.carucrm.biz.consume.ConsumeIDBean");
		beanMap.put("GetConsume.mgr", "com.carucrm.biz.consume.ConsumeMgrImpl");
		beanMap.put("GetConsume.api", "getConsume");
		beanMap.put("GetConsume.authentication", "true");

		beanMap.put("DeleteConsume", "com.carucrm.biz.consume.ConsumeIDBean");
		beanMap.put("DeleteConsume.mgr",
				"com.carucrm.biz.consume.ConsumeMgrImpl");
		beanMap.put("DeleteConsume.api", "deleteConsume");
		beanMap.put("DeleteConsume.authentication", "true");

		beanMap.put("Consumeitems", "com.carucrm.biz.consume.ConsumeitemBean");

		beanMap.put("ListConsume",
				"com.carucrm.biz.consume.ConsumeListConditionBean");
		beanMap.put("ListConsume.mgr", "com.carucrm.biz.consume.ConsumeMgrImpl");
		beanMap.put("ListConsume.api", "listConsume");
		beanMap.put("ListConsume.authentication", "true");

		beanMap.put("VerifyCode", "java.lang.String");
		beanMap.put("VerifyCode.mgr", "com.carucrm.biz.carowner.AccountMgrImpl");
		beanMap.put("VerifyCode.api", "verifyCode");
		beanMap.put("VerifyCode.authentication", "false");

		beanMap.put("CheckCarOwnerAccountEmailDuplicated", "java.lang.String");
		beanMap.put("CheckCarOwnerAccountEmailDuplicated.mgr",
				"com.carucrm.biz.carowner.AccountMgrImpl");
		beanMap.put("CheckCarOwnerAccountEmailDuplicated.api",
				"isCarOwnerAccountEmailDuplicated");
		beanMap.put("CheckCarOwnerAccountEmailDuplicated.authentication",
				"false");

		beanMap.put("CheckCarOwnerAccountNameDuplicated", "java.lang.String");
		beanMap.put("CheckCarOwnerAccountNameDuplicated.mgr",
				"com.carucrm.biz.carowner.AccountMgrImpl");
		beanMap.put("CheckCarOwnerAccountNameDuplicated.api",
				"isCarOwnerAccountNameDuplicated");
		beanMap.put("CheckCarOwnerAccountNameDuplicated.authentication",
				"false");

		beanMap.put("RegisterCarOwnerAccount",
				"com.carucrm.biz.carowner.AccountBean");
		beanMap.put("RegisterCarOwnerAccount.mgr",
				"com.carucrm.biz.carowner.AccountMgrImpl");
		beanMap.put("RegisterCarOwnerAccount.api", "registerCarOwnerAccount");
		beanMap.put("RegisterCarOwnerAccount.authentication", "false");

		beanMap.put("SendVerifiedCode", "java.lang.String");
		beanMap.put("SendVerifiedCode.mgr",
				"com.carucrm.biz.carowner.AccountMgrImpl");
		beanMap.put("SendVerifiedCode.api", "sendVerifiedCode");
		beanMap.put("SendVerifiedCode.authentication", "false");

		beanMap.put("LoginAsCarOwner", "com.carucrm.biz.carowner.LoginBean");
		beanMap.put("LoginAsCarOwner.mgr",
				"com.carucrm.biz.carowner.AccountMgrImpl");
		beanMap.put("LoginAsCarOwner.api", "loginAsCarOwner");
		beanMap.put("LoginAsCarOwner.authentication", "false");

		beanMap.put("GetCarOwnerAccountBean.mgr",
				"com.carucrm.biz.carowner.AccountMgrImpl");
		beanMap.put("GetCarOwnerAccountBean.api", "getCarOwnerAccountBean");
		beanMap.put("GetCarOwnerAccountBean.authentication", "true");

		beanMap.put("UpdateCarOwnerAccount",
				"com.carucrm.biz.carowner.UpdateAccountBean");
		beanMap.put("UpdateCarOwnerAccount.mgr",
				"com.carucrm.biz.carowner.AccountMgrImpl");
		beanMap.put("UpdateCarOwnerAccount.api", "updateCarOwnerAccount");
		beanMap.put("UpdateCarOwnerAccount.authentication", "true");

		beanMap.put("ListCarOwnerConsume",
				"com.carucrm.biz.carowner.ConsumeListConditionBean");
		beanMap.put("ListCarOwnerConsume.mgr",
				"com.carucrm.biz.carowner.ConsumeMgrImpl");
		beanMap.put("ListCarOwnerConsume.api", "listCarOwnerConsume");
		beanMap.put("ListCarOwnerConsume.authentication", "true");

		beanMap.put("GetCarOwnerConsume",
				"com.carucrm.biz.carowner.ConsumeIDBean");
		beanMap.put("GetCarOwnerConsume.mgr",
				"com.carucrm.biz.carowner.ConsumeMgrImpl");
		beanMap.put("GetCarOwnerConsume.api", "getConsume");
		beanMap.put("GetCarOwnerConsume.authentication", "true");
	}

	public String getProperty(String name) {
		return (String) beanMap.get(name);
	}
}
