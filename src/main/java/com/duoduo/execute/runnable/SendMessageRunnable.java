package com.duoduo.execute.runnable;

import com.duoduo.message.service.MessageService;
import com.duoduo.thirdorder.resp.ThirdOrderResp;
import com.duoduo.util.SpringContextUtil;

public class SendMessageRunnable implements Runnable {
	private ThirdOrderResp order;
	
	public SendMessageRunnable(ThirdOrderResp order) {
		this.order = order;
	}
	
	@Override
	public void run() {
		MessageService messageService = SpringContextUtil.getBean(MessageService.class);
		messageService.sendMessage(order);
	}

}
