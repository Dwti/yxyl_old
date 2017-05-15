package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.gphone.studentold.bean.RequestBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.parser.RequestParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestFeedBackTask extends AbstractAsyncTask<RequestBean>{
    private String quesId;
    private static RequestFeedBackTask requestFeedBackTask;
    private RequestFeedBackTask(Context context,String quesId) {
        super(context);
        this.quesId = quesId;
    }

    public static void startFeedBack(Context context,String quesId ){
        requestFeedBackTask = new RequestFeedBackTask(context,quesId);
        requestFeedBackTask.start();
    }

    @Override
    public YanxiuDataHull<RequestBean> doInBackground() {
        return YanxiuHttpApi.requestFeedBackReport(0,quesId, new RequestParser());
    }

    @Override
    public void onPostExecute(int updateId, RequestBean result) {

    }
    @Override
    public void netNull() {

    }

    @Override
    public void netErr(int updateId, String errMsg) {
    }

    @Override
    public void dataNull(int updateId, String errMsg) {
    }
}
