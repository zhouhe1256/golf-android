
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.BookListModel;
import com.bjcathay.qt.model.BookModel;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.model.PackagePriceModel;
import com.bjcathay.qt.model.PriceModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.model.UserModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ValidformUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-7-1.
 */
public class OrderCommitActivity extends Activity implements View.OnClickListener {
    private TopView topView;
    private ProductModel stadiumModel;
    private Context context;
    private Button sure;
    private TextView name;
    private TextView time;
    private String date;
    String hourSelect;
    private int number;
    private PriceModel currentPrice;
    private PackagePriceModel comboPrice;
    private ImageView minas;
    private ImageView plus;
    private TextView fourPlus;
    private TextView price;
    private EditText phone;
    private EditText cName;
    private TextView palyerNames;
    private int amount = -1;
    private int amountmax = -1;
    private String players;
    private EditText commemt;
    private BookListModel bookListModel = new BookListModel();

    private int reqname = 1;
    private int respname = 2;

    private ProgressDialog dialog = null;
    private String contactName;
    private String contactPhone;
    private String playName;
    private String playNote;
    private LinearLayout timeLinear;
    private TextView totalprice;
    private TextView spotPrice;
    private TextView payType;
    private EditText playNameipt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_commit);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_order_commit_layout);
        name = ViewUtil.findViewById(this, R.id.commit_place_name);
        time = ViewUtil.findViewById(this, R.id.commit_place_date);
        minas = ViewUtil.findViewById(this, R.id.dialog_order_minas);
        plus = ViewUtil.findViewById(this, R.id.dialog_order_plus);
        fourPlus = ViewUtil.findViewById(this, R.id.dialog_order_sure_number_edit);
        price = ViewUtil.findViewById(this, R.id.dialog_order_sure_price);
        phone = ViewUtil.findViewById(this, R.id.dialog_order_sure_phone);
        cName = ViewUtil.findViewById(this, R.id.dialog_order_sure_name);
        sure = ViewUtil.findViewById(this, R.id.sure_order);
        palyerNames = ViewUtil.findViewById(this, R.id.select_player_names);
        timeLinear = ViewUtil.findViewById(this, R.id.time);
        totalprice = ViewUtil.findViewById(this, R.id.total_price);
        spotPrice = ViewUtil.findViewById(this, R.id.spotPrice);
        payType = ViewUtil.findViewById(this, R.id.pay_type);
        commemt = ViewUtil.findViewById(this, R.id.order_commit_comment);
        playNameipt = ViewUtil.findViewById(this, R.id.order_commit_player_names);
    }

    private void commitOrder() {
        contactPhone = phone.getText().toString().trim();
        contactName = cName.getText().toString().trim();
        playName=playNameipt.getText().toString().trim();
        playNote=commemt.getText().toString().trim();
        if (StringUtils.isEmpty(contactName)) {
            DialogUtil.showMessage("尚未填写联系人姓名");
            return;
        }
        if (StringUtils.isEmpty(contactPhone)) {
            DialogUtil.showMessage("尚未填写联系人电话");
            return;
        }
        if (!ValidformUtil.isMobileNo(contactPhone)) {
            DialogUtil.showMessage("请填写正确的手机号码");
            return;
        }
        if (dialog == null) {
            dialog = ProgressDialog.show(context, "", "正在提交订单，请稍等...", false);
            dialog.setCanceledOnTouchOutside(false);// 创建ProgressDialog
        }
        OrderModel
                .commitNewOrder("PRODUCT",stadiumModel.getId(), number, date, contactName, contactPhone,
                       playName,playNote, null).done(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        JSONObject jsonObject = arguments.get(0);
                        if (jsonObject.optBoolean("success")) {
                            OrderModel orderModel = JSONUtil.load(OrderModel.class,
                                    jsonObject.optJSONObject("order"));
                            dialog.dismiss();
                            DialogUtil.showMessage("下单成功");
                            Intent intent = new Intent(context, OrderDetailActivity.class);
                            intent.putExtra("imageurl", orderModel.getImageUrl());
                            intent.putExtra("id", orderModel.getId());
                            ViewUtil.startActivity(context, intent);

                            // if ("LIMIT".equals(stadiumModel.getType())
                            // || "NONE".equals(stadiumModel.getType())) {
                            // intent = new Intent(context,
                            // OrderSucActivity.class);
                            // } else if
                            // ("SPECIAL".equals(stadiumModel.getType())) {
                            // intent = new Intent(context,
                            // OrderSucTEActivity.class);
                            // } else if
                            // ("GROUP".equals(stadiumModel.getType())) {
                            // intent = new Intent(context,
                            // SelectPayWayActivity.class);
                            // intent.putExtra("order", orderModel);
                            // }
                            // intent.putExtra("id", orderModel.getId());
                            // ViewUtil.startActivity(context, intent);
                        } else {
                            dialog.dismiss();
                            String errorMessage = jsonObject.optString("message");
                            if (!StringUtils.isEmpty(errorMessage))
                                DialogUtil.showMessage(errorMessage);
                            else {
                                int code = jsonObject.optInt("code");
                                DialogUtil.showMessage(ErrorCode.getCodeName(code));
                            }
                        }
                    }
                }
                ).fail(new ICallback() {
                    @Override
                    public void call(Arguments arguments) {
                        dialog.dismiss();
                        DialogUtil.showMessage(getString(R.string.empty_net_text));
                    }
                }
                );
    }

    private void initEvent() {
        topView.setTitleText("填写订单");
        topView.setTitleBackVisiable();
        sure.setOnClickListener(this);
        commemt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    LogUtil.d("keyCode", "6666666666666666666666666666666");
                    if (commemt.getText().length() <= 4) {
                        commemt.setText("备注： ");
                        commemt.clearFocus();
                    } else {
                        commemt.requestFocus();
                    }
                }
                return false;
            }
        });

        if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
            timeLinear.setVisibility(View.VISIBLE);
        } else
            timeLinear.setVisibility(View.GONE);
        if (ProductType.payType.PREPAY.equals(stadiumModel.getPayType())) {
            payType.setText("(全额预付)");
        } else if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
            payType.setText("(部分预付)");
        } else if (ProductType.payType.SPOTPAY.equals(stadiumModel.getPayType())) {
            payType.setText("(全额现付)");
        }
    }

    private void initData() {
        Intent intent = getIntent();
        stadiumModel = (ProductModel) intent.getSerializableExtra("product");
        date = intent.getStringExtra("date");
        number = intent.getIntExtra("number", 1);
        currentPrice = (PriceModel) intent.getSerializableExtra("currentPrice");
        comboPrice = (PackagePriceModel) intent.getSerializableExtra("comboPrice");
        hourSelect = intent.getStringExtra("hourSelect");
        name.setText(stadiumModel.getName());
        time.setText(DateUtil.stringToDateToOrderString(date));
        UserModel userModel1 = GApplication.getInstance().getUser();
        phone.setText(userModel1.getMobileNumber());
        cName.setText(userModel1.getRealName());
        BookModel bookModel = new BookModel();
        if (userModel1.getRealName() == null) {
            bookModel.setName(userModel1.getMobileNumber() + "(本人)");
        } else {
            bookModel.setName(userModel1.getRealName() + "(本人)");
        }
        bookModel.setPhone(userModel1.getMobileNumber());
        List<BookModel> bookModels = new ArrayList<BookModel>();
        bookModels.add(bookModel);
        bookListModel.setPersons(bookModels);
        players = JSONUtil.dump(bookListModel);
        palyerNames.setText(bookModel.getName());
        minas.setVisibility(View.VISIBLE);
        plus.setVisibility(View.VISIBLE);
        if (ProductType.prdtType.COMBO.equals(stadiumModel.getType())) {
            getComboPrice();
            time.setText(DateUtil.stringToDateToOrderString(date).substring(0, 11));
        } else
            getProductPrice();
    }

    private void getComboPrice() {
        amount = comboPrice.getMinPerson();
        amountmax = comboPrice.getMaxPerson();
        if (number == 0) {
            number = 5;
            int[] priceStr = comboPrice.getFianlPrice(stadiumModel.getType(),
                    number, hourSelect);
            if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
                double prepay = priceStr[2] * number;
                double spotpay = priceStr[3] * number;
                price.setText("￥" + (int) Math.floor(prepay));
                totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                spotPrice.setText((int) Math.floor(spotpay) + "");
            } else
                price.setText("￥"
                        +
                        (int) Math.floor((priceStr[0] * number)));
            minas.setOnClickListener(new
                    View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (number > 5) {
                                number--;
                                int[] priceStr = comboPrice.getFianlPrice(stadiumModel.getType(),
                                        number, hourSelect);
                                fourPlus.setText(number < 10 ? " " + number : number + "");
                                if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
                                    double prepay = priceStr[2] * number;
                                    double spotpay = priceStr[3] * number;
                                    price.setText("￥" + (int) Math.floor(prepay));
                                    totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                                    spotPrice.setText((int) Math.floor(spotpay) + "");
                                } else
                                    price.setText("￥"
                                            +
                                            (int) Math
                                                    .floor((priceStr[0] * number)));
                            }
                        }
                    });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (number < amountmax) {
                        number++;
                        int[] priceStr = comboPrice.getFianlPrice(stadiumModel.getType(),
                                number, hourSelect);
                        fourPlus.setText(number < 10 ? " " + number : number + "");
                        if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
                            double prepay = priceStr[2] * number;
                            double spotpay = priceStr[3] * number;
                            price.setText("￥" + (int) Math.floor(prepay));
                            totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                            spotPrice.setText((int) Math.floor(spotpay) + "");
                        } else
                            price.setText("￥"
                                    +
                                    (int) Math.floor((priceStr[0] * number)));
                    }
                }
            });
        } else {
            int[] priceStr = comboPrice.getFianlPrice(stadiumModel.getType(),
                    number, hourSelect);
            minas.setOnClickListener(new
                    View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (amount > 0) {
                                if (number > amount) {
                                    number--;
                                    int[] priceStr = comboPrice.getFianlPrice(
                                            stadiumModel.getType(),
                                            number, hourSelect);
                                    fourPlus.setText(number < 10 ? " " + number : number + "");
                                    if (ProductType.payType.BLENDPAY.equals(stadiumModel
                                            .getPayType())) {
                                        double prepay = priceStr[2] * number;
                                        double spotpay = priceStr[3] * number;
                                        price.setText("￥" + (int) Math.floor(prepay));
                                        totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                                        spotPrice.setText((int) Math.floor(spotpay) + "");
                                    } else
                                        price.setText("￥"
                                                +
                                                (int) Math
                                                        .floor((priceStr[0] * number)));
                                }
                            } else {
                                if (number > 1) {
                                    number--;
                                    int[] priceStr = comboPrice.getFianlPrice(
                                            stadiumModel.getType(),
                                            number, hourSelect);
                                    fourPlus.setText(number < 10 ? " " + number : number + "");
                                    if (ProductType.payType.BLENDPAY.equals(stadiumModel
                                            .getPayType())) {
                                        double prepay = priceStr[2] * number;
                                        double spotpay = priceStr[3] * number;
                                        price.setText("￥" + (int) Math.floor(prepay));
                                        totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                                        spotPrice.setText((int) Math.floor(spotpay) + "");
                                    } else
                                        price.setText("￥"
                                                +
                                                (int) Math
                                                        .floor((priceStr[0] * number)));
                                }
                            }
                        }
                    });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (number < amountmax) {
                        number++;
                        int[] priceStr = comboPrice.getFianlPrice(stadiumModel.getType(),
                                number, hourSelect);
                        fourPlus.setText(number < 10 ? " " + number : number + "");
                        if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
                            double prepay = priceStr[2] * number;
                            double spotpay = priceStr[3] * number;
                            price.setText("￥" + (int) Math.floor(prepay));
                            totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                            spotPrice.setText((int) Math.floor(spotpay) + "");
                        } else
                            price.setText("￥"
                                    +
                                    (int) Math.floor((priceStr[0] * number)));
                    }
                }
            });
            fourPlus.setText(number < 10 ? " " + number : number + "");
            if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
                double prepay = priceStr[2] * number;
                double spotpay = priceStr[3] * number;
                price.setText("￥" + (int) Math.floor(prepay));
                totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                spotPrice.setText((int) Math.floor(spotpay) + "");
            } else
                price.setText("￥"
                        +
                        (int) Math.floor((priceStr[0] * number)));
        }
    }

    private void getProductPrice() {
        amount = currentPrice.getMinPerson();
        amountmax = currentPrice.getMaxPerson();
        if (number == 0) {
            number = 5;
            int[] priceStr = currentPrice.getFianlPrice(stadiumModel.getType(),
                    number, hourSelect);
            if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
                double prepay = priceStr[2] * number;
                double spotpay = priceStr[3] * number;
                price.setText("￥" + (int) Math.floor(prepay));
                totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                spotPrice.setText((int) Math.floor(spotpay) + "");
            } else
                price.setText("￥"
                        +
                        (int) Math.floor((priceStr[0] * number == 0 ? currentPrice.getPrice()
                                * number : priceStr[0]
                                * number)));
            minas.setOnClickListener(new
                    View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (number > 5) {
                                number--;
                                int[] priceStr = currentPrice.getFianlPrice(stadiumModel.getType(),
                                        number, hourSelect);
                                fourPlus.setText(number < 10 ? " " + number : number + "");
                                if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
                                    double prepay = priceStr[2] * number;
                                    double spotpay = priceStr[3] * number;
                                    price.setText("￥" + (int) Math.floor(prepay));
                                    totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                                    spotPrice.setText((int) Math.floor(spotpay) + "");
                                } else
                                    price.setText("￥"
                                            +
                                            (int) Math
                                                    .floor((priceStr[0] * number == 0 ? currentPrice
                                                            .getPrice()
                                                            * number
                                                            : priceStr[0]
                                                                    * number)));
                            }
                        }
                    });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (number < amountmax) {
                        number++;
                        int[] priceStr = currentPrice.getFianlPrice(stadiumModel.getType(),
                                number, hourSelect);
                        fourPlus.setText(number < 10 ? " " + number : number + "");
                        if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
                            double prepay = priceStr[2] * number;
                            double spotpay = priceStr[3] * number;
                            price.setText("￥" + (int) Math.floor(prepay));
                            totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                            spotPrice.setText((int) Math.floor(spotpay) + "");
                        } else
                            price.setText("￥"
                                    +
                                    (int) Math.floor((priceStr[0] * number == 0 ? currentPrice
                                            .getPrice() * number : priceStr[0]
                                            * number)));
                    }
                }
            });
        } else {
            int[] priceStr = currentPrice.getFianlPrice(stadiumModel.getType(),
                    number, hourSelect);
            minas.setOnClickListener(new
                    View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (amount > 0) {
                                if (number > amount) {
                                    number--;
                                    int[] priceStr = currentPrice.getFianlPrice(
                                            stadiumModel.getType(),
                                            number, hourSelect);
                                    fourPlus.setText(number < 10 ? " " + number : number + "");
                                    if (ProductType.payType.BLENDPAY.equals(stadiumModel
                                            .getPayType())) {
                                        double prepay = priceStr[2] * number;
                                        double spotpay = priceStr[3] * number;
                                        price.setText("￥" + (int) Math.floor(prepay));
                                        totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                                        spotPrice.setText((int) Math.floor(spotpay) + "");
                                    } else
                                        price.setText("￥"
                                                +
                                                (int) Math
                                                        .floor((priceStr[0] * number == 0 ? currentPrice
                                                                .getPrice()
                                                                * number
                                                                : priceStr[0]
                                                                        * number)));
                                }
                            } else {
                                if (number > 1) {
                                    number--;
                                    int[] priceStr = currentPrice.getFianlPrice(
                                            stadiumModel.getType(),
                                            number, hourSelect);
                                    fourPlus.setText(number < 10 ? " " + number : number + "");
                                    if (ProductType.payType.BLENDPAY.equals(stadiumModel
                                            .getPayType())) {
                                        double prepay = priceStr[2] * number;
                                        double spotpay = priceStr[3] * number;
                                        price.setText("￥" + (int) Math.floor(prepay));
                                        totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                                        spotPrice.setText((int) Math.floor(spotpay) + "");
                                    } else
                                        price.setText("￥"
                                                +
                                                (int) Math
                                                        .floor((priceStr[0] * number == 0 ? currentPrice
                                                                .getPrice()
                                                                * number
                                                                : priceStr[0]
                                                                        * number)));
                                }
                            }
                        }
                    });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (number < amountmax) {
                        number++;
                        int[] priceStr = currentPrice.getFianlPrice(stadiumModel.getType(),
                                number, hourSelect);
                        fourPlus.setText(number < 10 ? " " + number : number + "");
                        if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
                            double prepay = priceStr[2] * number;
                            double spotpay = priceStr[3] * number;
                            price.setText("￥" + (int) Math.floor(prepay));
                            totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                            spotPrice.setText((int) Math.floor(spotpay) + "");
                        } else
                            price.setText("￥"
                                    +
                                    (int) Math.floor((priceStr[0] * number == 0 ? currentPrice
                                            .getPrice() * number : priceStr[0]
                                            * number)));
                    }
                }
            });
            fourPlus.setText(number < 10 ? " " + number : number + "");
            if (ProductType.payType.BLENDPAY.equals(stadiumModel.getPayType())) {
                double prepay = priceStr[2] * number;
                double spotpay = priceStr[3] * number;
                price.setText("￥" + (int) Math.floor(prepay));
                totalprice.setText((int) Math.floor(prepay + spotpay) + "");
                spotPrice.setText((int) Math.floor(spotpay) + "");
            } else
                price.setText("￥"
                        +
                        (int) Math.floor((priceStr[0] * number == 0 ? currentPrice.getPrice()
                                * number : priceStr[0]
                                * number)));
        }
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.sure_order:
                commitOrder();
                break;
            case R.id.select_player_layout:
                intent = new Intent(this, SelectPlayerActivity.class);
                intent.putExtra("select", bookListModel);
                startActivityForResult(intent, reqname);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == reqname && resultCode == respname) {
            players = intent.getStringExtra("books");
            LogUtil.e("book", players);
            bookListModel = JSONUtil.load(BookListModel.class, players);
            List<BookModel> b = JSONUtil.load(BookListModel.class, players).getPersons();
            StringBuffer sb = new StringBuffer();
            for (BookModel bookModel : b) {
                sb.append(bookModel.getName() + ",");
            }
            if (!StringUtils.isEmpty(sb.toString()) && sb.length() > 1)
                palyerNames.setText(sb.substring(0, sb.length() - 1));
            else {
                UserModel userModel1 = GApplication.getInstance().getUser();
                BookModel bookModel = new BookModel();
                if (userModel1.getRealName() == null) {
                    bookModel.setName(userModel1.getMobileNumber() + "(本人)");
                } else {
                    bookModel.setName(userModel1.getRealName() + "(本人)");
                }
                bookModel.setPhone(userModel1.getMobileNumber());

                List<BookModel> bookModels = new ArrayList<BookModel>();
                bookModels.add(bookModel);
                bookListModel.setPersons(bookModels);
                players = JSONUtil.dump(bookListModel);
                palyerNames.setText(bookModel.getName());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MessageReceiver.baseActivity = this;
        MobclickAgent.onPageStart("完善订单页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("完善订单页面");
        MobclickAgent.onPause(this);
    }
}
