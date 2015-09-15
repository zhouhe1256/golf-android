
package com.bjcathay.qt.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjcathay.android.util.LogUtil;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.R;
import com.bjcathay.qt.model.PriceModel;
import com.bjcathay.qt.model.ProductModel;
import com.bjcathay.qt.model.TextInfo;
import com.bjcathay.qt.util.DateUtil;
import com.bjcathay.qt.util.SizeUtil;
import com.bjcathay.qt.util.ViewUtil;
import com.bjcathay.qt.widget.TosGallery;
import com.bjcathay.qt.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-9-14.
 */
public class ArraysFragment extends Fragment {
    int mNum;
    private Context context;
    private static ProductModel productModel;
    private ChangePrice changePrice;
    private List<PriceModel> priceModels;
    // PackagePriceModel packagePriceModel;
    private List<com.bjcathay.qt.model.TextInfo> textInfos = new ArrayList<com.bjcathay.qt.model.TextInfo>();

    public interface ChangePrice {
        void priceChanged(int price, PriceModel currentPrice, int number, String date);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.changePrice = (ChangePrice) activity;
    }

    public static ArraysFragment newInstance(int num, ProductModel productModel) {
        ArraysFragment array = new ArraysFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putSerializable("product", productModel);
        array.setArguments(args);
        return array;
    }

    public void setContext(Context context, ProductModel productModel) {
        this.context = context;
        this.productModel = productModel;
        this.priceModels = productModel.getPrices();
    }

    private TextView time;
    private String date;
    String hourSelect;
    private int number;
    private PriceModel currentPrice;
    private ImageView minas;
    private ImageView plus;
    private TextView fourPlus;
    private int amount = -1;
    private int amountmax = -1;
    private String daySelect;

    private void getDate() {
        // mCurYear, mCurMonth + 1, mCurDate
        String mounth = mCurMonth + "";
        String day = mCurDate + "";
        daySelect = mCurYear + "-" + (mounth.length() == 1 ? "0" + mounth : mounth) + "-"
                + (day.length() == 1 ? "0" + day : day) + " 00:00:01";
        // int[] priceStr = currentPrice.getFianlPrice(productModel.getType(),
        // number, hourSelect);
        // changePrice.priceChanged(priceStr[0], currentPrice, number,
        // daySelect);
        LogUtil.e("选择的日期是", daySelect);
    }

    private void getDayPrice() {
        if (daySelect == null) {
            getDate();
        }
        if (priceModels != null && priceModels.size() > 0)
            for (PriceModel priceModel : priceModels) {
                if (DateUtil.CompareTime(daySelect, priceModel.getStartAt(), priceModel.getEndAt()) == true) {
                    int[] priceStr = priceModel.getFianlPrice(productModel.getType(),
                            number, hourSelect);
                    priceModel.getPersonsDuring(ProductType.prdtType.COMBO);
                    amount = priceModel.getMinPerson();
                    if (number < amount)
                        number = amount;
                    currentPrice = priceModel;
                    changePrice.priceChanged(priceStr[0], currentPrice, number, daySelect);
                    return;
                }
            }
    }

