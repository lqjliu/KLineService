package com.common.web;

public class BasicResponse {
	private String job;
	private String kLineTicket;
	private boolean status = true;
	private String errorKey;
	private String msg;
	private Object data;
	private String additionalInfo;
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getKLineTicket() {
		return kLineTicket;
	}
	public void setKLineTicket(String credential) {
		this.kLineTicket = credential;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	public String getErrorKey() {
		return errorKey;
	}
	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}
	
}
