package com.bjcathay.qt.util;

import android.content.Context;

import com.bjcathay.qt.R;
import com.bjcathay.qt.model.ShareModel;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * Created by bjcathay on 15-5-18.
 */
public class ShareUtil {
    private static ShareUtil shareUtil;

    public static void showShare(Context context) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(context.getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(context);
    }

    public static synchronized ShareUtil getInstance() {
        if (shareUtil == null) {
            shareUtil = new ShareUtil();
        }
        return shareUtil;
    }


    public void shareDemo(Context context, ShareModel shareModel) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle(shareModel.getTitle());
        oks.setTitleUrl(shareModel.getUrl());
        oks.setText(shareModel.getDescription());
        // oks.setImagePath(shareModel.getPicture());
        oks.setImagePath("");
        // oks.setImageUrl(shareModel.getImageUrl());
        oks.setUrl(shareModel.getUrl());
        oks.setFilePath("");
        oks.setComment("");
        oks.setSite(context.getString(R.string.app_name));
        oks.setSiteUrl(shareModel.getUrl());
        oks.setVenueName(context.getString(R.string.app_name));
        oks.setVenueDescription("This is a beautiful place!");
        oks.setSilent(true);
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        oks.setDialogMode();
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeImplement(context, shareModel.getTitle(), shareModel.getSmsContent()));
      //  if (PreferencesUtils.getBoolean(context, PreferencesConstant.SHARE_CLICK)==false) {
            oks.show(context);
       // }

    }
}