package com.duoduo.message.service;

import java.util.List;

import com.duoduo.message.resp.ReceiveMessageResp;
import com.duoduo.message.resp.SendMessageResp;

public interface MessageService {

	SendMessageResp sendMessage(String mobile);

	List<ReceiveMessageResp> receiveMessage();

}
