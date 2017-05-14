package com.duoduo.thirdorder.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.duoduo.thirdorder.resp.BaseThirdOrderResp;
import com.duoduo.thirdorder.resp.ListThirdOrderResp;
import com.duoduo.thirdorder.resp.SubThirdOrder;
import com.duoduo.thirdorder.service.OrderService;
import com.duoduo.util.GsonUtil;
import com.duoduo.util.HttpUtil;

@Service
public class OrderServiceImpl implements OrderService {

	String listOrderUrl = "https://shop.snssdk.com/order/order/list";
	String updateOrderUrl = "https://shop.snssdk.com/order/order/edit";
	
	String final_status = "1";
	String order = "create_time";
	String is_desc = "is_desc";
	String start_time = "2017-05-13%2009:42:49";

	@Override
	public ListThirdOrderResp listOrder() {
		String __token = "39ed2a4393332e132021bbd8751b44f5";

		Map<String, String> params = new HashMap<String, String>();
		params.put("__token", __token);
		params.put("final_status", final_status);
		params.put("order", order);
		params.put("is_desc", is_desc);
		params.put("start_time", start_time);

		String responseStr = HttpUtil.get(listOrderUrl, getHeaderInfo(), params);
		System.out.println(responseStr);
		return GsonUtil.fromJson(responseStr, ListThirdOrderResp.class);
	}

	@Override
	public BaseThirdOrderResp updateOrder(SubThirdOrder subOrder, String content) {
		if(StringUtils.isBlank(content)) {
			System.err.println("receive content is null.orderId=" + subOrder.getOrder_id());
			return null;
		}
		if(subOrder.getBuyer_words().contains(content)) {
			System.err.println("receive content is exist.orderId=" + subOrder.getOrder_id());
			return null;
		}
		
		String __token= "39ed2a4393332e132021bbd8751b44f5";

		Map<String, String> params = convertToOrderUpdateParam(subOrder, __token, content);
		String responseStr = HttpUtil.post(updateOrderUrl, getHeaderInfo(), params);
		return GsonUtil.fromJson(responseStr, BaseThirdOrderResp.class);
	}

	private Map<String, String> convertToOrderUpdateParam(SubThirdOrder subOrder, String __token, String messageStr) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("order_id", subOrder.getOrder_id());
		if(StringUtils.isNotBlank(messageStr)) {
			params.put("buyer_words", subOrder.getBuyer_words() + messageStr);
		}
		params.put("combo_amount", subOrder.getCombo_amount());
		params.put("total_amount", subOrder.getTotal_amount());
		params.put("combo_id", subOrder.getCombo_id());
		params.put("combo_num", subOrder.getCombo_num());
		params.put("logistics_id", subOrder.getLogistics_id());
		params.put("logistics_code", subOrder.getLogistics_code());
		params.put("post_addr[province][id]", subOrder.getPost_addr().getProvince().getId());
		params.put("post_addr[province][name]", subOrder.getPost_addr().getProvince().getName());
		params.put("post_addr[city][id]", subOrder.getPost_addr().getCity().getId());
		params.put("post_addr[city][name]", subOrder.getPost_addr().getCity().getName());
		params.put("post_addr[town][id]", subOrder.getPost_addr().getTown().getId());
		params.put("post_addr[town][name]", subOrder.getPost_addr().getTown().getName());
		params.put("post_addr[detail]", subOrder.getPost_addr().getDetail());
		params.put("post_tel", subOrder.getPost_tel());
		params.put("post_receiver", subOrder.getPost_receiver());
		if(subOrder.getSpec_desc() != null && !subOrder.getSpec_desc().isEmpty()) {
			params.put("spec_desc[0][name]", subOrder.getSpec_desc().get(0).getName());
			params.put("spec_desc[0][value]", subOrder.getSpec_desc().get(0).getValue());
		}
		params.put("__token", __token);
		return params;
	}

	private static Map<String, String> getHeaderInfo() {
		String cookie = "PHPSESSID=647j8s3tm5n0bdbj8tb0pe3t23";
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Cookie", cookie);
		headers.put("User-Agent", userAgent);
		return headers;
	}

}
