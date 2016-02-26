package com.common.exception;

public class DataFormatInvalidException extends CommonException{
	private String field;
	private String type;
	public DataFormatInvalidException(String field, String type){
		super(ExceptionKeys.DATA_FORMAT_INVALID);
		this.field = field;
		this.type = type;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
