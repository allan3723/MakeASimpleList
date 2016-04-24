package com.chenga.android.makeasimplelist;

/*
* Opens an activity that contains the instruction on how to
* make a widget. This is the main activity and opens up when
* the app icon is pressed.
 */

import android.app.Activity;
import android.os.Bundle;

public class InstructionsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);
    }
}
