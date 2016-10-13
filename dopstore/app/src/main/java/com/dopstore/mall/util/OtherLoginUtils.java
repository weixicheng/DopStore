package com.dopstore.mall.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.dopstore.mall.onekeyshare.OnekeyShare;
import com.dopstore.mall.util.ShareData.ShareData;

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
    private static final int MSG_AUTH_ERROR = 1;
    private static final int MSG_AUTH_COMPLETE = 2;
    private static OtherCallBack callBack;

    public OtherLoginUtils(Context context) {
        this.context = context;
    }

    public static void authorize(int type) {
        Platform plat = null;
        switch (type) {
            case 0: {
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                plat = qq;
            }
            break;
            case 1: {
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                plat = wechat;
            }
            break;
            case 2: {
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                plat = sina;
            }
            break;
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

    static PlatformActionListener listener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
            if (action == Platform.ACTION_USER_INFOR) {
                Message msg = new Message();
                msg.what = MSG_AUTH_COMPLETE;
                msg.obj = new Object[]{platform.getName(), hashMap};
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

    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_AUTH_CANCEL: {
                    //取消授权
                    callBack.failed("取消授权");
                }
                break;
                case MSG_AUTH_ERROR: {
                    //授权失败
                    callBack.failed("授权失败");
                }
                break;
                case MSG_AUTH_COMPLETE: {
                    //授权成功
                    Object[] objs = (Object[]) msg.obj;
                    String platform = (String) objs[0];
                    callBack.success(platform);
                }
                break;
            }
        }
    };

    public void setCallBack(OtherCallBack callBack) {
        this.callBack = callBack;
    }

    public void showShare(Context context, ShareData shareData) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(shareData.getContent());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(shareData.getContent());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(shareData.getImage());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(shareData.getUrl());
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(shareData.getUrl());
        // 启动分享GUI
        oks.show(context);
    }


}
