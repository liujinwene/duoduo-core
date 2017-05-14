package com.duoduo;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.duoduo.base.core.CoreServerApp;
import com.duoduo.order.po.Order;
import com.duoduo.schema.Tables;
import com.duoduo.schema.tables.records.OrderRecord;
import com.duoduo.thirdorder.resp.ListThirdOrderResp;
import com.duoduo.thirdorder.resp.SubThirdOrder;
import com.duoduo.thirdorder.resp.ThirdOrderResp;
import com.duoduo.util.GsonUtil;
import com.duoduo.util.HttpUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CoreServerApp.class)
@WebAppConfiguration
public class OrderTest {
	
	@Autowired
	private DSLContext dsl;
	
	@Test
	@Ignore
	public void fetchTest() {
		List<Order> orders = new ArrayList<Order>();
		Result<OrderRecord> result = dsl.selectFrom(Tables.ORDER).fetch();
		result.map(r -> {
			System.out.println(r.getId());
			return r;
		});
	}
	
	@Test
	public void createTest() {
		OrderRecord order = new OrderRecord();
		order.setTaskId("2");
		order.setOrderJson("test");
		order.setCreateTime(new Timestamp(System.currentTimeMillis()));
		int result = dsl.executeUpdate(order);
		System.err.println(result);
	}

	@Test
	@Ignore
	public void getOrderTest() throws IOException {
		listOrder();
	}

	private ListThirdOrderResp listOrder() {
		String url = "https://shop.snssdk.com/order/order/list";
		
		String __token = "39ed2a4393332e132021bbd8751b44f5";
		String final_status = "4";
		String order = "create_time";
		String is_desc = "is_desc";
		String start_time = "2017-05-13%2009:42:49";
		String order_id = "6419571953484431618";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("__token", __token);
		params.put("final_status", final_status);
		params.put("order", order);
		params.put("is_desc", is_desc);
		params.put("start_time", start_time);
		params.put("order_id", order_id);
		
		String responseStr = HttpUtil.get(url, getHeaderInfo(), params);
		System.out.println(responseStr);
		return GsonUtil.fromJson(responseStr, ListThirdOrderResp.class);
	}

	@Test
	@Ignore
	public void updateOrderTest2() {
		String url = "https://shop.snssdk.com/order/order/edit";
		String __token= "39ed2a4393332e132021bbd8751b44f5";
		String messageStr = "【短信回复:%s】";

		ListThirdOrderResp listOrderResp = listOrder();
		if(listOrderResp.isSuccess()) {
			List<ThirdOrderResp> orders = listOrderResp.getData();
			if(orders != null && !orders.isEmpty()) {
				for(ThirdOrderResp order : orders) {
					SubThirdOrder subOrder = order.getOrder();
					Map<String, String> params = convertToOrderUpdateParam(subOrder, __token, messageStr);
					
					String responseStr = HttpUtil.post(url, getHeaderInfo(), params);
					System.out.println(responseStr);
				}
			}
		}
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

	@Test
	@Ignore
	public void getAccountInfoTest() {
		String url = "https://shop.snssdk.com/common/index/index?_:1494736482920";
		String responseStr = HttpUtil.get(url, getHeaderInfo(), null);
		System.out.println(responseStr);
	}

	private static Map<String, String> getHeaderInfo() {
		String cookie = "PHPSESSID=647j8s3tm5n0bdbj8tb0pe3t23";
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36";
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Cookie", cookie);
		headers.put("User-Agent", userAgent);

		//		headers.put("Origin", "https://sso.toutiao.com");
		//		headers.put("X-CSRFToken", "undefined");
		return headers;
	}

}
