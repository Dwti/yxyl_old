package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;


public class ParentProvinceModel implements YanxiuBaseBean {
	private String name;
	private String id;
	private ArrayList<ParentCityModel> cityList;

	public ParentProvinceModel() {
		super();
	}

	public ParentProvinceModel(String name, ArrayList<ParentCityModel> cityList) {
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

	public ArrayList<ParentCityModel> getCityList() {
		return cityList;
	}

	public void setCityList(ArrayList<ParentCityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}
	
}