    private void person() {
        if (currentPrice != null)
            amountmax = currentPrice.getMaxPerson();
        minas.setOnClickListener(new
                View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (amount > 0) {
                            if (number > amount) {
                                number--;
                                fourPlus.setText(number < 10 ? " " + number : number + "");
                                getDayPrice();
                            }
                        } else {
                            if (number > 1) {
                                number--;
                                fourPlus.setText(number < 10 ? " " + number : number + "");
                                getDayPrice();
                            }
                        }
                    }
                });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number < amountmax)
                    number++;
                fourPlus.setText(number < 10 ? " " + number : number + "");
                getDayPrice();
            }
        });
        fourPlus.setText(number < 10 ? " " + number : number + "");
        getDayPrice();

    }

    // 日期选择
    WheelView mDateWheel = null;
    WheelView mMonthWheel = null;
    WheelView mYearWheel = null;

    int mCurDate = 0;
    int mCurMonth = 0;
    int mCurYear = 0;
    int yearpos = 0;
    int monthpos = 0;
    int daypos = 0;
    ArrayList<TextInfo> mMonths = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mYears = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mDates = new ArrayList<TextInfo>();
    private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {
        @Override
        public void onEndFling(TosGallery v) {
            int pos = v.getSelectedItemPosition();
            if (!priceModels.isEmpty()) {
                if (v == mDateWheel) {
                    daypos = pos;
                    TextInfo info = textInfos.get(yearpos).textInfos.get(monthpos).textInfos
                            .get(pos);
                    setDate(info.mIndex, pos);
                } else if (v == mMonthWheel) {
                    monthpos = pos;
                    TextInfo info = textInfos.get(yearpos).textInfos.get(pos);
                    setMonth(info.mIndex, pos);
                } else if (v == mYearWheel) {
                    yearpos = pos;
                    TextInfo info = textInfos.get(pos);
                    setYear(info.mIndex, pos);
                }
                getDate();
                getDayPrice();
            }
        }
    };

    private String formatDate() {
        return String.format("Date: %d/%02d/%02d", mCurYear, mCurMonth + 1, mCurDate);
    }

    private void setDate(int date, int pos) {
        if (date != mCurDate) {
            mCurDate = date;
            mCurDate = textInfos.get(yearpos).textInfos.get(monthpos).textInfos.get(pos).mIndex;
        }
    }

    private void setYear(int year, int pos) {
        if (year != mCurYear) {
            mCurYear = year;
            mCurMonth = textInfos.get(pos).textInfos.get(0).mIndex;
            mCurDate = textInfos.get(pos).textInfos.get(0).textInfos.get(0).mIndex;
            ((WheelTextAdapter) mMonthWheel.getAdapter()).setData(textInfos.get(pos).textInfos);
            mMonthWheel.setSelection(0);
            ((WheelTextAdapter) mDateWheel.getAdapter()).setData(textInfos.get(pos).textInfos
                    .get(0).textInfos);
            mDateWheel.setSelection(0);
        }
    }

    private void setMonth(int month, int pos) {
        if (month != mCurMonth) {
            mCurMonth = month;
            mCurDate = textInfos.get(yearpos).textInfos.get(pos).textInfos.get(0).mIndex;
            ((WheelTextAdapter) mDateWheel.getAdapter()).setData(textInfos.get(yearpos).textInfos
                    .get(pos).textInfos);
            mDateWheel.setSelection(0);
        }
    }

    private void initView(View v) {
        mDateWheel = (WheelView) v.findViewById(R.id.wheel_date);
        mMonthWheel = (WheelView) v.findViewById(R.id.wheel_month);
        mYearWheel = (WheelView) v.findViewById(R.id.wheel_year);

        mDateWheel.setOnEndFlingListener(mListener);
        mMonthWheel.setOnEndFlingListener(mListener);
        mYearWheel.setOnEndFlingListener(mListener);

        mDateWheel.setSoundEffectsEnabled(true);
        mMonthWheel.setSoundEffectsEnabled(true);
        mYearWheel.setSoundEffectsEnabled(true);

        mDateWheel.setAdapter(new WheelTextAdapter(context));
        mMonthWheel.setAdapter(new WheelTextAdapter(context));
        mYearWheel.setAdapter(new WheelTextAdapter(context));

        prepareData();
        person();

    }

    private void prepareData() {
        priceModels = productModel.getPrices();
        if (!priceModels.isEmpty()) {
            textInfos = DateUtil.getComboDates(priceModels);

            ((WheelTextAdapter) mMonthWheel.getAdapter()).setData(textInfos.get(0).textInfos);
            ((WheelTextAdapter) mYearWheel.getAdapter()).setData(textInfos);
            ((WheelTextAdapter) mDateWheel.getAdapter())
                    .setData(textInfos.get(0).textInfos.get(0).textInfos);

            mCurDate = textInfos.get(0).textInfos.get(0).textInfos.get(0).mIndex;
            mCurMonth = textInfos.get(0).textInfos.get(0).mIndex;
            mCurYear = textInfos.get(0).mIndex;
            getDate();
            getDayPrice();
            mMonthWheel.setSelection(0);
            mYearWheel.setSelection(0);
            mDateWheel.setSelection(0);
        }
    }

    protected class WheelTextAdapter extends BaseAdapter {
        List<TextInfo> mData = null;
        int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int mHeight = 50;
        Context mContext = null;

        public WheelTextAdapter(Context context) {
            mContext = context;
            mHeight = (int) SizeUtil.pixelToDp(context, mHeight);
        }

        public void setData(List<TextInfo> data) {
            mData = data;
            this.notifyDataSetChanged();
        }

        public void setItemSize(int width, int height) {
            mWidth = width;
            mHeight = (int) SizeUtil.pixelToDp(mContext, height);
        }

        @Override
        public int getCount() {
            return (null != mData) ? mData.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;

            if (null == convertView) {
                convertView = new TextView(mContext);
                convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth, mHeight));
                textView = (TextView) convertView;
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                textView.setTextColor(Color.BLACK);
            }

            if (null == textView) {
                textView = (TextView) convertView;
            }

            TextInfo info = mData.get(position);
            textView.setText(info.mText);
            // textView.setTextColor(info.mColor);

            return convertView;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        productModel = (ProductModel) getArguments().getSerializable("product");
        this.priceModels = productModel.getPrices();
        // this.packagePriceModel = productModel.getPackagePriceModel();
        this.priceModels = productModel.getPrices();
        System.out.println("mNum Fragment create =" + mNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        System.out.println("onCreateView = ");
        // 在这里加载每个 fragment的显示的 View
        View v = null;
        if (mNum == 0) {
            v = inflater.inflate(R.layout.fragment_package_note, container, false);
            // ((TextView) v.findViewById(R.id.textView1)).setText(mNum +
            // "= mNum");
            if (productModel != null)
                ((TextView) v.findViewById(R.id.amountNotice)).setText(productModel
                        .getAmountNotice());
            minas = ViewUtil.findViewById(v, R.id.dialog_order_minas);
            plus = ViewUtil.findViewById(v, R.id.dialog_order_plus);
            fourPlus = ViewUtil.findViewById(v, R.id.dialog_order_sure_number_edit);
            initView(v);
        } else if (mNum == 1) {
            v = inflater.inflate(R.layout.fragment_package_note2, container, false);
            // ((TextView) v.findViewById(R.id.textView1)).setText(mNum +
            // "= mNum");
            if (productModel != null) {

                ((TextView) v.findViewById(R.id.purchasingNotice)).setText(productModel
                        .getPurchasingNotice());
                ((TextView) v.findViewById(R.id.costInclude))
                        .setText(productModel.getCostInclude());
                ((TextView) v.findViewById(R.id.recommendReason)).setText(productModel
                        .getRecommendReason());
            }
        } else if (mNum == 2) {
            v = inflater.inflate(R.layout.fragment_package_note3, container, false);
            if (productModel != null&&!productModel.getScheduling().isEmpty())

                ((WebView) v.findViewById(R.id.richTextView))
                        .loadDataWithBaseURL(
                                null,
                                productModel.getScheduling()
                                        .replaceAll("font-size:.*pt;", "font-size:0pt;")
                                        .replaceAll("font-family:.*;", "font-family:;"),
                                "text/html", "UTF-8", null);

        } else {
            v = inflater.inflate(R.layout.fragment_package_note, container, false);
            // ((TextView) v.findViewById(R.id.textView1)).setText(mNum +
            // "= mNum");
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        System.out.println("onActivityCreated = ");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        System.out.println(mNum + "mNumDestory");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
