package com.duoduo.execute.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import com.duoduo.execute.runnable.SendMessageRunnable;
import com.duoduo.execute.runnable.UpdateThirdOrderRunnable;
import com.duoduo.message.resp.ReceiveMessageResp;
import com.duoduo.thirdorder.resp.ThirdOrderResp;

@Component
public class ExecuteServiceImpl implements ExecuteService {
	ExecutorService threadpool = Executors.newFixedThreadPool(100);

	@Override
	public void sendMessage(ThirdOrderResp order) {
		threadpool.execute(new SendMessageRunnable(order));
	}

	@Override
	public void updateThirdOrder(ReceiveMessageResp receiveMessage) {
		threadpool.execute(new UpdateThirdOrderRunnable(receiveMessage));
	}
	
	
	
}
