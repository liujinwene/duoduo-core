package com.duoduo.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoduo.configuration.constant.ConfigName;
import com.duoduo.configuration.service.ConfigurationService;
import com.duoduo.execute.service.ExecuteService;
import com.duoduo.thirdorder.resp.ListThirdOrderResp;
import com.duoduo.thirdorder.resp.ThirdOrderResp;
import com.duoduo.thirdorder.service.OrderService;

@Service
public class SendMessageJob {

	@Autowired
	private OrderService orderService;
	@Autowired
	private ExecuteService executeService;
	@Autowired
	private ConfigurationService configurationService;

	public void execute() {
		ListThirdOrderResp listOrderResp = orderService.listOrder();
		if(listOrderResp.isSuccess()) {
			List<ThirdOrderResp> orders = listOrderResp.getData();
			if(orders != null && !orders.isEmpty()) {
				configurationService.update(ConfigName.EXPIRED_TIME, orders.get(0).getOrder().getCreate_time());
				for(ThirdOrderResp order : orders) {
					try {
						executeService.sendMessage(order);
					} catch(Exception e) {
						System.err.println("sendMessage exception.orderId=" + order.getOrder().getOrder_id());
						e.printStackTrace();
					}
				}
			}
		}
	}

}
