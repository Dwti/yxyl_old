package com.yanxiu.gphone.parent.utils;

import com.yanxiu.gphone.parent.bean.ParentCityModel;
import com.yanxiu.gphone.parent.bean.ParentDistrictModel;
import com.yanxiu.gphone.parent.bean.ParentProvinceModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ParentXmlParserHandler extends DefaultHandler {

	/**
	 * 存储所有的解析对象
	 */
	private ArrayList<ParentProvinceModel> provinceList = new ArrayList<ParentProvinceModel>();

	public ParentXmlParserHandler() {

	}

	public ArrayList<ParentProvinceModel> getDataList() {
		return provinceList;
	}

	@Override
	public void startDocument() throws SAXException {
		// 当读到第一个开始标签的时候，会触发这个方法
	}

	ParentProvinceModel provinceModel = new ParentProvinceModel();
	ParentCityModel cityModel = new ParentCityModel();
	ParentDistrictModel districtModel = new ParentDistrictModel();

	@Override
	public void startElement(String uri, String localName, String qName,
							 Attributes attributes) throws SAXException {
		// 当遇到开始标记的时候，调用这个方法
		if (qName.equals("province")) {
			provinceModel = new ParentProvinceModel();
			provinceModel.setName(attributes.getValue(0));
			provinceModel.setId(attributes.getValue(1));
			provinceModel.setCityList(new ArrayList<ParentCityModel>());
		} else if (qName.equals("city")) {
			cityModel = new ParentCityModel();
			cityModel.setProvinceName(provinceModel.getName());
			cityModel.setProvinceId(provinceModel.getId());
			cityModel.setName(attributes.getValue(0));
			cityModel.setId(attributes.getValue(1));
			cityModel.setDistrictList(new ArrayList<ParentDistrictModel>());
		} else if (qName.equals("district")) {
			districtModel = new ParentDistrictModel();
			districtModel.setCityName(cityModel.getName());
			districtModel.setProvinceName(cityModel.getProvinceName());
			districtModel.setProvinceId(cityModel.getProvinceId());
			districtModel.setCityId(cityModel.getId());
			districtModel.setName(attributes.getValue(0));
			districtModel.setZipcode(attributes.getValue(1));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// 遇到结束标记的时候，会调用这个方法
		if (qName.equals("district")) {
			cityModel.getDistrictList().add(districtModel);
		} else if (qName.equals("city")) {
			provinceModel.getCityList().add(cityModel);
		} else if (qName.equals("province")) {
			provinceList.add(provinceModel);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

}
