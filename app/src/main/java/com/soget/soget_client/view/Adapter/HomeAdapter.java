package com.soget.soget_client.view.Adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soget.soget_client.model.Bookmark;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-04-06.
 */
public class HomeAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Bookmark> bookmarks;

    public HomeAdapter(Context context, ArrayList<Bookmark> bookmarks){
        this.mContext = context;
        this.bookmarks = bookmarks;
        this.inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return bookmarks.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int pos){
        View page = null;

        return page;
    }

}
