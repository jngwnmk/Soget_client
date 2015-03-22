package com.soget.soget_client.view.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soget.soget_client.R;

/**
 * Created by wonmook on 2015-03-18.
 */
public class HomeFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.home_layout,container, false);
        return rootView;
    }
}
