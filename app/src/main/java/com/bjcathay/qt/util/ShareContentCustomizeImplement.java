package com.bjcathay.qt.util;

import android.content.Context;

import com.bjcathay.mallfm.R;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by bjcathay on 15-1-22.
 */
public class ShareContentCustomizeImplement implements ShareContentCustomizeCallback {
    private String type;
    private String title;
    private String url;
    private Context context;

    public ShareContentCustomizeImplement(Context context, String type, String title, String text){
        this.context=context;
        this.type=type;
        this.title=title;
        this.url=text;
    }
    @Override
    public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
        if(platform instanceof CustomPlatform){
            return;
        }
        int id = ShareSDK.platformNameToId(platform.getName());
            /*if (MainActivity.TEST_TEXT != null && MainActivity.TEST_TEXT.containsKey(id)) {
               // String text = MainActivity.TEST_TEXT.get(id);
                paramsToShare.setText(text);
            } else if ("Twitter".equals(platform.getName())) {
                // 改写twitter分享内容中的text字段，否则会超长，
                // 因为twitter会将图片地址当作文本的一部分去计算长度
               // String text = platform.getContext().getString(R.string.share_content_short);
               // paramsToShare.setText(text);
            }*/
        if("ShortMessage".equals(platform.getName())){
            String text=String.format(context.getString(R.string.share_shortmessage_text,type,title,url));
            paramsToShare.setText(text);
            paramsToShare.setImagePath("");
            paramsToShare.setImageUrl("");
        }else if("SinaWeibo".equals(platform.getName())){
            String text=String.format(context.getString(R.string.share_shortmessage_text,type,title,url));
            paramsToShare.setText(text);
        }

    }
}
