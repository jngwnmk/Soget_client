package com.markin.app.callback;

import android.support.v4.app.Fragment;

/**
 * Created by wonmook on 2016. 4. 24..
 */
public interface FragmentChangeListener {
    /**
     * Callback method when the fragment is changed.
     * @param fragment
     */
     void replaceFragment(Fragment fragment);
}
