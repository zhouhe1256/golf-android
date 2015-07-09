
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bjcathay.qt.R;
import com.bjcathay.qt.db.DBManager;
import com.bjcathay.qt.model.BookModel;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ValidformUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * Created by dengt on 15-7-3.
 */
public class EditPlayerActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private BookModel bookModel;
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
        bookModel = (BookModel) intent.getSerializableExtra("book");
        name.setText(bookModel.getName());
        phone.setText(bookModel.getPhone());

    }

    private void finishEdit() {
        Intent intent = new Intent();
        String nameEdit = name.getText().toString().trim();
        String phoneEdit = phone.getText().toString().trim();
        if (!StringUtils.isEmpty(phoneEdit) && !ValidformUtil.isMobileNo(phoneEdit)) {
            DialogUtil.showMessage("请填写正确的手机号码");
            return;
        }
        if (nameEdit.equals(bookModel.getName()) && phoneEdit.equals(bookModel.getPhone())) {

        } else if (!StringUtils.isEmpty(nameEdit)) {
            BookModel newBook = new BookModel();
            newBook.setName(nameEdit);
            newBook.setPhone(phoneEdit);
            DBManager.getInstance().updatePlayer(bookModel, newBook);
            intent.putExtra("editnew", newBook);
            intent.putExtra("editold", bookModel);
        }
        setResult(2, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.title_finish:
                finishEdit();
                break;
        }
    }
}
