package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

public class ParentCityModel implements YanxiuBaseBean{
	private String name;
	private String id;
	private String provinceName;
	private String provinceId;
	private ArrayList<ParentDistrictModel> districtList;

	public ParentCityModel() {
		super();
	}

	public ParentCityModel(String name, ArrayList<ParentDistrictModel> districtList) {
		super();
		this.name = name;
		this.districtList = districtList;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<ParentDistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(ArrayList<ParentDistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}
	
}
