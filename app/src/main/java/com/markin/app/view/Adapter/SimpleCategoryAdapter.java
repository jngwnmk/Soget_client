package com.markin.app.view.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by wonmook on 2016. 4. 28..
 */
public class SimpleCategoryAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<String> categories;


    public SimpleCategoryAdapter(Context context, ArrayList<String> categories) {
        this.mContext = context;
        this.categories = categories;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CategoryWrapper categoryWrapper = null;
        if (row == null) {
            row = inflater.inflate(R.layout.category_simple_list_row, null);
            categoryWrapper = new CategoryWrapper(row);
            row.setTag(categoryWrapper);
        } else {
            categoryWrapper = (CategoryWrapper) row.getTag();

        }

        categoryWrapper.getTypeTv().setText((String)getItem(position));
        categoryWrapper.getTypeTv().setTextColor(mContext.getResources().getColor(R.color.charcol_text_color_80));
        categoryWrapper.getTypeTv().setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/FrutigerLTStd-Bold.otf"));


        return row;
    }

    private class CategoryWrapper {
        private View base;
        private TextView type;


        public CategoryWrapper(View base) {
            this.base = base;
        }

        public TextView getTypeTv() {
            if (type == null) {
                type = (TextView) base.findViewById(R.id.category_type_tv);
                type.setTextColor(mContext.getResources().getColor(R.color.category_text_charcol));
            }
            return type;
        }

    }
}