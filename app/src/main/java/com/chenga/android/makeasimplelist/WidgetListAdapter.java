package com.chenga.android.makeasimplelist;

/*
* ListView's adapter via remoteviews to control the items in the list.
* Called when the list need updating such as when items are added,
* colors/size are changed, and on-click effects.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WidgetListAdapter implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<String> itemList;
    private Context mContext;
    private int appWidgetId;
    private SingletonDatabase mData;

    private static final String TAG = "RemoteViewsFactory";

    public WidgetListAdapter(Context context, Intent intent) {
        mContext = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {
        itemList = new ArrayList();
    }

    //Update the list when items are added/deleted
    @Override
    public void onDataSetChanged() {
        mData = SingletonDatabase.get(mContext);
        Widget widget = mData.getWidget(appWidgetId);

        String jsonString = widget.getList();

        List<String> allItems = new ArrayList<>();
        if (!(jsonString == null)) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.optJSONArray(WidgetProvider.ARRAY_STRING);

                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        allItems.add(jsonArray.get(i).toString());
                    }
                }
            } catch(JSONException je) {
                Log.e(TAG, "Failed to parse JSON", je);
            }
        }

        //an item was deleted so remake list
        if (itemList.size() > allItems.size()) {
            itemList.clear();
        }

        for (int i = itemList.size(); i < allItems.size(); i++) {
            itemList.add(allItems.get(i));
        }
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Get the current location of clicked item and get the current
    //settings and set it to the current item
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteViews =
                new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        mData = SingletonDatabase.get(mContext);
        Widget widget = mData.getWidget(appWidgetId);

        String strikethrough = widget.getStrikeThrough();
        String item = itemList.get(position);
        SpannableString ss = new SpannableString(item);

        //Set strike through and highlight if the item has crossed out
        if (strikethrough != null) {
            if (strikethrough.charAt(position) == 't') {
                ss.setSpan(new StrikethroughSpan(), 0, item.length(), 0);

                int color;
                switch (widget.getBGColor()) {
                    case R.drawable.rectangle_red_bg:
                        color = ContextCompat.getColor(mContext, R.color.colorHRed);
                        break;
                    case R.drawable.rectangle_orange_bg:
                        color = ContextCompat.getColor(mContext, R.color.colorHOrange);
                        break;
                    case R.drawable.rectangle_yellow_bg:
                        color = ContextCompat.getColor(mContext, R.color.colorHYellow);
                        break;
                    case R.drawable.rectangle_green_bg:
                        color = ContextCompat.getColor(mContext, R.color.colorHGreen);
                        break;
                    case R.drawable.rectangle_blue_bg:
                        color = ContextCompat.getColor(mContext, R.color.colorHBlue);
                        break;
                    default:
                        color = ContextCompat.getColor(mContext, R.color.colorHWhite);
                }

                //remoteViews.setInt(R.id.items, "setBackgroundColor", color);
                ss.setSpan(new BackgroundColorSpan(color), 0, item.length(), 0);
            }
            else {
                //remoteViews.setInt(R.id.items, "setBackgroundColor", Color.TRANSPARENT);
                ss.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), 0, item.length(), 0);
            }
        }

        //Now set bold
        String bold = widget.getBold();
        if (bold != null) {
            if (bold.charAt(position) == 't') {
                ss.setSpan(new StyleSpan(Typeface.BOLD), 0, item.length(), 0);
            }
        }

        //Show the item on the list and set the text size and color
        remoteViews.setTextViewText(R.id.items, ss);
        remoteViews.setTextColor(R.id.items, widget.getTextColor());
        remoteViews.setTextViewTextSize(R.id.items,
                TypedValue.COMPLEX_UNIT_SP, widget.getTextSize());

        //Make list items clickable for strike through effect
        Bundle extras = new Bundle();
        extras.putInt(WidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.items, fillInIntent);

        return remoteViews;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDestroy() {
        itemList.clear();
    }
}
