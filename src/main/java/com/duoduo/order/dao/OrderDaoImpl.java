package com.duoduo.order.dao;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.duoduo.schema.Tables;
import com.duoduo.schema.tables.records.OrderRecord;

@Component
public class OrderDaoImpl implements OrderDao {
	
	@Autowired
	private DSLContext dsl;

	@Override
	public void create(OrderRecord orderRecord) {
		dsl.executeInsert(orderRecord);
	}

	@Override
	public OrderRecord findByTaskId(String taskId) {
		return dsl.selectFrom(Tables.ORDER).where(Tables.ORDER.TASK_ID.eq(taskId)).fetchOne();
	}

	@Override
	public OrderRecord findByOrderId(String orderId) {
		return dsl.selectFrom(Tables.ORDER).where(Tables.ORDER.ORDER_ID.eq(orderId)).fetchOne();
	}

	@Override
	public void update(OrderRecord orderRecord) {
		dsl.executeUpdate(orderRecord);
	}

}
