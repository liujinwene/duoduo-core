package com.duoduo.thirdorder.resp;

import java.util.List;

public class ListThirdOrderResp extends BaseThirdOrderResp {
	private List<ThirdOrderResp> data;

	public List<ThirdOrderResp> getData() {
		return data;
	}

	public void setData(List<ThirdOrderResp> data) {
		this.data = data;
	}
}
