package com.bjcathay.qt.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.bjcathay.qt.R;

/**
 * Created by bjcathay on 15-5-21.
 */
public class ShareActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_back:
                finish();
                break;
        }
    }
}
