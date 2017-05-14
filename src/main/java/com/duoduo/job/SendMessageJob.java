package com.duoduo.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoduo.message.service.MessageService;
import com.duoduo.thirdorder.resp.ListThirdOrderResp;
import com.duoduo.thirdorder.resp.ThirdOrderResp;
import com.duoduo.thirdorder.service.OrderService;

@Service
public class SendMessageJob {

	@Autowired
	private OrderService orderService;
	@Autowired
	private MessageService messageService;

	public void execute() {
		ListThirdOrderResp listOrderResp = orderService.listOrder();
		if(listOrderResp.isSuccess()) {
			List<ThirdOrderResp> orders = listOrderResp.getData();
			if(orders != null && !orders.isEmpty()) {
				for(ThirdOrderResp order : orders) {
					try {
						messageService.sendMessage(order.getOrder().getPost_tel());
					} catch(Exception e) {
						System.err.println("sendMessage exception.orderId=" + order.getOrder().getOrder_id());
						e.printStackTrace();
					}
				}
			}
		}
	}

}
