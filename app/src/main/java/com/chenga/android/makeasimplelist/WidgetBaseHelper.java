package com.chenga.android.makeasimplelist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chenga.android.makeasimplelist.WidgetDbSchema.WidgetTable;

public class WidgetBaseHelper extends SQLiteOpenHelper {
    private static final int version = 1;
    private static final String DATABASE_NAME = "widgetBase.db";

    public WidgetBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + WidgetTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        WidgetTable.Cols.WIDGETID + ", " +
                        WidgetTable.Cols.TITLE + ", " +
                        WidgetTable.Cols.TEXTSIZE + ", " +
                        WidgetTable.Cols.TEXTCOLOR + ", " +
                        WidgetTable.Cols.BGCOLOR + ", " +
                        WidgetTable.Cols.LIST + ", " +
                        WidgetTable.Cols.STRIKETHROUGH + ", " +
                        WidgetTable.Cols.CLICKOPTION + ", " +
                        WidgetTable.Cols.BOLD +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
