package com.tencent.qcloud.suixinbo.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.tencent.TIMManager;
import com.tencent.av.sdk.AVContext;
import com.tencent.qalsdk.QALSDKManager;
import com.tencent.qcloud.suixinbo.QavsdkApplication;
import com.tencent.qcloud.suixinbo.R;
import com.tencent.qcloud.suixinbo.model.MySelfInfo;
import com.tencent.qcloud.suixinbo.utils.SxbLog;
import com.tencent.qcloud.suixinbo.views.customviews.CustomSwitch;
import com.tencent.qcloud.suixinbo.views.customviews.LineControllerView;

/**
 * Created by admin on 2016/5/13.
 */
public class SetActivity extends Activity implements View.OnClickListener{
    private final static String TAG = "SetActivity";
    private CustomSwitch csAnimator;
    private LineControllerView lcvLog;
    private LineControllerView lcvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        QavsdkApplication.getInstance().addActivity(this);

        initView();
    }

    @Override
    protected void onDestroy() {
        QavsdkApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }

    private void initView(){
        csAnimator = (CustomSwitch)findViewById(R.id.cs_animator);
        lcvLog = (LineControllerView)findViewById(R.id.lcv_set_log_level);
        lcvVersion = (LineControllerView)findViewById(R.id.lcv_set_version);

        lcvLog.setContent(MySelfInfo.getInstance().getLogLevel().toString());

        csAnimator.setChecked(MySelfInfo.getInstance().isbLiveAnimator(), false);
    }

    private void changeLogLevel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(SxbLog.getStringValues(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int which) {
                MySelfInfo.getInstance().setLogLevel(SxbLog.SxbLogLevel.values()[which]);
                SxbLog.setLogLevel(MySelfInfo.getInstance().getLogLevel());
                lcvLog.setContent(MySelfInfo.getInstance().getLogLevel().toString());
                MySelfInfo.getInstance().writeToCache(SetActivity.this);
            }
        });
        builder.show();
    }

    private void showSDKVersion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("IM SDK: "+ TIMManager.getInstance().getVersion()+"\r\n"
            +"QAL SDK: "+ QALSDKManager.getInstance().getSdkVersion()+"\r\n"
            +"AV SDK: "+ AVContext.getVersion());
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.cs_animator:
            MySelfInfo.getInstance().setbLiveAnimator(!MySelfInfo.getInstance().isbLiveAnimator());
            MySelfInfo.getInstance().writeToCache(this);
            csAnimator.setChecked(MySelfInfo.getInstance().isbLiveAnimator(), true);
            break;
        case R.id.lcv_set_log_level:
            changeLogLevel();
            break;
        case R.id.lcv_set_version:
            showSDKVersion();
            break;
        }
    }
}
