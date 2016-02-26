package com.common.exception;

public interface ExceptionKeys {
	String JSON_FORMAT_ERROR = "json.format.error";
	String JSON_NO_RQST = "json.no.rqst";
	String JSON_NO_JOB = "json.no.job";
	String NO_KLINE_TICKET = "no.kline.ticket";
	String INVALID_KLINE_TICKET = "invalid.kline.ticket";
	String INVALID_JOB = "invalid.job";
	
	
	String USERNAME_IS_NULL = "username.is.null";
	String PASSWORD_IS_NULL = "password.is.null";
	String EMAIL_IS_NULL = "email.is.null";
	String EMAIL_IS_DUPLICATED = "email.is.in.used";
	String USERNAME_IS_DUPLIDATED = "email.is.in.used";
	String REGISTER_MUST_ACCEPT_RULES = "register.must.accept.rule";
	String DATA_FORMAT_INVALID = "data.format.invalid";
	String USER_NOT_FOUND = "user.not.found";
	String OLD_PASSWORD_WRONG = "old.password.is.wrong";
	String CAR_NOT_FOUND = "car.not.found";
	
	String OLD_PASSWORD_IS_NULL = "old.password.is.null";
	String NEW_PASSWORD_IS_NULL = "new.password.is.null";
	
	String FEE_AMOUNT_IS_NULL = "fee.amount.is.null";
	String FEE_ID_IS_NULL = "fee.id.is.null";
	
	String SYMBOL_IS_NULL = "symbol.is.null";
	String NAME_IS_NULL = "name.is.null";
	String CONTACTNAME_IS_NULL = "contactname.is.null";
	String CONTACTMOBILE_IS_NULL = "contactmobile.is.null";
	String ACCOUNTNAME_IS_NULL = "accountname.is.null";
	String ACCOUNTPASSWORD_IS_NULL = "accountpassword.is.null";
	String ACCOUNTCONTACTNAME_IS_NULL = "accountcontactname.is.null";
	String ACCOUNTCONTACTMOBILE_IS_NULL = "accountcontactmobile.is.null";
	
	String SYMBOL_IS_DUPLICATED = "symbol.is.in.used";
	String CONTACTMOBILE_IS_DUPLICATED = "contactmobile.is.in.used";
	String ACCOUNTNAME_IS_DUPLICATED = "accountname.is.in.used";
	String ACCOUNTEMAIL_IS_DUPLICATED = "accountemail.is.in.used";
	String ACCOUNTCONTACTMOBILE_IS_DUPLICATED = "accountcontactmobile.is.in.used";
	
	String CUSTOMER_IS_NOT_EXISTED = "customer.is.not.existed";
	
	String NO_PRIVILEGE_REVIEW_CONSUME = "no.privilege.review.consume";
}
