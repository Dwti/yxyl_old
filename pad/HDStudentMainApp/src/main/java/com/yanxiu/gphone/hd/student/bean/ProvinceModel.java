package com.yanxiu.gphone.hd.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

public class ProvinceModel  implements YanxiuBaseBean {
	private String name;
	private String id;
	private ArrayList<CityModel> cityList;
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, ArrayList<CityModel> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(ArrayList<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}
	
}
