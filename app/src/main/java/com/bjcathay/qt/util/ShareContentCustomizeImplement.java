
package com.bjcathay.qt.util;

import android.content.Context;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by dengt on 15-1-22.
 */
public class ShareContentCustomizeImplement implements ShareContentCustomizeCallback {

    private String title;
    private String text;
    private Context context;

    public ShareContentCustomizeImplement(Context context, String title, String text) {
        this.context = context;
        this.title = title;
        this.text = text;
    }

    @Override
    public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
        if (platform instanceof CustomPlatform) {
            return;
        }
        int id = ShareSDK.platformNameToId(platform.getName());
        if ("ShortMessage".equals(platform.getName())) {
            paramsToShare.setText(text);
            paramsToShare.setImagePath("");
            paramsToShare.setImageUrl("");
        } else if ("SinaWeibo".equals(platform.getName())) {
            // platform.removeAccount(true);
            paramsToShare.setText(text);
        } else if ("Email".equals(platform.getName())) {
            paramsToShare.setText(text);

        }

    }
}
