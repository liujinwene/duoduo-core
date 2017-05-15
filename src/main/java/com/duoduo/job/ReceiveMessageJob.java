package com.duoduo.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoduo.execute.service.ExecuteService;
import com.duoduo.message.resp.ReceiveMessageResp;
import com.duoduo.message.service.MessageService;

@Service
public class ReceiveMessageJob {
	
	@Autowired
	private ExecuteService executeService;
	@Autowired
	private MessageService messageService;
	
	public void execute() {
		List<ReceiveMessageResp> receiveMessages = messageService.receiveMessage();
		if(receiveMessages != null && !receiveMessages.isEmpty()) {
			for(ReceiveMessageResp receiveMessage : receiveMessages) {
				executeService.updateThirdOrder(receiveMessage);
			}
		}
    }
}
