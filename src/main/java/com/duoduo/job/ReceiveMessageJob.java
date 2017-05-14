package com.duoduo.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoduo.message.resp.ReceiveMessageResp;
import com.duoduo.message.service.MessageService;
import com.duoduo.thirdorder.service.OrderService;

@Service
public class ReceiveMessageJob {
	
	private static final String messageFormat = "【短信回复:%s】";
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private MessageService messageService;
	
	public void execute() {
		List<ReceiveMessageResp> receiveMessages = messageService.receiveMessage();
		if(receiveMessages != null && !receiveMessages.isEmpty()) {
			for(ReceiveMessageResp receiveMessage : receiveMessages) {
//				getSubThirdOrder(receiveMessage.getTaskid());
			}
		}
    }
}
