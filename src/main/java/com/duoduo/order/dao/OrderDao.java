package com.duoduo.order.dao;

import com.duoduo.schema.tables.records.OrderRecord;

public interface OrderDao {
	void create(OrderRecord orderRecord);
	OrderRecord findByTaskId(String taskId);
	OrderRecord findByOrderId(String orderId);
	void update(OrderRecord orderRecord);
}
