
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bjcathay.qt.R;
import com.bjcathay.qt.db.DBManager;
import com.bjcathay.qt.model.BModel;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * Created by dengt on 15-7-3.
 */
public class EditPlayerActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private BModel bookModel;
    private EditText name;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_edit_player_layout);
        name = ViewUtil.findViewById(this, R.id.edit_player_name);
        phone = ViewUtil.findViewById(this, R.id.edit_player_phone);
    }

    private void initEvent() {
        topView.setTitleText("编辑打球人信息");
        topView.setTitleBackVisiable();
        topView.setFinishVisiable();
    }

    private void initData() {
        Intent intent = getIntent();
        bookModel = (BModel) intent.getSerializableExtra("book");
        name.setText(bookModel.getName());
        phone.setText(bookModel.getPhone());

    }

    @Override
    public void onClick(View view) {
        String nameEdit = name.getText().toString().trim();
        String phoneEdit = phone.getText().toString().trim();
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.title_back_img:
               /* if (nameEdit.equals(bookModel.getName()) && phoneEdit.equals(bookModel.getPhone())) {
                } else if (!StringUtils.isEmpty(nameEdit)) {
                    BookModel newBook = new BookModel();
                    newBook.setName(nameEdit);
                    newBook.setPhone(phoneEdit);
                    DBManager.getInstance().updatePlayer(bookModel, newBook);
                    intent.putExtra("editnew", newBook);
                    intent.putExtra("editold", bookModel);
                }
                setResult(2, intent);*/
                finish();
                break;
            case R.id.title_finish:
                if (nameEdit.equals(bookModel.getName()) && phoneEdit.equals(bookModel.getPhone())) {

                } else if (!StringUtils.isEmpty(nameEdit)) {
                    BModel newBook = new BModel();
                    newBook.setName(nameEdit);
                    newBook.setPhone(phoneEdit);
                    DBManager.getInstance().updatePlayer(bookModel, newBook);
                    intent.putExtra("editnew", newBook);
                    intent.putExtra("editold", bookModel);
                }
                setResult(2, intent);
                finish();
                break;
        }
    }
}