package com.soget.soget_client.view.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soget.soget_client.R;
import com.soget.soget_client.view.Adapter.HomeAdapter;

/**
 * Created by wonmook on 2015-03-18.
 */
public class HomeFragment extends Fragment{
    private ViewPager viewPager = null;
    private HomeAdapter homeAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.home_layout,container, false);
        viewPager = (ViewPager)rootView.findViewById(R.id.pager);
        homeAdapter = new HomeAdapter();
        viewPager.setAdapter(homeAdapter);

        return rootView;
    }
}
