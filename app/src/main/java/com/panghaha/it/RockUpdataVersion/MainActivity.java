package com.panghaha.it.RockUpdataVersion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private UpdataDialog updataDialog;
    private  String versionName = "";
    private  int versioncode;
    private String oldVersion,NewVersion,versionmsg,url,channelid;
    //这里是测试用 登录检测版本号的服务器地址
    private String URL_UpdataVersion = "http://123.56.97.229:6080/Server/user/version.do";
    private TextView tvmsg,tvcode;
    private Data_updataVserion updataVserion;

    private Button show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //初始化弹窗 布局 点击事件的id
        updataDialog = new UpdataDialog(this,R.layout.dialog_updataversion,
                new int[]{R.id.dialog_sure});

        oldVersion =  getAppVersionName(this);

        InitData();

        show = (Button) findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updataDialog.show();
            }
        });


    }

    private void InitData() {
        try {
            OkHttpUtils.get(URL_UpdataVersion)
                    .params("ostype","1")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            updataVserion = JsonUtil.parseJsonToBean(s,Data_updataVserion.class);
                            NewVersion = updataVserion.getVersion();
                            versionmsg = updataVserion.getMess();
                            url = updataVserion.getAddress();
                            String[] n = versionmsg.split("-");
//                            for (int i = 0; i < n.length; i++) {
//                                versionmsg += n[i]+"\n";
//                            }
                            versionmsg = n[0]+"\n"+n[1]+"\n"+n[2];
                            if (!NewVersion.equals(oldVersion)){/**新旧版本号对比*/
                                updataDialog.show();
                                tvmsg = (TextView) updataDialog.findViewById(R.id.updataversion_msg);
                                tvcode = (TextView) updataDialog.findViewById(R.id.updataversioncode);
                                tvcode.setText(NewVersion);
                                tvmsg.setText(versionmsg);
                                updataDialog.setOnCenterItemClickListener(new UpdataDialog.OnCenterItemClickListener() {
                                    @Override
                                    public void OnCenterItemClick(UpdataDialog dialog, View view) {
                                        switch (view.getId()){
                                            case R.id.dialog_sure:
                                                /**调用系统自带的浏览器去下载最新apk*/
                                                Intent intent= new Intent();
                                                intent.setAction("android.intent.action.VIEW");
                                                Uri content_url = Uri.parse(url);
                                                intent.setData(content_url);
                                                startActivity(intent);
                                                break;
                                        }
                                        updataDialog.dismiss();
                                    }
                                });

                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 返回当前程序版本名  build.gradle里的
     */
    public  String getAppVersionName(Context context) {
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            LogUtil.d("versionName:---"+versionName,"versioncode:---"+versioncode);
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
