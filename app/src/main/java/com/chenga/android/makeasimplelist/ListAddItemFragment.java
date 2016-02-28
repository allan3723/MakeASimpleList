package com.chenga.android.makeasimplelist;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class ListAddItemFragment extends Fragment {
    private String mItem;

    private EditText addItemTextField;
    private ImageButton doneImageButton;

    private static final String ARG_ADD = "add_item";

    public static ListAddItemFragment newInstance() {
        return new ListAddItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.widget_listview_add_item, container, false);

        addItemTextField = (EditText) v.findViewById(R.id.listview_add_item);
        addItemTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mItem = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        doneImageButton = (ImageButton) v.findViewById(R.id.add_item_done);
        doneImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WidgetProvider.ACTION_ADD_ITEM);
                intent.putExtra(ARG_ADD, mItem);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        getActivity().getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                AppWidgetManager.INVALID_APPWIDGET_ID
                        ));
                getActivity().getApplicationContext().sendBroadcast(intent);
                getActivity().finish();
            }
        });
        return v;
    }
}
