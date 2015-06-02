package com.bjcathay.qt.lib;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.bjcathay.qt.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjcathay on 15-5-31.
 */
public class WheelDate {
    private View view;
    private WheelView wv_option1;
    private WheelView wv_option2;
    private WheelView wv_option3;
    public int screenheight;
    private List<String> mOptions1Items;//日期
    private List<String> mOptions2Items;//上午下午
    private List<String> mOptions3Items;//上午时间段
    private List<String> mOptions4Items;//下午时间段
    private OnOptionsSelectListener optionsSelectListener;
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public WheelDate(WheelView wv_option1, WheelView wv_option2, WheelView wv_option3, OnOptionsSelectListener optionsSelectListener) {
        super();
        this.view = view;
        this.wv_option1 = wv_option1;
        this.wv_option2 = wv_option2;
        this.wv_option3 = wv_option3;

        this.optionsSelectListener = optionsSelectListener;
        setView(view);
    }

    public WheelDate(View view, OnOptionsSelectListener optionsSelectListener) {
        super();
        this.view = view;
        this.optionsSelectListener = optionsSelectListener;
        FrameLayout.LayoutParams lpLl = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
       /* lpLl.gravity = Gravity.BOTTOM;
        this.view.setLayoutParams(lpLl);*/
        setView(this.view);
    }

    public void setPicker(List<String> options1Items,
                          List<String> options2Items,
                          List<String> options3Items,
                          List<String> options4Items,
                          boolean linkage) {
        this.mOptions1Items = options1Items;
        this.mOptions2Items = options2Items;
        this.mOptions3Items = options3Items;
        this.mOptions4Items = options4Items;
        int len = ArrayWheelAdapter.DEFAULT_LENGTH;
        if (this.mOptions3Items == null)
            len = 8;
        if (this.mOptions2Items == null)
            len = 12;
        // 选项1

           wv_option1 = (WheelView) view.findViewById(R.id.options1);
        wv_option1.setAdapter(new ArrayWheelAdapter(mOptions1Items, len));// 设置显示数据
        if (mOptions1Items.size() > 1)
            wv_option1.setCurrentItem(1);// 初始化时显示的数据
        else
            wv_option1.setCurrentItem(0);// 初始化时显示的数据
        // 选项2
            wv_option2 = (WheelView) view.findViewById(R.id.options2);
        if (mOptions2Items != null)
            wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items, len));// 设置显示数据
        wv_option2.setCurrentItem(wv_option1.getCurrentItem());// 初始化时显示的数据
        // 选项3
          wv_option3 = (WheelView) view.findViewById(R.id.options3);
        if (mOptions3Items != null)
            wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items, len));// 设置显示数据
        wv_option3.setCurrentItem(wv_option3.getCurrentItem());// 初始化时显示的数据

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = (screenheight / 100) * 4;
        textSize = 30;

        wv_option1.TEXT_SIZE = textSize;
        wv_option2.TEXT_SIZE = textSize;
        wv_option3.TEXT_SIZE = textSize;

        if (this.mOptions2Items == null)
            wv_option2.setVisibility(View.GONE);
        if (this.mOptions3Items == null)
            wv_option3.setVisibility(View.GONE);

        // 联动监听器
        OnWheelChangedListener wheelListener_option1 = new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
               /* if ("上午".equals(mOptions2Items.get(wv_option2.getCurrentItem()).toString())) {
                    optionsSelectListener.onOptionsSelect(mOptions1Items.get(wv_option1.getCurrentItem()).toString(),
                            mOptions2Items.get(wv_option2.getCurrentItem()).toString(),
                            mOptions3Items.get(wv_option3.getCurrentItem()).toString());
                } else {
                    optionsSelectListener.onOptionsSelect(mOptions1Items.get(wv_option1.getCurrentItem()).toString(),
                            mOptions2Items.get(wv_option2.getCurrentItem()).toString(),
                            mOptions4Items.get(wv_option3.getCurrentItem()).toString());
                }*/
            }
        };
        OnWheelChangedListener wheelListener_option2 = new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
               /* if (mOptions3Items != null && mOptions1Items.size() > 1) {
                    if ("上午".equals(mOptions2Items.get(wv_option2.getCurrentItem()).toString())) {
                        wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items));
                    } else {
                        wv_option3.setAdapter(new ArrayWheelAdapter(mOptions4Items));
                    }

                    if ("上午".equals(mOptions2Items.get(wv_option2.getCurrentItem()).toString())) {

                        optionsSelectListener.onOptionsSelect(mOptions1Items.get(wv_option1.getCurrentItem()).toString(),
                                mOptions2Items.get(wv_option2.getCurrentItem()).toString(),
                                mOptions3Items.get(wv_option3.getCurrentItem()).toString());
                    } else {
                        optionsSelectListener.onOptionsSelect(mOptions1Items.get(wv_option1.getCurrentItem()).toString(),
                                mOptions2Items.get(wv_option2.getCurrentItem()).toString(),
                                mOptions4Items.get(wv_option3.getCurrentItem()).toString());
                    }
                    wv_option3.setCurrentItem(0);

                }*/
            }
        };
        OnWheelChangedListener wheelListener_option3 = new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
               /* if ("上午".equals(mOptions2Items.get(wv_option2.getCurrentItem()).toString())) {

                    optionsSelectListener.onOptionsSelect(mOptions1Items.get(wv_option1.getCurrentItem()).toString(),
                            mOptions2Items.get(wv_option2.getCurrentItem()).toString(),
                            mOptions3Items.get(wv_option3.getCurrentItem()).toString());
                } else {
                    optionsSelectListener.onOptionsSelect(mOptions1Items.get(wv_option1.getCurrentItem()).toString(),
                            mOptions2Items.get(wv_option2.getCurrentItem()).toString(),
                            mOptions4Items.get(wv_option3.getCurrentItem()).toString());
                }*/
            }
        };

        // 添加联动监听
        if (options2Items != null && linkage)
            wv_option1.addChangingListener(wheelListener_option1);
        if (options3Items != null && linkage)
            wv_option2.addChangingListener(wheelListener_option2);
        if (options4Items != null && linkage)
            wv_option3.addChangingListener(wheelListener_option3);

    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wv_option1.setCyclic(cyclic);
        wv_option2.setCyclic(cyclic);
        wv_option3.setCyclic(cyclic);
    }

    /**
     * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
     *
     * @return
     */
    public int[] getCurrentItems() {
        int[] currentItems = new int[3];
        currentItems[0] = wv_option1.getCurrentItem();
        currentItems[1] = wv_option2.getCurrentItem();
        currentItems[2] = wv_option3.getCurrentItem();
        return currentItems;
    }

    /**
     * 设置选项的单位
     *
     * @param label1
     * @param label2
     * @param label3
     */
    public void setLabels(String label1, String label2, String label3) {
        if (label1 != null)
            wv_option1.setLabel(label1);
        if (label2 != null)
            wv_option2.setLabel(label2);
        if (label3 != null)
            wv_option3.setLabel(label3);
    }

    public void setCurrentItems(int option1, int option2, int option3) {
        wv_option1.setCurrentItem(option1);
        wv_option2.setCurrentItem(option2);
        wv_option3.setCurrentItem(option3);
    }

    public interface OnOptionsSelectListener {
        public void onOptionsSelect(String options1, String option2, String options3);
    }

    public void notifyDate(Context context){
        wv_option1.update(context);
    }
}
