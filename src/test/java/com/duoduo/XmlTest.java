package com.duoduo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Ignore;
import org.junit.Test;

import com.duoduo.message.resp.ReceiveMessageResp;
import com.duoduo.message.resp.SendMessageResp;
import com.duoduo.util.GsonUtil;

public class XmlTest {

	@Test
	@Ignore
	public void receiveMessageTest() {
		String xmlResult = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms><callbox><mobile>13760276565</mobile><taskid>1351722</taskid><content>棒</content><receivetime>2017/5/13 13:09:46</receivetime><extno></extno></callbox><callbox><mobile>13760276565</mobile><taskid>1351722</taskid><content>棒2</content><receivetime>2017/5/13 13:09:46</receivetime><extno></extno></callbox></returnsms>";
		List<ReceiveMessageResp> receiveMessages = listReceiveMessage(xmlResult);
		System.out.println(GsonUtil.toJson(receiveMessages));
	}

	@Test
	public void sendMeesageTest() {
		String xmlResult = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms><returnstatus>Success</returnstatus><message>ok</message><remainpoint>1243</remainpoint><taskID>1351736</taskID><successCounts>1</successCounts></returnsms>";
		SendMessageResp sendMessageResp = getSendMessageResp(xmlResult);
		System.out.println(GsonUtil.toJson(sendMessageResp));
	}

	private SendMessageResp getSendMessageResp(String xmlResult) {
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

	private List<ReceiveMessageResp> listReceiveMessage(String xmlResult) {
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
