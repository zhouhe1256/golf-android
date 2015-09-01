
package com.bjcathay.qt.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bjcathay.android.async.Arguments;
import com.bjcathay.android.async.ICallback;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.view.ImageViewAdapter;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.constant.ErrorCode;
import com.bjcathay.qt.model.OrderModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.model.ShareModel;
import com.bjcathay.qt.receiver.MessageReceiver;
import com.bjcathay.qt.util.ClickUtil;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.DialogUtil;
import com.bjcathay.qt.util.ShareUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.view.CancleInfoDialog;
import com.bjcathay.qt.view.DeleteInfoDialog;
import com.bjcathay.qt.view.RichTextView;
import com.bjcathay.qt.view.RoundCornerImageView;
import com.bjcathay.qt.view.TopView;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by dengt on 15-4-29.
 */
public class OrderDetailActivity extends Activity implements ICallback, View.OnClickListener,
        DeleteInfoDialog.DeleteInfoDialogResult, CancleInfoDialog.CancleInfoDialogResult {

    private Activity context;
    private TopView topView;
    private Long id = 0l;
    private TextView orderName;
    private TextView orderSale;
    private TextView orderConDate;
    private TextView orderConNum;
    private TextView orderPrice;
    private TextView orderPay;
    private TextView orderPhone;
    private TextView orderNum;
    private TextView orderPayDate;
    private TextView orderStatus;
    private OrderModel orderModel;
    private Button orderToPay;
    private Button contactUs;
    private Button cancleOrder;
    private LinearLayout orderUndel;
    private TextView orderDel;
    private ShareModel shareModel;
    private TextView userRealName;
    private TextView personNames;
    private TextView purchasingNotice;
    private RoundCornerImageView orderImg;
    private TextView fanxian;
    private TextView orderMessageNote;
    private TextView dataNote;
    private TextView numberNote;
    private TextView serviceNote;

    private TextView peopleNote;
    private TextView shouldPayFact;// payType
    private TextView payType;
    private TextView paywallet;
    private LinearLayout orderStatusGone;
    private LinearLayout useWallet;
    private LinearLayout notice_linear;
    private LinearLayout schNotice;
    private LinearLayout schNoticeNumber;
    private LinearLayout contains;
    private TextView comboNumber;
    private WebView richTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_detail);
        context = this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        topView = ViewUtil.findViewById(this, R.id.top_order_detail_layout);
        orderName = ViewUtil.findViewById(this, R.id.order_detail_name);
        orderSale = ViewUtil.findViewById(this, R.id.order_detail_sale);
        orderConDate = ViewUtil.findViewById(this, R.id.order_detail_comsumer_date);
        orderConNum = ViewUtil.findViewById(this, R.id.order_detail_comsumer_numb);
        orderPrice = ViewUtil.findViewById(this, R.id.order_detail_price);
        orderPay = ViewUtil.findViewById(this, R.id.order_detail_pay);
        orderPhone = ViewUtil.findViewById(this, R.id.order_detail_phone);
        orderNum = ViewUtil.findViewById(this, R.id.order_detail_nomber);
        orderPayDate = ViewUtil.findViewById(this, R.id.order_detail_pay_date);
        orderStatus = ViewUtil.findViewById(this, R.id.order_detail_status);
        orderToPay = ViewUtil.findViewById(this, R.id.order_detail_now_pay);
        orderUndel = ViewUtil.findViewById(this, R.id.order_undelete_note);
        orderDel = ViewUtil.findViewById(this, R.id.order_delete_note);
        contactUs = ViewUtil.findViewById(this, R.id.contact_us);
        cancleOrder = ViewUtil.findViewById(this, R.id.order_cancle);
        userRealName = ViewUtil.findViewById(this, R.id.userRealName);
        personNames = ViewUtil.findViewById(this, R.id.personNames);
        purchasingNotice = ViewUtil.findViewById(this, R.id.purchasingNotice);
        orderImg = ViewUtil.findViewById(this, R.id.my_order_img);
        fanxian = ViewUtil.findViewById(this, R.id.order_detail_price_back);
        // todo combo
        orderMessageNote = ViewUtil.findViewById(this, R.id.order_message_note);
        dataNote = ViewUtil.findViewById(this, R.id.order_detail_comsumer_date_note);
        numberNote = ViewUtil.findViewById(this, R.id.order_detail_comsumer_numb_note);
        serviceNote = ViewUtil.findViewById(this, R.id.order_detail_sale_note);

        peopleNote = ViewUtil.findViewById(this, R.id.company_with_note);
        shouldPayFact = ViewUtil.findViewById(this, R.id.should_pay_fact);
        orderStatusGone = ViewUtil.findViewById(this, R.id.order_status_gone);
        payType = ViewUtil.findViewById(this, R.id.order_detail_price_type);
        useWallet = ViewUtil.findViewById(this, R.id.use_wallet);
        paywallet = ViewUtil.findViewById(this, R.id.order_detail_pay_wallet);

        notice_linear = ViewUtil.findViewById(this, R.id.notice_linear);
        schNotice = ViewUtil.findViewById(this, R.id.schNotice);
        schNoticeNumber = ViewUtil.findViewById(this, R.id.combo_number);
        comboNumber = ViewUtil.findViewById(this, R.id.combo_personnumber);
        richTextView = ViewUtil.findViewById(this, R.id.purchasing);
        contains = ViewUtil.findViewById(this, R.id.order_contains);
    }

    private void initEvent() {
        topView.setTitleBackVisiable();
        topView.setTitleText("订单详情");
    }

    private void initData() {
        Intent intent = getIntent();
        if ("message".equals(intent.getAction())) {
            orderModel = (OrderModel) intent.getSerializableExtra("orderModel");
            initViewData();
        } else {
            id = intent.getLongExtra("id", 0);
            OrderModel.orderDetail(id).done(this).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    orderDel.setVisibility(View.VISIBLE);
                    orderUndel.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void call(Arguments arguments) {
        orderModel = arguments.get(0);
        initViewData();
    }

    private void initViewData() {
        if (orderModel != null) {
            topView.setShareVisiable();
            orderName.setText(orderModel.getTitle());

            ImageViewAdapter.adapt(orderImg, orderModel.getImageUrl(), R.drawable.exchange_default);
            orderConDate.setText("" + DateUtil.stringToDateToOrderString(orderModel.getDate()));

            if (orderModel.getPeopleNumber() == 0)
                orderPrice.setText("￥" + (int) Math.floor(orderModel.getTotalPrice()) + "+");
            else
                orderPrice.setText("￥" + (int) Math.floor(orderModel.getTotalPrice()));
            orderPay.setText("￥" + (int) Math.floor(orderModel.getPrepayMoney()));
            if (ProductType.payType.PREPAY.equals(orderModel.getPayType())) {
                payType.setText("(全额预付)");
            } else if (ProductType.payType.BLENDPAY.equals(orderModel.getPayType())) {
                payType.setText("(部分预付)");
            } else if (ProductType.payType.SPOTPAY.equals(orderModel.getPayType())) {
                payType.setText("(全额现付)");
            }
            if (orderModel.getBalancePayMoney() == 0) {
                useWallet.setVisibility(View.GONE);
            } else {
                useWallet.setVisibility(View.VISIBLE);
                paywallet.setText("￥" + (int) Math.floor(orderModel.getBalancePayMoney()));
            }
            if(ProductType.prdtType.EVENT.equals(orderModel.getType())){
                contains.setVisibility(View.GONE);
                personNames.setText(orderModel.getCompanion());
                orderMessageNote.setText("赛事信息");
                cancleOrder.setVisibility(View.GONE);
            }else{
                contains.setVisibility(View.VISIBLE);
                personNames.setText(orderModel.getCompanion());
                cancleOrder.setVisibility(View.VISIBLE);
            }
            orderPhone.setText(orderModel.getMobileNumber());
            orderNum.setText("" + orderModel.getOrderId());
            orderPayDate
                    .setText("" + DateUtil.stringToDateToOrderString(orderModel.getCreatedAt()));
          //  personNames.setText(orderModel.getPersonNames());
            userRealName.setText(orderModel.getUserRealName());

            purchasingNotice.setText(orderModel.getPurchasingNotice());
            if (orderModel.getFan() != 0) {
                fanxian.setVisibility(View.VISIBLE);
                fanxian.setText("返" + (int) Math.floor(orderModel.getFan()) + "");
            } else {
                fanxian.setVisibility(View.GONE);
            }
            switch (orderModel.getType()) {
                case COMBO:
                    orderMessageNote.setText("旅游信息");
                    dataNote.setText("出发时间:");
                    numberNote.setText("返回日期:");
                    serviceNote.setText("出发城市:");
                    peopleNote.setText("同行人信息");
                    schNoticeNumber.setVisibility(View.VISIBLE);
                    comboNumber.setText(orderModel.getPeopleNumber() + "人");
                    schNotice.setVisibility(View.VISIBLE);
                    notice_linear.setVisibility(View.GONE);
                    orderConDate.setText("" + DateUtil.shortDateString(orderModel.getDate()));
                    richTextView.loadDataWithBaseURL(null, orderModel.getScheduling().replaceAll("font-size:.*pt;", "font-size:0pt;").replaceAll("font-family:.*;", "font-family:;"), "text/html", "UTF-8", null);
                    break;
                default:
                    schNotice.setVisibility(View.GONE);
                    notice_linear.setVisibility(View.VISIBLE);
                    orderConNum.setText(""
                            + (orderModel.getPeopleNumber() == 0 ? "4人+"
                                    : (orderModel.getPeopleNumber() + "人")));
                    orderSale.setText("" + orderModel.getPriceInclude());
                    break;
            }
            cancleOrder.setOnClickListener(this);
            if ("PENDING".equals(orderModel.getStatus())) {
                orderToPay.setVisibility(View.GONE);
                orderStatus.setText("确认中");
                cancleOrder.setBackgroundResource(R.drawable.yellow_stroke_bg);
                cancleOrder.setTextColor(getResources().getColor(R.color.order_price_color));
              //  cancleOrder.setVisibility(View.VISIBLE);
                shouldPayFact.setText("应付金额:");
            } else if ("UNPAID".equals(orderModel.getStatus())) {
                shouldPayFact.setText("应付金额:");
                orderToPay.setVisibility(View.VISIBLE);
                orderStatus.setText("待支付");
                cancleOrder.setBackgroundResource(R.drawable.yellow_stroke_bg);
                cancleOrder.setTextColor(getResources().getColor(R.color.order_price_color));
              //  cancleOrder.setVisibility(View.VISIBLE);
            } else if ("PAID".equals(orderModel.getStatus())) {
                shouldPayFact.setText("实付金额:");
                orderToPay.setVisibility(View.GONE);
                orderStatus.setText("已支付");
              //  cancleOrder.setVisibility(View.VISIBLE);
                cancleOrder.setBackgroundResource(R.drawable.gray_stroke_bg);
                cancleOrder.setTextColor(Color.GRAY);
                cancleOrder.setOnClickListener(null);
                // if (orderModel.getPeopleNumber() == 0)
                // orderPay.setText("￥" + (int)
                // Math.floor(orderModel.getTotalPrice()) + "+");
                // else
                // orderPay.setText("￥" + (int)
                // Math.floor(orderModel.getTotalPrice()));
            } else if ("FINISH".equals(orderModel.getStatus())) {
                orderToPay.setVisibility(View.GONE);
                shouldPayFact.setText("实付金额:");
                orderStatus.setText("已完成");
                cancleOrder.setBackgroundResource(R.drawable.gray_stroke_bg);
                cancleOrder.setTextColor(Color.GRAY);
                cancleOrder.setOnClickListener(null);
              //  cancleOrder.setVisibility(View.VISIBLE);
                // if (orderModel.getPeopleNumber() == 0)
                // orderPay.setText("￥" + (int)
                // Math.floor(orderModel.getTotalPrice()) + "+");
                // else
                // orderPay.setText("￥" + (int)
                // Math.floor(orderModel.getTotalPrice()));
            } else if ("CANCEL".equals(orderModel.getStatus())) {
                orderStatusGone.setVisibility(View.GONE);
                orderToPay.setVisibility(View.GONE);
                orderStatus.setText("已取消");
                cancleOrder.setBackgroundResource(R.drawable.gray_stroke_bg);
                cancleOrder.setTextColor(Color.GRAY);
                cancleOrder.setOnClickListener(null);
               // cancleOrder.setVisibility(View.VISIBLE);
            } else if ("CONFIRMED".equals(orderModel.getStatus())) {
                // shouldPayFact.setText("实付金额:");
                orderStatusGone.setVisibility(View.GONE);
                orderToPay.setVisibility(View.GONE);
                orderStatus.setText("已确认");
                // cancleOrder.setVisibility(View.VISIBLE);
                cancleOrder.setBackgroundResource(R.drawable.gray_stroke_bg);
                cancleOrder.setTextColor(Color.GRAY);
                cancleOrder.setOnClickListener(null);
            }
            orderToPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SelectPayWayActivity.class);
                    intent.putExtra("order", orderModel);
                    ViewUtil.startActivity(context, intent);
                }
            });

            contactUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteInfoDialog infoDialog = new DeleteInfoDialog(OrderDetailActivity.this,
                            R.style.InfoDialog, getResources()
                                    .getString(R.string.service_tel_format).toString().trim(),
                            "呼叫", 0l, OrderDetailActivity.this);
                    infoDialog.show();
                }
            });
            // todo
            // orderToPay.setVisibility(View.VISIBLE);
        }

    }

    private void cancleOrder() {
        CancleInfoDialog infoDialog = new CancleInfoDialog(OrderDetailActivity.this,
                R.style.InfoDialog, "确认取消订单?", 0l, OrderDetailActivity.this);
        infoDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.schNotice:
                ProductModel.product(orderModel.getProductId())
                        .done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                JSONObject jsonObject = arguments.get(0);
                                if (jsonObject.optBoolean("success")) {
                                    ProductModel productModel = JSONUtil.load(ProductModel.class,
                                            jsonObject.optJSONObject("product"));
                                    // ProductModel productModel =
                                    // arguments.get(0);
                                    Intent intent = new Intent(OrderDetailActivity.this,
                                            PackageDetailActivity.class);
                                    intent.putExtra("id", orderModel.getProductId());
                                    intent.putExtra("name", orderModel.getTitle());
                                    intent.putExtra("product", productModel);
                                    intent.putExtra("sch", true);
                                    ViewUtil.startActivity(OrderDetailActivity.this, intent);
                                } else {
                                    String errorMessage = jsonObject.optString("message");
                                    int code = jsonObject.optInt("code");
                                    if (code == 13005) {
                                        Intent intent = new Intent(OrderDetailActivity.this,
                                                ProductOfflineActivity.class);
                                        intent.putExtra("name", orderModel.getTitle());
                                        ViewUtil.startActivity(OrderDetailActivity.this, intent);
                                    } else {
                                        if (!StringUtils.isEmpty(errorMessage))
                                            DialogUtil.showMessage(errorMessage);
                                        else {

                                            DialogUtil.showMessage(ErrorCode.getCodeName(code));
                                        }
                                    }
                                }
                            }
                        }).fail(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                DialogUtil.showMessage(getString(R.string.empty_net_text));
                            }
                        });
                break;
            case R.id.title_share_img:
                if (orderModel != null)
                    if (shareModel == null)
                        ShareModel.shareOrders(orderModel.getId()).done(new ICallback() {
                            @Override
                            public void call(Arguments arguments) {
                                shareModel = arguments.get(0);
                                ShareUtil.getInstance().shareDemo(context, shareModel);
                            }
                        });
                    else
                        ShareUtil.getInstance().shareDemo(context, shareModel);
                break;
            case R.id.order_cancle:
                cancleOrder();
                break;
            case R.id.order_detail_to_place:
                Intent intent;
                switch (orderModel.getType()) {
                    case COMBO:
                        ProductModel.product(orderModel.getProductId())
                                .done(new ICallback() {
                                    @Override
                                    public void call(Arguments arguments) {
                                        JSONObject jsonObject = arguments.get(0);
                                        if (jsonObject.optBoolean("success")) {
                                            ProductModel productModel = JSONUtil.load(
                                                    ProductModel.class,
                                                    jsonObject.optJSONObject("product"));
                                            Intent intent = new Intent(OrderDetailActivity.this,
                                                    PackageDetailActivity.class);
                                            intent.putExtra("id", orderModel.getProductId());
                                            intent.putExtra("name", orderModel.getTitle());
                                            intent.putExtra("product", productModel);
                                            ViewUtil.startActivity(OrderDetailActivity.this, intent);
                                        } else {
                                            String errorMessage = jsonObject.optString("message");
                                            int code = jsonObject.optInt("code");
                                            if (code == 13005) {
                                                Intent intent = new Intent(
                                                        OrderDetailActivity.this,
                                                        ProductOfflineActivity.class);
                                                intent.putExtra("name", orderModel.getTitle());
                                                ViewUtil.startActivity(OrderDetailActivity.this,
                                                        intent);
                                            } else {
                                                if (!StringUtils.isEmpty(errorMessage))
                                                    DialogUtil.showMessage(errorMessage);
                                                else {

                                                    DialogUtil.showMessage(ErrorCode
                                                            .getCodeName(code));
                                                }
                                            }
                                        }
                                    }
                                }).fail(new ICallback() {
                                    @Override
                                    public void call(Arguments arguments) {
                                        DialogUtil.showMessage(getString(R.string.empty_net_text));
                                    }
                                });
                        break;
                    case REAL_TIME:
                        intent = new Intent(OrderDetailActivity.this,
                                RealTOrderActivity.class);
                        intent.putExtra("id", orderModel.getProductId());
                        intent.putExtra("imageurl", orderModel.getImageUrl());
                        ViewUtil.startActivity(OrderDetailActivity.this, intent);
                        break;
                    case EVENT:
                        intent = new Intent(OrderDetailActivity.this,
                                EventDetailActivity.class);
                        intent.putExtra("id", orderModel.getProductId());
                        ViewUtil.startActivity(OrderDetailActivity.this, intent);
                        break;
                    default:
                        intent = new Intent(OrderDetailActivity.this,
                                OrderStadiumDetailActivity.class);
                        intent.putExtra("id", orderModel.getProductId());
                        intent.putExtra("imageurl", orderModel.getImageUrl());
                        intent.putExtra("name", orderModel.getTitle());
                        ViewUtil.startActivity(OrderDetailActivity.this, intent);
                        break;
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (id != 0)
            OrderModel.orderDetail(id).done(this).fail(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    orderDel.setVisibility(View.VISIBLE);
                    orderUndel.setVisibility(View.GONE);

                }
            });
        MessageReceiver.baseActivity = this;
        MobclickAgent.onPageStart("订单详情页面");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("订单详情页面");
        MobclickAgent.onPause(this);
    }

    @Override
    public void deleteResult(Long targetId, boolean isDelete) {
        if (isDelete) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                    + getResources().getString(R.string.service_tel).toString().trim()));
            this.startActivity(intent);
        }
    }

    @Override
    public void cancleResult(Long targetId, boolean isDelete) {
        if (isDelete)
            OrderModel.orderCancle(orderModel.getId()).done(new ICallback() {
                @Override
                public void call(Arguments arguments) {
                    JSONObject jsonObject = arguments.get(0);
                    if (jsonObject.optBoolean("success")) {
                        DialogUtil.showMessage("订单已取消");
                        cancleOrder.setBackgroundResource(R.drawable.gray_stroke_bg);
                        cancleOrder.setTextColor(Color.GRAY);

                        // cancleOrder.setVisibility(View.GONE);
                        cancleOrder.setOnClickListener(null);
                        orderToPay.setVisibility(View.GONE);
                        orderStatus.setText("已取消");
                    } else {
                        String errorMessage = jsonObject.optString("message");
                        if (!StringUtils.isEmpty(errorMessage))
                            DialogUtil.showMessage(errorMessage);
                        else {
                            int code = jsonObject.optInt("code");
                            DialogUtil.showMessage(ErrorCode.getCodeName(code));
                        }
                    }
                }
            });
    }
}
