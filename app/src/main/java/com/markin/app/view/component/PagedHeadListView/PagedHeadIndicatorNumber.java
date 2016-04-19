package com.markin.app.view.component.PagedHeadListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.view.component.PagedHeadListView.utils.DisplayUtils;

/**
 * Created by wonmook on 2016. 4. 13..
 */
public class PagedHeadIndicatorNumber extends AbstractPagedHeadIndicator{


    private LinearLayout indicatorView;
    private TextView totalPageTv;
    private TextView currentPageTv;
    private Context context;
    private int lastPageOffset = 0;
    private int screenWidthPixels;

    public PagedHeadIndicatorNumber(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PagedHeadIndicatorNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public PagedHeadIndicatorNumber(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    @Override
    public void init() {
        screenWidthPixels = DisplayUtils.getScreenWidthPixels(context);

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);

        setBackgroundColor(getResources().getColor(R.color.transparent)); //default indicator bg color

        addIndicatorView();
    }

    private void addIndicatorView() {
        LinearLayout.LayoutParams indicatorParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        indicatorView = (LinearLayout) inflater.inflate(R.layout.recommend_num_indicator, null);
        currentPageTv = (TextView)indicatorView.findViewById(R.id.recommend_current_idx);
        totalPageTv = (TextView)indicatorView.findViewById(R.id.recommend_total_num);
        addView(indicatorView, indicatorParams);
    }

    @Override
    public void addPage() {
        pageCount++;
        totalPageTv.setText(String.valueOf(pageCount));
    }

    @Override
    public void setBgColor(int bgColor) {

    }

    @Override
    public void setColor(int indicatorColor) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPageTv.setText(String.valueOf(position+1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
