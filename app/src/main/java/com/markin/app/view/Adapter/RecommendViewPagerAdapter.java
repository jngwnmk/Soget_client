package com.markin.app.view.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.markin.app.view.Fragment.TutorialOneSlide;

import java.util.ArrayList;

/**
 * Created by jorge on 3/17/14.
 */
public class RecommendViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragments;
    private int count;

    public RecommendViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<Fragment>();
        count = 0;
    }

    @Override
    public Fragment getItem(int position) {

        if (fragments.size() == 0) {
            return new TutorialOneSlide();
        }

        return fragments.get(position);
    }

    public void addFragment(Fragment fragmentToAdd) {
        fragments.add(fragmentToAdd);
        count++;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return count;
    }
}
