package com.chenga.android.makeasimplelist;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.chenga.android.makeasimplelist.WidgetDbSchema.WidgetTable;

public class WidgetCursorWrapper extends CursorWrapper {

    public WidgetCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Widget getWidget() {
        String widgetId = getString(getColumnIndex(WidgetTable.Cols.WIDGETID));
        String title = getString(getColumnIndex(WidgetTable.Cols.TITLE));
        float textSize = getFloat(getColumnIndex(WidgetTable.Cols.TEXTSIZE));
        int textColor = getInt(getColumnIndex(WidgetTable.Cols.TEXTCOLOR));
        int bgColor = getInt(getColumnIndex(WidgetTable.Cols.BGCOLOR));
        String list = getString(getColumnIndex(WidgetTable.Cols.LIST));
        String strikeThrough = getString(getColumnIndex(WidgetTable.Cols.STRIKETHROUGH));
        int clickOption = getInt(getColumnIndex(WidgetTable.Cols.CLICKOPTION));
        String bold = getString(getColumnIndex(WidgetTable.Cols.BOLD));

        Widget widget = new Widget(widgetId);
        widget.setTitle(title);
        widget.setTextSize(textSize);
        widget.setTextColor(textColor);
        widget.setBGColor(bgColor);
        widget.setList(list);
        widget.setStrikeThrough(strikeThrough);
        widget.setOnClickOption(clickOption);
        widget.setBold(bold);

        return widget;
    }
}
