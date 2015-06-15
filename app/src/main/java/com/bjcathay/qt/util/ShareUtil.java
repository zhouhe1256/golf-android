package com.bjcathay.qt.util;

import android.content.Context;

import com.bjcathay.qt.R;
import com.bjcathay.qt.activity.MainActivity;
import com.bjcathay.qt.model.ShareModel;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * Created by dengt on 15-5-18.
 */
public class ShareUtil {
    private static ShareUtil shareUtil;
    public static synchronized ShareUtil getInstance() {
        if (shareUtil == null) {
            shareUtil = new ShareUtil();
        }
        return shareUtil;
    }
    public void shareDemo(Context context, ShareModel shareModel) {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
       // oks.disableSSOWhenAuthorize();
        oks.setTitle(shareModel.getTitle());
        oks.setTitleUrl(shareModel.getUrl());
        oks.setText(shareModel.getDescription());
        // oks.setImagePath(shareModel.getPicture());
        oks.setImagePath(MainActivity.TEST_IMAGE);
        // oks.setImageUrl(shareModel.getImageUrl());
        oks.setUrl(shareModel.getUrl());
        oks.setFilePath("");
        oks.setComment("");
        oks.setSite(context.getString(R.string.app_name));
        oks.setSiteUrl(shareModel.getUrl());
        oks.setVenueName(context.getString(R.string.app_name));
        oks.setVenueDescription("7铁高尔夫!");
        oks.setSilent(true);
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        oks.setDialogMode();
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeImplement(context, shareModel.getTitle(), shareModel.getSmsContent()));
      //  if (PreferencesUtils.getBoolean(context, PreferencesConstant.SHARE_CLICK)==false) {
            oks.show(context);
       // }

    }
}
