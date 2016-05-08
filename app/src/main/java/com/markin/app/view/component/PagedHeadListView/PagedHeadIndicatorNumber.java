package com.markin.app.view.component.PagedHeadListView;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
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
        currentPageTv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/FrutigerLTStd-Bold.otf"));
        currentPageTv.setTextColor(context.getResources().getColor(R.color.sub_text_color));

        totalPageTv = (TextView)indicatorView.findViewById(R.id.recommend_total_num);
        totalPageTv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/FrutigerLTStd-Bold.otf"));

        addView(indicatorView, indicatorParams);
    }

    @Override
    public void addPage() {
        pageCount++;
        totalPageTv.setText(" / " + String.valueOf(pageCount));
    }

    @Override
    public void removePage() {
        pageCount--;
        totalPageTv.setText(" / " + String.valueOf(pageCount));
    }

    @Override
    public void setBgColor(int bgColor) {

    }

    @Override
    public void setColor(int indicatorColor) {

    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        currentPageTv.setText(String.valueOf(position + 1));
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*@Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i("", "onPageScrolled: " + position);

        CampaignPagerFragment sampleFragment = (CampaignPagerFragment) ((CampaignPagerAdapter) getAdapter()).getRegisteredFragment(position);


        float scale = 1 - (positionOffset * RATIO_SCALE);

        // Just a shortcut to findViewById(R.id.image).setScale(scale);
        sampleFragment.scaleImage(scale);


        if (position + 1 < pager.getAdapter().getCount()) {
            sampleFragment = (CampaignPagerFragment) ((CampaignPagerAdapter) pager.getAdapter()).getRegisteredFragment(position + 1);
            scale = positionOffset * RATIO_SCALE + (1 - RATIO_SCALE);
            sampleFragment.scaleImage(scale);
        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.i("", "onPageSelected: " + position);
        currentPageTv.setText(String.valueOf(position + 1));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i("", "onPageScrollStateChanged: " + state);
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            CampaignPagerFragment fragment = (CampaignPagerFragment) ((CampaignPagerAdapter) pager.getAdapter()).getRegisteredFragment(pager.getCurrentItem());
            fragment.scaleImage(1);
            if (pager.getCurrentItem() > 0) {
                fragment = (CampaignPagerFragment) ((CampaignPagerAdapter) pager.getAdapter()).getRegisteredFragment(pager.getCurrentItem() - 1);
                fragment.scaleImage(1 - RATIO_SCALE);
            }

            if (pager.getCurrentItem() + 1 < pager.getAdapter().getCount()) {
                fragment = (CampaignPagerFragment) ((CampaignPagerAdapter) pager.getAdapter()).getRegisteredFragment(pager.getCurrentItem() + 1);
                fragment.scaleImage(1 - RATIO_SCALE);
            }
        }

    }*/
}
