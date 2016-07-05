package com.markin.app.view.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.markin.app.R;
import com.markin.app.view.Adapter.AddBookmarkPageAdapter;

/**
 * Created by wonmook on 2016. 7. 3..
 */
public class AddFriendDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.add_bookmark_dialog_1, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.popup_black_background);


        return rootView;
    }
}
