package com.duoduo.order.po;

public class Order {
	private Long id;
	private String taskId; 
	private String orderJson;
	private Long createTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getOrderJson() {
		return orderJson;
	}
	public void setOrderJson(String orderJson) {
		this.orderJson = orderJson;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}
