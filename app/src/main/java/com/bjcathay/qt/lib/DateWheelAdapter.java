package com.bjcathay.qt.lib;

/**
 * Created by bjcathay on 15-5-31.
 */
public class DateWheelAdapter implements WheelAdapter {

    /**
     * The default min value
     */
    public static final int DEFAULT_MAX_VALUE = 9;

    /**
     * The default max value
     */
    private static final int DEFAULT_MIN_VALUE = 0;

    // Values
    private int minValue;
    private int maxValue;
    private boolean flag;

    // format
    private String format;

    /**
     * Default constructor
     */
    public DateWheelAdapter() {
        this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
    }

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public DateWheelAdapter(int minValue, int maxValue) {
        this(minValue, maxValue, null);
    }

    public DateWheelAdapter(int minValue, int maxValue, boolean flag) {
        this(minValue, maxValue, flag, null);
    }

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     * @param format   the format string
     */
    public DateWheelAdapter(int minValue, int maxValue, String format) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }

    public DateWheelAdapter(int minValue, int maxValue, boolean flag, String format) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.flag = flag;
        this.format = format;
    }

    @Override
    public String getItem(int index) {
        if(flag){
            if(minValue+index==1)
                return "上午";
            else
                return "下午";
        }
        if (index >= 0 && index < getItemsCount()) {
            int value = minValue + index;
            return format != null ? String.format(format, value) : Integer.toString(value);
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }

    @Override
    public int getMaximumLength() {
        int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
        int maxLen = Integer.toString(max).length();
        if (minValue < 0) {
            maxLen++;
        }
        return maxLen;
    }

    @Override
    public void updateDate() {

    }
}