package com.duoduo.message.service;

import java.util.List;

import com.duoduo.message.resp.ReceiveMessageResp;
import com.duoduo.message.resp.SendMessageResp;
import com.duoduo.thirdorder.resp.ThirdOrderResp;

public interface MessageService {

	SendMessageResp sendMessage(ThirdOrderResp order);

	List<ReceiveMessageResp> receiveMessage();

}
