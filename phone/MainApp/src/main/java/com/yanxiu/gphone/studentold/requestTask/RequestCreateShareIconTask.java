package com.yanxiu.gphone.studentold.requestTask;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.common.share.utils.ShareUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
//import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;

import java.io.File;

/**
 * Created by lee on 16-4-5.
 */
public class RequestCreateShareIconTask extends AbstractAsyncTask {
    private Context mContext;
    public RequestCreateShareIconTask(Context context) {
        super(context);
        this.mContext=context;
    }

    @Override
    public void tokenInvalidate(String msg) {
    }

    @Override
    public YanxiuDataHull doInBackground() {
        final String filePath =YanXiuConstant.SHARE_ICON_PATH  + YanXiuConstant.SHARE_LOGO_NAME;
        File file = new File(filePath);
        if(!file.exists()){
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.share_app_icon);
            ShareUtils.saveShareBitmap(bitmap,YanXiuConstant.SHARE_ICON_PATH ,YanXiuConstant.SHARE_LOGO_NAME);
        }
        /*final String xlcgFilePath= YanxiuParentConstants.XUANLIANGCIGU_ICON_PATH;
        File xlcgFile=new File(xlcgFilePath);
        if(!xlcgFile.exists()){
            Bitmap bitmap=BitmapFactory.decodeStream(ShareUtils.getAsssetStream(YanxiuParentConstants.XUANLIANGCIFUICON));
            ShareUtils.saveShareBitmap(bitmap, YanXiuConstant.SHARE_ICON_PATH, YanxiuParentConstants.XUANLIANGCIFUICON);
        }

        final String lizlFilePath= YanxiuParentConstants.LIANZHANLIANJIE_ICON_PATH;
        File lzljFile=new File(lizlFilePath);
        if(!lzljFile.exists()){
            Bitmap bitmap= BitmapFactory.decodeStream(ShareUtils.getAsssetStream(YanxiuParentConstants.LIANZHANLIANJIEICON));
            ShareUtils.saveShareBitmap(bitmap, YanXiuConstant.SHARE_ICON_PATH, YanxiuParentConstants.LIANZHANLIANJIEICON);
        }

        final String wkbpFilePath= YanxiuParentConstants.WEIKUAIBUPO_ICON_PATH;
        File wkbpFile=new File(wkbpFilePath);
        if(!wkbpFile.exists()){
            Bitmap bitmap=BitmapFactory.decodeStream(ShareUtils.getAsssetStream(YanxiuParentConstants.WEIKUAIBUPOICON));
            ShareUtils.saveShareBitmap(bitmap, YanXiuConstant.SHARE_ICON_PATH, YanxiuParentConstants.WEIKUAIBUPOICON);
        }

        final String zlzyFilePath=YanxiuParentConstants.ZHULUZHONGYUAN_ICON_PATH;
        File zlzyFile=new File(zlzyFilePath);
        if(!zlzyFile.exists()){
            Bitmap bitmap=BitmapFactory.decodeStream(ShareUtils.getAsssetStream(YanxiuParentConstants.ZHULUZHONGYUANICON));
            ShareUtils.saveShareBitmap(bitmap, YanXiuConstant.SHARE_ICON_PATH, YanxiuParentConstants.ZHULUZHONGYUANICON);
        }*/

        return null;
    }

    @Override
    public void onPostExecute(int updateId, YanxiuBaseBean result) {

    }
}
