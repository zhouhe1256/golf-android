package com.bjcathay.qt.lib;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.qt.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-5-31.
 */
public class TextActivity extends Activity implements WheelDate.OnOptionsSelectListener {
    WheelDate wheelDateoption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_text);
        final View optionspicker = findViewById(R.id.optionspicker);
        ScreenInfo screenInfo = new ScreenInfo((Activity) this);
        wheelDateoption = new WheelDate(optionspicker, this);
        wheelDateoption.screenheight = screenInfo.getHeight();
        List<String> a = new ArrayList<String>();
        a.add("1");
        a.add("2");
        a.add("3");
        a.add("4");
        List<String> b = new ArrayList<String>();
        b.add("1");
        b.add("2");
        b.add("3");
        b.add("4");
        List<String> c = new ArrayList<String>();
        c.add("1");
        c.add("2");
        c.add("3");
        c.add("4");
        List<String> d = new ArrayList<String>();
        d.add("1");
        d.add("2");
        d.add("3");
        d.add("4");
        wheelDateoption.setPicker(a, b, c, d, false);
    }

    @Override
    public void onOptionsSelect(String options1, String option2, String options3) {

    }
}
