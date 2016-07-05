package com.markin.app.view.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.markin.app.model.Bookmark;
import com.markin.app.model.Friend;
import com.markin.app.view.Fragment.RecommendFragment;
import com.markin.app.view.Fragment.TutorialOneSlide;

import java.util.ArrayList;

/**
 * Created by jorge on 3/17/14.
 */
public class RecommendViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private int count;
    private long baseId = 0;

    public RecommendViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<Fragment>();
        count = 0;
    }

    @Override
    public Fragment getItem(int position) {

        //if (fragments.size() == 0) {
          //  return new RecommendFragment();
        //}

        return fragments.get(position);

        /*Fragment page = null;
        if (fragments.size() > position) {
            page = fragments.get(position);
            if (page != null) {
                return page;
            }
        }

        while (position>=fragments.size()) {
            fragments.add(null);
        }
        page = fragments.get(position);
        fragments.set(position, page);
        return page;*/

    }

    public void addFragment(Fragment fragmentToAdd) {
        fragments.add(fragmentToAdd);
        Log.d("checkRecommend", "addFragment Size : " + fragments.size());

        count++;
        //notifyDataSetChanged();
    }

    public void removeFragment(String bookmark_id){

        for(int i = 0 ; i < fragments.size() ; ++i){
            Bundle bundle = fragments.get(i).getArguments();
            Bookmark bookmark = bundle.getParcelable("bookmark");
            if(bookmark.getId().equals(bookmark_id)){
                fragments.remove(i);
                count--;
                //notifyDataSetChanged();
                break;
            }
        }
        Log.d("checkRecommend", "Fragment Size : " + fragments.size());
        for(int i = 0 ; i < fragments.size() ; ++i){
            Log.d("checkRecommend", "ID : " + fragments.get(i).getTag());

        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        if (position >= getCount()) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commit();
        }



    }

    @Override
    public int getItemPosition(Object object) {
        /*if (fragments.contains((Fragment) object)){
           return fragments.indexOf((Fragment) object);
        } else {
            return PagerAdapter.POSITION_NONE;
        }*/

        int index = fragments.indexOf((Fragment) object);

        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public long getItemId(int position) {
        // give an ID different from position when position has been changed
        Log.d("checkRecommend", "getItemId : " + (baseId + position));
        return baseId + position;
    }

    public void notifyChangeInPosition(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments

        baseId += getCount();
        Log.d("checkRecommend", "notifyChangeInPosition : " + (baseId ));

    }
}
