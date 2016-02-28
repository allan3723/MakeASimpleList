package com.chenga.android.makeasimplelist;

import android.support.v4.app.Fragment;

public class SettingActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SettingFragment.newInstance();
    }
}
