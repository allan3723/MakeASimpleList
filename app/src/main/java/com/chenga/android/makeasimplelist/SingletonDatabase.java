package com.chenga.android.makeasimplelist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chenga.android.makeasimplelist.WidgetDbSchema.WidgetTable;

import java.util.ArrayList;
import java.util.List;

public class SingletonDatabase {
    private static SingletonDatabase sSingletonData;

    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static SingletonDatabase get(Context context) {
        if (sSingletonData == null) {
            sSingletonData = new SingletonDatabase(context);
        }

        return sSingletonData;
    }

    private SingletonDatabase(Context context) {
        mContext = context;
        mDatabase = new WidgetBaseHelper(mContext).getWritableDatabase();
    }

    public List<Widget> getWidgets() {
        List<Widget> settings = new ArrayList<>();
        WidgetCursorWrapper cursor = queryWidgets(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                settings.add(cursor.getWidget());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return settings;
    }

    public Widget getWidget(int widgetId) {
        WidgetCursorWrapper cursor = queryWidgets(
                WidgetTable.Cols.WIDGETID + " = ?",
                new String[]{Integer.toString(widgetId)}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getWidget();
        } finally {
            cursor.close();
        }
    }

    public boolean inDatabase(int widgetId) {
        WidgetCursorWrapper cursor = queryWidgets(
                WidgetTable.Cols.WIDGETID + " = ?",
                new String[]{Integer.toString(widgetId)}
        );

        try {
            if (cursor.getCount() == 0) {
                return false;
            }

            return true;
        } finally {
            cursor.close();
        }
    }

    public void updateWidget(Widget widget) {
        String widgetIdString = widget.getAppWidgetId();
        ContentValues values = getContentValues(widget);

        mDatabase.update(WidgetTable.NAME, values,
                WidgetTable.Cols.WIDGETID + " = ?", new String[] { widgetIdString });
    }

    public void addWidget(Widget s) {
        ContentValues values = getContentValues(s);

        mDatabase.insert(WidgetTable.NAME, null, values);
    }

    public void deleteWidget(int widgetId) {
        String widgetIdString = Integer.toString(widgetId);
        mDatabase.delete(WidgetTable.NAME, WidgetTable.Cols.WIDGETID + " =?",
                new String[] { widgetIdString });
    }

    private static ContentValues getContentValues(Widget widget) {
        ContentValues values = new ContentValues();

        values.put(WidgetTable.Cols.WIDGETID, widget.getAppWidgetId());
        values.put(WidgetTable.Cols.TITLE, widget.getTitle());
        values.put(WidgetTable.Cols.TEXTSIZE, widget.getTextSize());
        values.put(WidgetTable.Cols.TEXTCOLOR, widget.getTextColor());
        values.put(WidgetTable.Cols.BGCOLOR, widget.getBGColor());
        values.put(WidgetTable.Cols.LIST, widget.getList());
        values.put(WidgetTable.Cols.STRIKETHROUGH, widget.getStrikeThrough());

        return values;
    }

    private WidgetCursorWrapper queryWidgets(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                WidgetTable.NAME,
                null,   //Columns: null = all
                whereClause,
                whereArgs,
                null,   //Group By
                null,   // Having
                null    // Order By
        );

        return new WidgetCursorWrapper(cursor);
    }
}
