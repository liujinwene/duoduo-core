package com.duoduo.thirdorder.resp;

public class BaseThirdOrderResp {
	private static final String SUCCESS = "0";	
	private String st;
	private String msg;
	private String code;
	private String page;
	private String size;
	private String total;
	
	public boolean isSuccess() {
		if(this.code != null && this.code.equals(SUCCESS)) {
			return true;
		}
		return false;
	}
	
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}

}
