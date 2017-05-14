package com.duoduo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import com.duoduo.message.resp.ReceiveMessageResp;
import com.duoduo.message.resp.SendMessageResp;
import com.duoduo.util.DefaultHttpUtils;
import com.duoduo.util.GsonUtil;

public class MessageTest {
	
	@Test
	public void messageTest() throws IOException {
//		sendMessage();
		receiveMessage();
	}
	
	private static void sendMessage() throws IOException {
		String url = "http://119.29.60.211:8888/sms.aspx";

		//userId,account,password,mobile,content,sendTime,action,extno
		String userId = "817";
		String account = "278810263@qq.com";
		String password = "abc12345666";
		String mobile = "13760276565";
		String content = "【多多优品】您好！您的订单已接受。回复：1-确认发货，2-取消订单。客服电话：0755-400111222。退订回复TD";
		String action = "send";
		String extno = "111222";

		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("account", account);
		params.put("password", password);
		params.put("mobile", mobile);
		params.put("content", content);
		params.put("action", action);
		params.put("extno", extno);

		String responseStr = DefaultHttpUtils.get(url, params);
		System.out.println(responseStr);
		SendMessageResp sendMessageResp = getSendMessageResp(responseStr);
		System.out.println(GsonUtil.toJson(sendMessageResp));
	}

	private static void receiveMessage() throws IOException {
		String url = "http://119.29.60.211:8888/callApi.aspx";
		
		//userId,account,password,action
		String userId = "817";
		String account = "278810263@qq.com";
		String password = "abc12345666";
		String action = "query";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("account", account);
		params.put("password", password);
		params.put("action", action);
		
		String responseStr = DefaultHttpUtils.get(url, params);
		System.out.println(responseStr);
		List<ReceiveMessageResp> receiveMessages = listReceiveMessage(responseStr);
		System.out.println(GsonUtil.toJson(receiveMessages));
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
