package com.chenga.android.makeasimplelist;

/*
* The widget's broadcast receiver. Receives which buttons get pressed on the
* widget and calls the appropriate activity to take care of it. Also updates
* the database
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WidgetProvider extends AppWidgetProvider {

    public static final String UPDATE_LIST_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String EXTRA_ITEM = "com.chenga.android.EXTRA_ITEM";
    public static final String ACTION_SETTINGS_CHANGED =
            "com.chenga.android.makeasimplelist.SETTINGS_CHANGED";
    public static final String ACTION_ADD_ITEM = "com.chenga.android.makeasimplelist.ITEMS_ADDED";
    public static final String ACTION_STRIKETHROUGH_TEXT =
            "com.chenga.android.makeasimplelist.STRIKETHROUGH";
    public static final String ACTION_DELETE_ITEM =
            "com.chenga.android.makeasimplelist.ITEMS_DELETED";

    public static final String ARRAY_STRING = "SQLiteListItemArray";
    public static final int TITLE_SIZE_BUFFER = 5;

    private static final int DEFAULT_TEXTCOLOR = R.color.colorBlack;
    private static final int DEFAULT_TEXTSIZE = 20;
    private static final int DEFAULT_BGCOLOR = R.drawable.rectangle_blue_bg;

    private static final String ARG_SETTINGS = "settings";
    private static final String ARG_ADD = "add_item";

    private static final String TAG = "WidgetProvider";

    private SingletonDatabase mData;
    private Widget mInfo;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        mData = SingletonDatabase.get(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        mData = SingletonDatabase.get(context);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetListItem(context, appWidgetId);

            setButtons(remoteViews, context, appWidgetId);

            //If widget is not in database, add it. Otherwise, use it to update the widget's view.
            if (!mData.inDatabase(appWidgetId)) {
                Widget widget = new Widget(Integer.toString(appWidgetId));
                widget.setBGColor(DEFAULT_BGCOLOR);
                widget.setTextColor(ContextCompat.getColor(context, DEFAULT_TEXTCOLOR));
                widget.setTextSize(DEFAULT_TEXTSIZE);

                mData.addWidget(widget);
            } else {
                setSettings(remoteViews, context, appWidgetId);
            }

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        int widgetId;
        mData = SingletonDatabase.get(context);

        switch(intent.getAction()) {
            //Settings button pressed
            case ACTION_SETTINGS_CHANGED:
                mInfo = (Widget) intent.getSerializableExtra(ARG_SETTINGS);

                Bundle extras = intent.getExtras();
                if(extras != null) {
                    boolean textColorOrSizeChanged = false;
                    widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

                    Widget newSettings = mData.getWidget(widgetId);
                    RemoteViews remoteViews = getConfiguredView(context, widgetId);
                    if (remoteViews == null) {
                        Log.d(TAG, "remoteViews empty");
                    }

                    if (!mInfo.getTitle().isEmpty()) {
                        remoteViews.setTextViewText(R.id.list_title, mInfo.getTitle());
                        newSettings.setTitle(mInfo.getTitle());
                    }

                    if ((mInfo.getTextColor() != 0)) {
                        remoteViews.setTextColor(R.id.list_title, mInfo.getTextColor());
                        newSettings.setTextColor(mInfo.getTextColor());
                        textColorOrSizeChanged = true;
                    }

                    if ((mInfo.getTextSize() != 0)) {
                        remoteViews.setTextViewTextSize(R.id.list_title,
                                TypedValue.COMPLEX_UNIT_SP, mInfo.getTextSize() + TITLE_SIZE_BUFFER);
                        newSettings.setTextSize(mInfo.getTextSize());
                        textColorOrSizeChanged = true;
                    }

                    if ((mInfo.getBGColor() != 0)) {
                        remoteViews.setInt(R.id.layout, "setBackgroundResource",
                                mInfo.getBGColor());
                        newSettings.setBGColor(mInfo.getBGColor());
                        textColorOrSizeChanged = true;
                    }

                    if ((mInfo.getOnClickOption() != 0)) {
                        newSettings.setOnClickOption(mInfo.getOnClickOption());
                    }

                    if (textColorOrSizeChanged) {
                        AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(
                                widgetId, R.id.list);
                    }

                    mData.updateWidget(newSettings);
                    AppWidgetManager.getInstance(context).updateAppWidget(widgetId, remoteViews);
                }
                break;
            //Adding items to list button pressed
            case ACTION_ADD_ITEM:
                extras = intent.getExtras();
                if(extras != null) {
                    widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);
                    String newItems = extras.getString(ARG_ADD);

                    Widget widget = mData.getWidget(widgetId);

                    if (newItems == null || newItems.isEmpty()) {
                        return;
                    }

                    String jsonString = widget.getList();

                    List<String> list = new ArrayList<>();
                    if ((jsonString != null)) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONArray jsonArray = jsonObject.optJSONArray(ARRAY_STRING);

                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    list.add(jsonArray.get(i).toString());
                                }
                            }
                        } catch(JSONException je) {
                            Log.e(TAG, "Failed to parse JSON", je);
                        }
                    }

                    try {
                        list.add(newItems);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(ARRAY_STRING, new JSONArray(list));
                        String stringArray = jsonObject.toString();
                        widget.setList(stringArray);

                    } catch(JSONException je) {
                        Log.e(TAG, "Failed to parse JSON", je);
                    }

                    //Add strikethrough for the new item
                    String strikeThrough = widget.getStrikeThrough();
                    if (strikeThrough == null) {
                        strikeThrough = "f";
                    } else {
                        strikeThrough = strikeThrough + "f";
                    }
                    widget.setStrikeThrough(strikeThrough);

                    //Add bold for the new item
                    String bold = widget.getBold();
                    if (bold == null) {
                        bold = "f";
                    } else {
                        bold = bold + "f";
                    }
                    widget.setBold(bold);

                    mData.updateWidget(widget);

                    AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(
                            widgetId, R.id.list);
                }
                break;
            //Used for when the user clicks an item for the list DESPITE the name
            //Strike through, bold, OR delete effect
            case ACTION_STRIKETHROUGH_TEXT:
                int textPosition;

                extras = intent.getExtras();
                if(extras != null) {
                    widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);
                    textPosition = extras.getInt(EXTRA_ITEM);

                    Widget widget = mData.getWidget(widgetId);
                    int clickOption = widget.getOnClickOption();

                    switch (clickOption) {
                        case Widget.STRIKETHRU:
                            String strikeThrough = widget.getStrikeThrough();
                            StringBuilder sb = new StringBuilder(strikeThrough);
                            if (strikeThrough.charAt(textPosition) == 'f') {
                                sb.setCharAt(textPosition, 't');
                            } else {
                                sb.setCharAt(textPosition, 'f');
                            }

                            widget.setStrikeThrough(sb.toString());

                            //if list has been completed/completely crossed out, show toast
                            if (widget.getStrikeThrough() != null ) {
                                if (!(widget.getStrikeThrough().contains("f"))) {

                                    Toast.makeText(context, context
                                                    .getResources().getString(R.string.all_crossed_toast_front) +
                                                    widget.getTitle()+ context.getResources()
                                                    .getString(R.string.all_crossed_toast_back),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            break;
                        case Widget.BOLD:
                            String bold = widget.getBold();
                            sb = new StringBuilder(bold);
                            if (bold.charAt(textPosition) == 'f') {
                                sb.setCharAt(textPosition, 't');
                            } else {
                                sb.setCharAt(textPosition, 'f');
                            }

                            widget.setBold(sb.toString());
                            break;
                        case Widget.STB:
                            strikeThrough = widget.getStrikeThrough();
                            sb = new StringBuilder(strikeThrough);
                            if (strikeThrough.charAt(textPosition) == 'f') {
                                sb.setCharAt(textPosition, 't');
                            } else {
                                sb.setCharAt(textPosition, 'f');
                            }

                            widget.setStrikeThrough(sb.toString());

                            bold = widget.getBold();
                            sb = new StringBuilder(bold);
                            if (bold.charAt(textPosition) == 'f') {
                                sb.setCharAt(textPosition, 't');
                            } else {
                                sb.setCharAt(textPosition, 'f');
                            }

                            widget.setBold(sb.toString());

                            //if list has been completed/completely crossed out, show toast
                            if (widget.getStrikeThrough() != null ) {
                                if (!(widget.getStrikeThrough().contains("f"))) {

                                    Toast.makeText(context, context
                                                    .getResources().getString(R.string.all_crossed_toast_front) +
                                                    widget.getTitle()+ context.getResources()
                                                    .getString(R.string.all_crossed_toast_back),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                            break;
                        case Widget.DELETE:
                            Intent deleteIntent = new Intent(context, ListDeleteItemActivity.class);
                            deleteIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                            deleteIntent.putExtra(EXTRA_ITEM, textPosition);
                            deleteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(deleteIntent);
                            return;
                        default:
                            break;
                    }

                    mData.updateWidget(widget);

                    AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(
                            widgetId, R.id.list);
                }
                break;
            //Delete item from list
            case ACTION_DELETE_ITEM:
                extras = intent.getExtras();
                if(extras != null) {
                    widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);
                    textPosition = extras.getInt(EXTRA_ITEM);

                    Widget widget = mData.getWidget(widgetId);

                    String jsonString = widget.getList();

                    List<String> list = new ArrayList<>();
                    if ((jsonString != null)) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONArray jsonArray = jsonObject.optJSONArray(ARRAY_STRING);

                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    list.add(jsonArray.get(i).toString());
                                }
                            }
                        } catch(JSONException je) {
                            Log.e(TAG, "Failed to parse JSON", je);
                        }
                    }

                    try {
                        list.remove(textPosition);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(ARRAY_STRING, new JSONArray(list));
                        String stringArray = jsonObject.toString();
                        widget.setList(stringArray);

                    } catch(JSONException je) {
                        Log.e(TAG, "Failed to parse JSON", je);
                    }

                    //update strikethrough
                    StringBuilder sb = new StringBuilder(widget.getStrikeThrough());
                    sb.deleteCharAt(textPosition);
                    widget.setStrikeThrough(sb.toString());

                    mData.updateWidget(widget);
                    AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(
                            widgetId, R.id.list);

                }
                break;
            //Widget gets deleted
            case AppWidgetManager.ACTION_APPWIDGET_DELETED:
                widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                    mData.deleteWidget(widgetId);
                }
                break;
            default:
                super.onReceive(context, intent);
                break;
        }
    }

    private PendingIntent newPendingIntent(Context packageContext, Class<?> cls, int appWidgetId) {

        Intent intent = new Intent(packageContext, cls);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        return PendingIntent.getActivity(packageContext,
                appWidgetId, intent, 0);
    }

    private RemoteViews updateWidgetListItem(Context context, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        remoteViews.setRemoteAdapter(R.id.list, svcIntent);
        remoteViews.setEmptyView(R.id.list, R.id.empty);

        return remoteViews;
    }

    private RemoteViews getConfiguredView(Context context, int widgetId) {
        RemoteViews remoteViews = updateWidgetListItem(context, widgetId);

        setButtons(remoteViews, context, widgetId);
        setSettings(remoteViews, context, widgetId);

        return remoteViews;
    }

    public void setButtons(RemoteViews remoteViews, Context context, int appWidgetId) {
        //Add button
        PendingIntent addIcon = newPendingIntent(context, ListAddItemActivity.class,
                appWidgetId);
        remoteViews.setOnClickPendingIntent(R.id.add_item, addIcon);
        remoteViews.setOnClickPendingIntent(R.id.empty, addIcon);

        //Settings button
        PendingIntent settingIcon = newPendingIntent(context, SettingActivity.class,
                appWidgetId);
        remoteViews.setOnClickPendingIntent(R.id.settings_item, settingIcon);

        //Strike through text
        Intent clickIntent = new Intent(context, WidgetProvider.class);
        clickIntent.setAction(ACTION_STRIKETHROUGH_TEXT);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent strikeText = PendingIntent.getBroadcast(context, appWidgetId,
                clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.list, strikeText);
    }

    public void setSettings(RemoteViews remoteViews, Context context, int appWidgetId) {
        Widget widget = mData.getWidget(appWidgetId);
        boolean changeTextColorOrSize = false;

        if (!widget.getTitle().isEmpty()) {
            remoteViews.setTextViewText(R.id.list_title, widget.getTitle());
        }

        if (!(widget.getTextColor() == 0)) {
            remoteViews.setTextColor(R.id.items, widget.getTextColor());
            remoteViews.setTextColor(R.id.list_title, widget.getTextColor());
            changeTextColorOrSize = true;
        }

        if (!(widget.getTextSize() == 0)) {
            remoteViews.setTextViewTextSize(R.id.items,
                    TypedValue.COMPLEX_UNIT_SP, widget.getTextSize());
            remoteViews.setTextViewTextSize(R.id.list_title,
                    TypedValue.COMPLEX_UNIT_SP, widget.getTextSize()+TITLE_SIZE_BUFFER);
            changeTextColorOrSize = true;
        }

        if (!(widget.getBGColor() == 0)) {
            remoteViews.setInt(R.id.layout, "setBackgroundResource", widget.getBGColor());
        }

        if (changeTextColorOrSize) {
            AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(
                    appWidgetId, R.id.list);
        }
    }

}


