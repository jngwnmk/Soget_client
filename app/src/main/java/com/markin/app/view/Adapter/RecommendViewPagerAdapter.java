package com.markin.app.view.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.markin.app.model.Bookmark;
import com.markin.app.model.Friend;
import com.markin.app.view.Fragment.RecommendFragment;
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
            return new RecommendFragment();
        }

        return fragments.get(position);
    }

    public void addFragment(Fragment fragmentToAdd) {
        fragments.add(fragmentToAdd);
        count++;
        notifyDataSetChanged();
    }

    public void removeFragment(String bookmark_id){

        for(int i = 0 ; i < fragments.size() ; ++i){
            Bundle bundle = fragments.get(i).getArguments();
            Bookmark bookmark = bundle.getParcelable("bookmark");
            if(bookmark.getId().equals(bookmark_id)){
                fragments.remove(i);
                count--;
                notifyDataSetChanged();
                break;
            }
        }

    }

    @Override
    public int getItemPosition(Object object) {
        if (fragments.contains((Fragment) object)){
            return fragments.indexOf((Fragment) object);
        } else {
            return POSITION_NONE;
        }
    }

    @Override
    public int getCount() {
        return count;
    }

}
