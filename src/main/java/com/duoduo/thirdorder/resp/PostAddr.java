package com.duoduo.thirdorder.resp;

public class PostAddr {
	private Province province;
	private City city;
	private Town town;
	private String detail;
	
	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public Town getTown() {
		return town;
	}
	public void setTown(Town town) {
		this.town = town;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}
