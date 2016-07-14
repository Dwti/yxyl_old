package com.yanxiu.basecore.impl;

import android.text.TextUtils;
import com.yanxiu.basecore.YanxiuHttpJavaHandler;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.*;
import com.yanxiu.basecore.parse.YanxiuBaseParser;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.basecore.utils.BaseCoreLogInfo;
import com.yanxiu.basecore.utils.SymEncryptUtil;

import java.io.IOException;

/**
 * 请求封装内
 * */
public class YanxiuHttpTool<T extends YanxiuBaseBean> {
	
	public <D> YanxiuDataHull<T> requsetData(YanxiuHttpBaseParameter<T, D, ?> httpParameter) {
		YanxiuDataHull<T> dataHull;
		if (httpParameter == null) {
			dataHull = new YanxiuDataHull<T>();
			dataHull.setDataType(YanxiuDataHull.DataType.PARAMS_IS_NULL);
			BaseCoreLogInfo.err("Parameter is null");
			return dataHull;
		}
		if (httpParameter.getType() == YanxiuHttpParameter.Type.GET) {
			dataHull = doGet(httpParameter);
		} else if (httpParameter.getType() == YanxiuHttpParameter.Type.POST) {
			dataHull = doPost(httpParameter);
		} else {
			dataHull = new YanxiuDataHull<T>();
			dataHull.setDataType(YanxiuDataHull.DataType.REQUESTMETHOD_IS_ERR);
			BaseCoreLogInfo.err("RequestMethod is error");
			return dataHull;
		}
//		dataHull.setUpdataId(httpParameter.getUpdataId());
		return dataHull;
	}

	private <D> YanxiuDataHull<T> doGet(YanxiuHttpBaseParameter<T, D, ?> httpParameter) {
		YanxiuBaseParser<T, D> parser = httpParameter.getParser();
		String response = null ;
		YanxiuDataHull<T> dataHull = new YanxiuDataHull<T>();
        YanxiuHttpJavaHandler handler = new YanxiuHttpJavaHandler();
        try {
			response = handler.doGet(httpParameter);
			if(httpParameter.isEncode()){
				response = SymEncryptUtil.decryptDES(response, "DES");
			}
			if (parser != null) {
				dataHull.setDataEntity(parser.initialParse(response));
				dataHull.setDataType(YanxiuDataHull.DataType.DATA_IS_INTEGRITY);
				dataHull.setSourceData(response);
				BaseCoreLogInfo.err("DATA  IS INTEGRITY response=" + response);
				return dataHull;
			} else {
				dataHull.setDataType(YanxiuDataHull.DataType.DATA_PARSER_IS_NULL);
				BaseCoreLogInfo.err("Do not have parser");
			}
		} catch (IOException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.CONNECTION_FAIL);
			BaseCoreLogInfo.err("connected is fail");
		} catch (ParseException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.DATA_PARSE_EXCEPTION);
			BaseCoreLogInfo.err("parse error");
		} catch (DataIsNullException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.DATA_IS_NULL);
			BaseCoreLogInfo.err("data is null");
		} catch (JsonCanNotParseException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.DATA_CAN_NOT_PARSE);
			BaseCoreLogInfo.err("canParse is false");
		} catch (DataIsErrException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.DATA_IS_ERR);
			BaseCoreLogInfo.err("data is err");
		} catch (DataNoUpdateException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.DATA_NO_UPDATE);
			BaseCoreLogInfo.err("data has not update");
		} finally {
            try{
                if (parser != null) {
	                dataHull.setErrMsg(parser.getErrorMsg());
					if(parser.getErrorMsg() == YanxiuMobileParser.STATE.NODATA){
						if(TextUtils.isEmpty(parser.getMessage())){
							dataHull.setMessage("暂无相关数据");
						} else {
							dataHull.setMessage(parser.getMessage());
						}
					} else {
						dataHull.setMessage(parser.getMessage());
					}
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
            }
		}

		return dataHull;
	}

	private <D> YanxiuDataHull<T> doPost(YanxiuHttpBaseParameter<T, D, ?> httpParameter) {
		YanxiuBaseParser<T, D> parser = httpParameter.getParser();
		String response = null ;
		YanxiuDataHull<T> dataHull = new YanxiuDataHull<T>();
        YanxiuHttpJavaHandler handler = new YanxiuHttpJavaHandler();
        try {
			response = handler.doPost(httpParameter);
			if(httpParameter.isEncode()){
				response = SymEncryptUtil.decryptDES(response, "DES");
			}
			if (parser != null) {
				dataHull.setDataType(YanxiuDataHull.DataType.DATA_IS_INTEGRITY);
				dataHull.setDataEntity(parser.initialParse(response));
				dataHull.setSourceData(response);
				BaseCoreLogInfo.err("complete!");
				return dataHull;
			} else {
				dataHull.setDataType(YanxiuDataHull.DataType.DATA_PARSER_IS_NULL);
				BaseCoreLogInfo.err("Do not have parser");
			}
		} catch (IOException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.CONNECTION_FAIL);
			BaseCoreLogInfo.err("connected is fail");
		} catch (ParseException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.DATA_PARSE_EXCEPTION);
			BaseCoreLogInfo.err("parse error");
		} catch (DataIsNullException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.DATA_IS_NULL);
			BaseCoreLogInfo.err("data is null");
		} catch (JsonCanNotParseException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.DATA_CAN_NOT_PARSE);
			BaseCoreLogInfo.err("json can not parse");
		} catch (DataIsErrException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.DATA_IS_ERR);
			BaseCoreLogInfo.err("data is err");
		} catch (DataNoUpdateException e) {
			e.printStackTrace();
			dataHull.setDataType(YanxiuDataHull.DataType.DATA_NO_UPDATE);
			BaseCoreLogInfo.err("data has not update");
		} finally {
            try{
                if (parser != null) {
	                dataHull.setErrMsg(parser.getErrorMsg());
                    dataHull.setMessage(parser.getMessage());
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {

            }
        }
		return dataHull;
	}
}
