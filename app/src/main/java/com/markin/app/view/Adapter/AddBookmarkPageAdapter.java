package com.markin.app.view.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.markin.app.view.Fragment.AddBookmarkBodyFirstFragment;
import com.markin.app.view.Fragment.AddBookmarkBodySecondFragment;

/**
 * Created by wonmook on 2016. 4. 28..
 */
public class AddBookmarkPageAdapter  extends FragmentStatePagerAdapter {
    private static final int PAGE_NUM = 2;
    private String currentCategory = "";

    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public AddBookmarkPageAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            AddBookmarkBodyFirstFragment addBookmarkBodyFirstFragment = AddBookmarkBodyFirstFragment.getInstance().setCateogry(getCurrentCategory());  //new AddBookmarkBodyFirstFragment().setCateogry(getCurrentCategory());

            return addBookmarkBodyFirstFragment;
        } else if(position==1){
            AddBookmarkBodySecondFragment addBookmarkBodySecondFragment = new AddBookmarkBodySecondFragment();

            return addBookmarkBodySecondFragment;
        }
        return null;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return PAGE_NUM;
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
}
