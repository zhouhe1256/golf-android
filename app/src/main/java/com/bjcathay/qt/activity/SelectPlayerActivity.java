
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.R;
import com.bjcathay.qt.adapter.PlayerAdapter;
import com.bjcathay.qt.db.DBManager;
import com.bjcathay.qt.model.BookListModel;
import com.bjcathay.qt.model.BookModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-7-3.
 */
public class SelectPlayerActivity extends Activity implements View.OnClickListener {
    private List<BookModel> callRecords;
    private Activity context;
    private TopView topView;
    private ListView playList;
    private PlayerAdapter playerAdapter;
    private EditText inputPlayer;
    private ImageView leftImg;
    private ImageView rightImg;

    private int reqedit = 1;
    private int respedit = 2;
    private int reqcontact = 3;
    private int respcontact = 4;
    private LayoutInflater inflater;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_player);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_select_player_layout);
        // callRecords = new ArrayList<BookModel>();
        playList = ViewUtil.findViewById(this, R.id.player_list);
        inflater = LayoutInflater.from(context);
        header = inflater.inflate(R.layout.select_player_list_head, null);
        inputPlayer = ViewUtil.findViewById(header, R.id.input_player_name);
        leftImg = ViewUtil.findViewById(header, R.id.left_img);
        rightImg = ViewUtil.findViewById(header, R.id.right_img);
        playList.addHeaderView(header);
    }

    private void initEvent() {
        topView.setTitleText("选择打球人");
        topView.setTitleBackVisiable();
        playList.setAdapter(playerAdapter);
        rightImg.setOnClickListener(this);
        leftImg.setOnClickListener(this);
    }

    private void initData() {
        callRecords = DBManager.getInstance().queryPlayers();
        playerAdapter = new PlayerAdapter(callRecords, context);
    }

    private void addPlayer() {
        String name = inputPlayer.getText().toString().trim();
        if (StringUtils.isEmpty(name)) {
            return;
        }
        for (BookModel a : callRecords) {
            if (a.getName().equals(name)) {
                DialogUtil.showMessage("已有该联系人");
                return;
            }
        }
        BookModel b = new BookModel();
        b.setName(name);
        DBManager.getInstance().addPlayer(b);
        callRecords.add(b);
        playerAdapter.notifyDataSetChanged();
        inputPlayer.setText("");
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_img:
                if (playerAdapter != null) {
                    BookListModel bookListModel = new BookListModel();
                    List<BookModel> bookModels = playerAdapter.getCheckedItems();
                    bookListModel.setPersons(bookModels);
                    intent = new Intent();
                    intent.putExtra("books", JSONUtil.dump(bookListModel));
                    setResult(2, intent);
                }

                finish();
                break;
            case R.id.right_img:
                addPlayer();
                break;
            case R.id.left_img:
                intent = new Intent(this, SelectContactActivity.class);
                // ViewUtil.startActivity(this, intent);
                startActivityForResult(intent, reqcontact);
                // DialogUtil.showMessage("打开通讯录");
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (playerAdapter != null) {
            List<BookModel> bookModels = playerAdapter.getCheckedItems();
            Intent intent = new Intent();
            intent.putExtra("books", JSONUtil.dump(bookModels));
            setResult(2, intent);
        }

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callRecords = DBManager.getInstance().queryPlayers();
        playerAdapter.uodateView(callRecords);
    }
}
