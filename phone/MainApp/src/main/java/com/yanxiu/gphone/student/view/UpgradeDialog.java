package com.yanxiu.gphone.student.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.bean.InitializeBean;
import com.yanxiu.gphone.student.bean.NewInitializeBean;

/**
 * Created by Administrator on 2015/6/3.
 */
public class UpgradeDialog extends Dialog{

    private TextView mUpgradeTitleView;

    private TextView mUpgradeVersionView;

    private TextView mUpgradeCelView;

    private TextView mUpgradeSureView;

//    private TextView mUpgradeIgoneView;

    private NewInitializeBean initializeBean;

    private UpgradeDialogCallBack upgradeDialogCallBack;

    private Context mContext;
    private ProgressBar pb_loadapk;

    public UpgradeDialog(Context context, NewInitializeBean initializeBean,
            UpgradeDialogCallBack upgradeDialogCallBack) {
        super(context, R.style.alert_dialog_style);
        setOwnerActivity((Activity) context);
        mContext = context;
        this.initializeBean = initializeBean;
        this.upgradeDialogCallBack = upgradeDialogCallBack;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_popupwindow);
        pb_loadapk= (ProgressBar) findViewById(R.id.pb_loadapk);
        mUpgradeTitleView = (TextView)findViewById(R.id.upgrade_title);
        mUpgradeVersionView = (TextView)findViewById(R.id.upgrade_version);
        mUpgradeCelView = (TextView)findViewById(R.id.upgrade_layout_cel);
        mUpgradeSureView = (TextView)findViewById(R.id.upgrade_layout_sure);
//        mUpgradeIgoneView = (TextView)findViewById(R.id.upgrade_layout_inoge);
        if(TextUtils.isEmpty(initializeBean.getTitle())) {
            mUpgradeTitleView.setText(mContext.getResources().getString(R.string.app_upgrade, initializeBean.getVersion()));
        } else {
            mUpgradeTitleView.setText(initializeBean.getTitle());
        }
        mUpgradeVersionView.setText(initializeBean.getContent());
        if("1".equals(initializeBean.getUpgradetype())){ //"1"强制升级
            YanxiuApplication.getInstance().setIsForceUpdate(true);
            this.setCancelable(false);
            mUpgradeCelView.setText(R.string.app_upgrade_exit);
            mUpgradeCelView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (upgradeDialogCallBack != null) {
                        upgradeDialogCallBack.exit();
                    }
                }
            });
         /*   mUpgradeIgoneView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (upgradeDialogCallBack != null) {
                        upgradeDialogCallBack.exit();
                    }
                }
            });*/
            mUpgradeSureView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (upgradeDialogCallBack != null) {
                        pb_loadapk.setVisibility(View.VISIBLE);
                        upgradeDialogCallBack.upgrade();
                    }
                }
            });
        }else if("2".equals(initializeBean.getUpgradetype())) {  //"2"非强制升级
            mUpgradeCelView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    dismiss();
                    if (upgradeDialogCallBack != null) {
                        upgradeDialogCallBack.cancel();
                    }
                }
            });
            /*mUpgradeIgoneView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (upgradeDialogCallBack != null) {
                        upgradeDialogCallBack.cancel();
                    }
                }
            });*/
            mUpgradeSureView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (upgradeDialogCallBack != null) {
                        upgradeDialogCallBack.upgrade();
                        dismiss();
                    }
                }
            });
        }
    }

    public void setProgress(int progress){
        if (pb_loadapk!=null) {
            pb_loadapk.setProgress(progress);
        }
    }

    public interface UpgradeDialogCallBack{
        void upgrade();
        void cancel();
        void exit();
    }
}
