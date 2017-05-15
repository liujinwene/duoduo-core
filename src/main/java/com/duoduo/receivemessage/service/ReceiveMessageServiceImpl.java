package com.duoduo.receivemessage.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.duoduo.message.resp.ReceiveMessageResp;
import com.duoduo.receivemessage.dao.ReceiveMessageDao;
import com.duoduo.schema.tables.records.ReceiveMessageRecord;
import com.duoduo.util.GsonUtil;

@Component
public class ReceiveMessageServiceImpl implements ReceiveMessageService {
	
	@Autowired
	private ReceiveMessageDao receiveMessageDao;

	@Override
	public void create(ReceiveMessageResp receiveMessage) {
		ReceiveMessageRecord receiveMessageRecord = new ReceiveMessageRecord();
		receiveMessageRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));
		receiveMessageRecord.setReceiveMessageJson(GsonUtil.toJson(receiveMessage));
		receiveMessageDao.create(receiveMessageRecord);
	}
	

}
