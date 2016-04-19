package com.markin.app.view.component.PagedHeadListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.markin.app.R;
import com.markin.app.view.component.PagedHeadListView.utils.DisplayUtils;


/**
 * Created by jorge on 5/08/14.
 */
public class PagedHeadIndicator extends AbstractPagedHeadIndicator {

    private View indicatorView;
    private Context context;
    private int lastPageOffset = 0;
    private int screenWidthPixels;

    public PagedHeadIndicator(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PagedHeadIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public PagedHeadIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    @Override
    public void init() {

        screenWidthPixels = DisplayUtils.getScreenWidthPixels(context);

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.indicator_height));
        setLayoutParams(layoutParams);

        setBackgroundColor(getResources().getColor(R.color.white)); //default indicator bg color

        addIndicatorView();
    }

    private void addIndicatorView() {
        LinearLayout.LayoutParams indicatorParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);

        indicatorView = new View(context);
        indicatorView.setBackgroundColor(getResources().getColor(R.color.black)); //default indicator color

        addView(indicatorView, indicatorParams);
    }

    @Override
    public void addPage() {
        pageCount++;
        recalculateIndicatorWidth();
    }

    /**
     * Recalculates indicator width each time a new page is added to the pager
     */
    private void recalculateIndicatorWidth() {
        int newWidth = screenWidthPixels / pageCount;
        LayoutParams indicatorParams = (LayoutParams) indicatorView.getLayoutParams();
        indicatorParams.width = newWidth;
        indicatorView.setLayoutParams(indicatorParams);
    }

    @Override
    public void setBgColor(int bgColor) {
        setBackgroundColor(bgColor);
    }

    @Override
    public void setColor(int indicatorColor) {
        indicatorView.setBackgroundColor(indicatorColor);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        int relativePositionOffset = 0;
        if (pageCount != 0) {
            relativePositionOffset = indicatorView.getWidth() * position + positionOffsetPixels / pageCount;
        }

        if (positionOffset != 0) {
            LayoutParams indicatorParams = (LayoutParams) indicatorView.getLayoutParams();
            indicatorParams.leftMargin = relativePositionOffset;
            indicatorView.setLayoutParams(indicatorParams);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
