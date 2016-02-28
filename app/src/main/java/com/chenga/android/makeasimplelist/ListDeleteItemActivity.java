package com.chenga.android.makeasimplelist;

import android.support.v4.app.Fragment;

public class ListDeleteItemActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ListDeleteItemFragment.newInstance();
    }
}
