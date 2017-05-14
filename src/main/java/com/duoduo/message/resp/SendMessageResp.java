package com.duoduo.message.resp;

public class SendMessageResp {
	private String returnstatus;
	private String message;
	private String remainpoint;
	private String taskID;
	private String successCounts;
	
	private static final String RETURNSTATUS = "returnstatus";
	private static final String MESSAGE = "message";
	private static final String REMAINPOINT = "remainpoint";
	private static final String TASKID = "taskID";
	private static final String SUCCESSCOUNTS = "successCounts";
	
	public void setValue(String name, String value) {
		switch (name) {
		case RETURNSTATUS:
			this.remainpoint = value;
			break;
		case MESSAGE:
			this.message = value;
			break;
		case REMAINPOINT:
			this.remainpoint = value;
			break;
		case TASKID:
			this.taskID = value;
			break;
		case SUCCESSCOUNTS:
			this.successCounts = value;
			break;
		default:
			break;
		}
	}
	
	public String getReturnstatus() {
		return returnstatus;
	}
	public void setReturnstatus(String returnstatus) {
		this.returnstatus = returnstatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getRemainpoint() {
		return remainpoint;
	}
	public void setRemainpoint(String remainpoint) {
		this.remainpoint = remainpoint;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getSuccessCounts() {
		return successCounts;
	}
	public void setSuccessCounts(String successCounts) {
		this.successCounts = successCounts;
	}
}
