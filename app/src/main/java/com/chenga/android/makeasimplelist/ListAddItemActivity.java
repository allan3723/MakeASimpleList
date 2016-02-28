package com.chenga.android.makeasimplelist;

import android.support.v4.app.Fragment;

public class ListAddItemActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ListAddItemFragment.newInstance();
    }
}
