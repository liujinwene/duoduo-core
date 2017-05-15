package com.duoduo.message.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jooq.util.derby.sys.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoduo.job.SendMessageJob;
import com.duoduo.message.resp.ReceiveMessageResp;
import com.duoduo.message.resp.SendMessageResp;
import com.duoduo.message.service.MessageService;
import com.duoduo.order.dao.OrderDao;
import com.duoduo.schema.tables.records.OrderRecord;
import com.duoduo.thirdorder.resp.ThirdOrderResp;
import com.duoduo.util.DefaultHttpUtils;
import com.duoduo.util.GsonUtil;

@Service
public class MessageServiceImpl implements MessageService {
	private static Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);
	
	private static final Lock CREATE_ORDER_LOCK = new ReentrantLock();


	private static final String sendUrl = "http://119.29.60.211:8888/sms.aspx";
	private static final String receiveUrl = "http://119.29.60.211:8888/callApi.aspx";

	private static final String userId = "817";
	private static final String account = "278810263@qq.com";
	private static final String password = "abc12345666";

	private static final String sendContent = "【多多优品】您好！您购买的智能蓝牙太阳眼镜，订单已接收。回复：1-确认发货，2-取消订单。客服电话：0755-82347124";

	private static final String sendAction = "send";
	private static final String queryAction = "query";

	@Autowired
	private OrderDao orderDao;

	@Override
	public SendMessageResp sendMessage(ThirdOrderResp order) {
		try {
			CREATE_ORDER_LOCK.lock();
			OrderRecord existOrderRecord = orderDao.findByOrderId(order.getOrder().getOrder_id());
			if(existOrderRecord != null) {
				LOGGER.info("orderRecord is exist.orderId=" + order.getOrder().getOrder_id());
				return null;
			}

			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", userId);
			params.put("account", account);
			params.put("password", password);
			params.put("mobile", order.getOrder().getPost_tel());
			params.put("content", sendContent);
			params.put("action", sendAction);

			String responseStr = DefaultHttpUtils.get(sendUrl, params);
			LOGGER.info(responseStr);
			SendMessageResp sendMessageResp = getSendMessageResp(responseStr);
			LOGGER.info(GsonUtil.toJson(sendMessageResp));
			//create orderRecord
			if(sendMessageResp != null && StringUtils.isNotBlank(sendMessageResp.getTaskID())) {
				OrderRecord orderRecord = new OrderRecord();
				orderRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));
				orderRecord.setOrderJson(GsonUtil.toJson(order));
				orderRecord.setTaskId(sendMessageResp.getTaskID());
				orderRecord.setContent(sendContent);
				orderRecord.setMobile(order.getOrder().getPost_tel());
				orderRecord.setOrderId(order.getOrder().getOrder_id());
				orderRecord.setSendMessageJson(GsonUtil.toJson(sendMessageResp));
				orderDao.create(orderRecord);
			}
			return sendMessageResp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			CREATE_ORDER_LOCK.unlock();
		}
	}

	@Override
	public List<ReceiveMessageResp> receiveMessage() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", userId);
			params.put("account", account);
			params.put("password", password);
			params.put("action", queryAction);

			String responseStr = DefaultHttpUtils.get(receiveUrl, params);
			LOGGER.info(responseStr);
			List<ReceiveMessageResp> receiveMessages = listReceiveMessage(responseStr);
			LOGGER.info(GsonUtil.toJson(receiveMessages));
			return receiveMessages;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static SendMessageResp getSendMessageResp(String xmlResult) {
		try {
			// 将xml格式字符串转化为DOM对象
			Document document = DocumentHelper.parseText(xmlResult);
			// 获取根结点对象
			SendMessageResp sendMessageResp = new SendMessageResp();
			Element rootElement = document.getRootElement();
			// 循环第一层节点，获取其子节点
			for (Iterator iterInner = rootElement.elementIterator(); iterInner.hasNext();) {
				// 获取标签对象
				Element elementOption = (Element) iterInner.next();
				// 获取该标签对象的名称
				String tagName = elementOption.getName();
				// 获取该标签对象的内容
				String tagContent = elementOption.getTextTrim();
				sendMessageResp.setValue(tagName, tagContent);
			}
			return sendMessageResp;
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static List<ReceiveMessageResp> listReceiveMessage(String xmlResult) {
		try {
			// 将xml格式字符串转化为DOM对象
			Document document = DocumentHelper.parseText(xmlResult);
			// 获取根结点对象
			Element rootElement = document.getRootElement();
			// 循环根节点，获取其子节点
			List<ReceiveMessageResp> list = new ArrayList<ReceiveMessageResp>();
			for (Iterator iter = rootElement.elementIterator(); iter.hasNext();) {
				Element element = (Element) iter.next(); // 获取标签对象
				ReceiveMessageResp receiveResp = new ReceiveMessageResp();
				// 循环第一层节点，获取其子节点
				for (Iterator iterInner = element.elementIterator(); iterInner.hasNext();) {
					// 获取标签对象
					Element elementOption = (Element) iterInner.next();
					// 获取该标签对象的名称
					String tagName = elementOption.getName();
					// 获取该标签对象的内容
					String tagContent = elementOption.getTextTrim();
					receiveResp.setValue(tagName, tagContent);
				}
				list.add(receiveResp);
			}
			return list;

		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
