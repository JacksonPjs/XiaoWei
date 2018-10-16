package com.xiaowei.ui.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.blibrary.utils.PermissionsManager;
import com.example.blibrary.utils.T;
import com.xiaowei.R;
import com.xiaowei.ui.activity.Guide.AdvertActivity;
import com.xiaowei.ui.activity.Guide.GuideActivity;
import com.xiaowei.ui.activity.Login.LoginActivity;
import com.xiaowei.utils.SharedPreferencesUtils;


public class StartActivity extends BaseActivity {
    private final int REQUEST_VIDEO_PERMISSION = 1;
    private final static String TAG = "StartActivity";
    Activity activity;
    Intent intent ;


    Handler mHandler = new Handler() {
        Intent intent = null;

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    intent = new Intent(StartActivity.this, AdvertActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    intent = new Intent(StartActivity.this, GuideActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }

        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SharedPreferencesUtils.getIsFirst(this);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
             intent = new Intent(this, GuideActivity.class);
            intent.putExtra("flag","start");
            startActivity(intent);
            finish();
            return;
        }else {
            // 如果不是第一次启动app，则正常显示启动屏


            setContentView(R.layout.activity_start);
            activity = this;
//        while (true){
//            String[] permissions = PermissionsManager.haveNoPermissions(this, PERMISSIONS);
//            if (permissions == null || permissions.length < 1) {
//                startMain();
//                break;
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null && permissions.length > 0) {
//                ActivityCompat.requestPermissions(this, permissions, REQUEST_VIDEO_PERMISSION);
//            }
//        }
            intent = new Intent(StartActivity.this, AdvertActivity.class);
            startActivity(intent);
            finish();
//            startMain();

        }


    }

    public void startMain() {
//        String[] permissions = PermissionsManager.haveNoPermissions(activity, PERMISSIONS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null && permissions.length > 0) {
//            ActivityCompat.requestPermissions(activity, permissions, REQUEST_VIDEO_PERMISSION);
//        }
//        String gest = SharedPreferencesUtils.getGesturePsw(this);
//
//        boolean isGest=SharedPreferencesUtils.getIsGesture(this);
//        if (gest != null&&isGest) {
//            mHandler.sendEmptyMessageDelayed(1, 2000);
//
//        } else {
                    mHandler.sendEmptyMessageDelayed(0, 2000);

//        }



    }

    private final String[] PERMISSIONS = {
            Manifest.permission.CALL_PHONE
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGrant = true;

        if (grantResults != null && grantResults.length > 0) {
            //有权限获取失败
            for (int i = 0; i < grantResults.length; i++) {
                //是否为设置了【不再询问】
                if (grantResults[i] == -1) {
                    isGrant = false;
                }
            }

            if (!isGrant) {
                T.ShowToastForLong(this, "权限获取异常");
                return;
            }
        }
        Log.i(TAG, "权限获取正常");
        finish();
        //彻底退出进程，防止出现授权后 仍然无法搜索蓝牙设备的问题
        System.exit(0);
    }

}
