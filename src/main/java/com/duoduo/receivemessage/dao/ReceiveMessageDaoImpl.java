package com.duoduo.receivemessage.dao;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.duoduo.schema.tables.records.ReceiveMessageRecord;

@Component
public class ReceiveMessageDaoImpl implements ReceiveMessageDao {

	@Autowired
	private DSLContext dsl;
	
	@Override
	public void create(ReceiveMessageRecord record) {
		dsl.executeInsert(record);
	}

}
