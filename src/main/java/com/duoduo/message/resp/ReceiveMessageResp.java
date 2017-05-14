package com.duoduo.message.resp;

public class ReceiveMessageResp {
	private String mobile;
	private String taskid;
	private String content;
	private String receivetime;
	private String extno;
	
	public static final String MOBILE = "mobile"; 
	public static final String TASKID = "taskid"; 
	public static final String CONTENT = "content"; 
	public static final String RECEIVETIME = "receivetime"; 
	public static final String EXTNO = "extno"; 
	
	public void setValue(String name, String value) {
		switch (name) {
		case MOBILE:
			this.mobile = value;
			break;
		case TASKID:
			this.taskid = value;
			break;
		case CONTENT:
			this.content = value;
			break;
		case RECEIVETIME:
			this.receivetime = value;
			break;
		case EXTNO:
			this.extno = value;
			break;
		default:
			break;
		}
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReceivetime() {
		return receivetime;
	}
	public void setReceivetime(String receivetime) {
		this.receivetime = receivetime;
	}
	public String getExtno() {
		return extno;
	}
	public void setExtno(String extno) {
		this.extno = extno;
	}

}
