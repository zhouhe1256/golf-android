
package com.bjcathay.qt.model;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-9-14.
 */
public class TextInfo {
    public TextInfo(int index, String text, boolean isSelected) {
        mIndex = index;
        mText = text;
        mIsSelected = isSelected;

        if (isSelected) {
            mColor = Color.BLUE;
        }
    }

    public TextInfo() {

    }

    public int mIndex;
    public String mText;
    public boolean mIsSelected = false;
    public int mColor = Color.BLACK;
   public  List<TextInfo> textInfos=new ArrayList<TextInfo>();

    public void addTextInfo(TextInfo textInfo) {
        if (!textInfos.contains(textInfo))
            textInfos.add(textInfo);
    }

    @Override
    public boolean equals(Object obj) {
        TextInfo t = (TextInfo) obj;
        return mIndex == t.mIndex;
    }
}
