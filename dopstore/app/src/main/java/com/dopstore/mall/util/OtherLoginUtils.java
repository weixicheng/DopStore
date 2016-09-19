package com.dopstore.mall.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 作者：xicheng on 16/9/18 13:44
 * 类别：
 */
public class OtherLoginUtils {
    private static Context context;
    private static final int MSG_AUTH_CANCEL = 0;
    private static final int MSG_AUTH_ERROR= 1;
    private static final int MSG_AUTH_COMPLETE = 2;
    private static Platform mPlatform;

    public OtherLoginUtils(Context context) {
        this.context=context;
    }

    public static void authorize(int type) {
        Platform plat=null;
        switch (type){
            case 0:{
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                plat=qq;
            }break;
            case 1:{
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                plat=wechat;
            }break;
            case 2:{
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                plat=sina;
            }break;
        }
        if (plat == null) {
            //popupOthers();
            return;
        }

        plat.setPlatformActionListener(listener);
        //关闭SSO授权
        plat.SSOSetting(true);
        plat.showUser(null);


    }


    static PlatformActionListener listener=new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
            if (action == Platform.ACTION_USER_INFOR) {
                Message msg = new Message();
                msg.what = MSG_AUTH_COMPLETE;
                msg.obj = new Object[] {platform.getName(), hashMap};
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onError(Platform platform, int action, Throwable t) {
            if (action == Platform.ACTION_USER_INFOR) {
                handler.sendEmptyMessage(MSG_AUTH_ERROR);
            }
            t.printStackTrace();
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (action == Platform.ACTION_USER_INFOR) {
                handler.sendEmptyMessage(MSG_AUTH_CANCEL);
            }
        }
    };

    static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case MSG_AUTH_CANCEL: {
                    //取消授权
                } break;
                case MSG_AUTH_ERROR: {
                    //授权失败
                } break;
                case MSG_AUTH_COMPLETE: {
                    //授权成功

                    Object[] objs = (Object[]) msg.obj;
                    String platform = (String) objs[0];
                    HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
//                  if (signupListener != null && signupListener.onSignin(platform, res)) {
//                      SignupPage signupPage = new SignupPage();
//                      signupPage.setOnLoginListener(signupListener);
//                      signupPage.setPlatform(platform);
//                      signupPage.show(activity, null);
//                  }
                    doLogined(platform);
                } break;
            }
        }
    };

    private static void doLogined(String platform) {
        // TODO Auto-generated method stub
        mPlatform= ShareSDK.getPlatform(platform);
        String gender = "";
        if(platform != null){
            gender = mPlatform.getDb().getUserGender();
            if(gender.equals("m")){
                //  userInfo.setUserGender(UserInfo.Gender.BOY);
                gender = "男";
            }else{
                //userInfo.setUserGender(UserInfo.Gender.GIRL);
                gender = "女";
            }

//              userInfo.setUserIcon(platform.getDb().getUserIcon());
//              userInfo.setUserName(platform.getDb().getUserName());
//              userInfo.setUserNote(platform.getDb().getUserId());
//    getUserId        Toast.makeText(getApplicationContext(), gender+"/"+mPlatform.getDb().getUserName()+"/"+mPlatform.getDb().getUserId(), 4000).show();
        }

//          tvUserName.setText(userInfo.getUserName());
//          tvUserGender.setText(gender);
//          tvUserNote.setText("USER ID : " + userInfo.getUserNote());
    }



}
