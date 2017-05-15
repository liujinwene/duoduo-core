package com.duoduo.execute.runnable;

import com.duoduo.message.resp.ReceiveMessageResp;
import com.duoduo.message.service.MessageService;
import com.duoduo.thirdorder.service.OrderService;
import com.duoduo.util.SpringContextUtil;

public class UpdateThirdOrderRunnable implements Runnable {
	
	private ReceiveMessageResp receiveMessage;
	
	public UpdateThirdOrderRunnable(ReceiveMessageResp receiveMessage) {
		this.receiveMessage = receiveMessage;
	}

	@Override
	public void run() {
		OrderService orderService = SpringContextUtil.getBean(OrderService.class);
		orderService.updateOrder(receiveMessage);
		
	}
	

}
