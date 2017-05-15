package com.duoduo.execute.service;

import com.duoduo.message.resp.ReceiveMessageResp;
import com.duoduo.thirdorder.resp.ThirdOrderResp;

public interface ExecuteService {
	void sendMessage(ThirdOrderResp order);
	void updateThirdOrder(ReceiveMessageResp receiveMessage);

}
