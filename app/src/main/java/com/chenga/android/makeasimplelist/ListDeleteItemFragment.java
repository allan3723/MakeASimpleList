package com.chenga.android.makeasimplelist;

/*
* A dialog that ask if the user is certain that they want to delete
* the item off the list. This fragment is created when the delete on-click
* effect is selected and a user clicks an item on their list.
 */

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ListDeleteItemFragment extends Fragment {

    Button okButton;
    Button cancelButton;

    public static ListDeleteItemFragment newInstance() {
        return new ListDeleteItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_delete_alert, container, false);

        okButton = (Button) v.findViewById(R.id.delete_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WidgetProvider.ACTION_DELETE_ITEM);
                Bundle extras = getActivity().getIntent().getExtras();

                if (extras != null) {
                    intent.putExtra(WidgetProvider.EXTRA_ITEM,
                            extras.getInt(WidgetProvider.EXTRA_ITEM));
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                            extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                    AppWidgetManager.INVALID_APPWIDGET_ID
                            ));
                }

                getActivity().getApplicationContext().sendBroadcast(intent);
                getActivity().finish();
            }
        });

        cancelButton = (Button) v.findViewById(R.id.delete_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return v;
    }
}
