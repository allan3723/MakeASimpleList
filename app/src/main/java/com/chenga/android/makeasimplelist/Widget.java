package com.chenga.android.makeasimplelist;

import java.io.Serializable;

public class Widget implements Serializable {

    private String mTitle;
    private float mTextSize;
    private int mTextColor;
    private int mBGColor;
    private String mAppWidgetId;
    private String mList;
    private String mStrikeThrough;
    private int mOnClickOption;
    private String mBold;

    public static final int DEFAULT = 0;
    public static final int STRIKETHRU = 1;
    public static final int BOLD = 2;
    public static final int DELETE = 3;
    public static final int STB = 4;

    public Widget() {
        mTitle = new String();
    }

    public Widget(String appWidgetId) {
        mAppWidgetId = appWidgetId;
        mTitle = new String();
        mTextSize = 0;
        mTextColor = 0;
        mBGColor = 0;
        mOnClickOption = 0;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public int getBGColor() {
        return mBGColor;
    }

    public void setBGColor(int BGColor) {
        mBGColor = BGColor;
    }

    public String getAppWidgetId() {
        return mAppWidgetId;
    }

    public void setAppWidgetId(String appWidgetId) {
        mAppWidgetId = appWidgetId;
    }

    public String getList() {
        return mList;
    }

    public void setList(String list) {
        mList = list;
    }

    public String getStrikeThrough() {
        return mStrikeThrough;
    }

    public void setStrikeThrough(String strikeThrough) {
        mStrikeThrough = strikeThrough;
    }

    public int getOnClickOption() {
        return mOnClickOption;
    }

    public void setOnClickOption(int onClickOption) {
        mOnClickOption = onClickOption;
    }

    public String getBold() {
        return mBold;
    }

    public void setBold(String bold) {
        mBold = bold;
    }
}
