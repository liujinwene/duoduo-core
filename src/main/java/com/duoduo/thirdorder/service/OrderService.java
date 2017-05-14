package com.duoduo.thirdorder.service;

import com.duoduo.thirdorder.resp.BaseThirdOrderResp;
import com.duoduo.thirdorder.resp.ListThirdOrderResp;
import com.duoduo.thirdorder.resp.SubThirdOrder;

public interface OrderService {

	ListThirdOrderResp listOrder();

	BaseThirdOrderResp updateOrder(SubThirdOrder subOrder, String content);

}
