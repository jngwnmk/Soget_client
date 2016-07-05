package com.markin.app.view.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.markin.app.R;
import com.markin.app.view.Adapter.RecommendViewPagerAdapter;
import com.markin.app.view.Fragment.RecommendFragment;
import com.markin.app.view.component.PagedHeadListView.AbstractPagedHeadIndicator;
import com.markin.app.view.component.PagedHeadListView.PagedHeadIndicator;
import com.markin.app.view.component.PagedHeadListView.PagedHeadIndicatorNumber;
import com.markin.app.view.component.PagedHeadListView.pagetransformers.AccordionPageTransformer;
import com.markin.app.view.component.PagedHeadListView.pagetransformers.DepthPageTransformer;
import com.markin.app.view.component.PagedHeadListView.pagetransformers.FlipPageTransformer;
import com.markin.app.view.component.PagedHeadListView.pagetransformers.RotationPageTransformer;
import com.markin.app.view.component.PagedHeadListView.pagetransformers.ScalePageTransformer;
import com.markin.app.view.component.PagedHeadListView.pagetransformers.ZoomOutPageTransformer;
import com.markin.app.view.component.PagedHeadListView.utils.IndicatorTypes;
import com.markin.app.view.component.PagedHeadListView.utils.PageTransformerTypes;

/**
 * Created by wonmook on 2016. 4. 19..
 */
public class RecommendView extends FrameLayout{


    public final static float RATIO_SCALE = 0.7f;

    private static String TAG = "RecommendView";
    private View headerView;
    private ViewPager mPager;
    private RecommendViewPagerAdapter headerViewPagerAdapter;
    private AbstractPagedHeadIndicator indicator;
    //Custom attrs
    private float headerHeight;
    private boolean disableVerticalTouchOnHeader;
    private int indicatorBgColor;
    private int indicatorColor;
    private int indicatorType;
    private int pageTransformer;

    /**
     * Inner listener defined to be used if disableVerticalTouchOnHeader attr is set to true
     */
    private OnTouchListener touchListenerForHeaderIntercept = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            requestDisallowInterceptTouchEvent(true);

            return false;
        }
    };

    public RecommendView(Context context) {
        super(context);
        init(null);
    }

    public RecommendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RecommendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PagedHeadListView);

            headerHeight = a.getDimensionPixelSize(R.styleable.PagedHeadListView_headerHeight, 300);

            disableVerticalTouchOnHeader = a.getBoolean(R.styleable.PagedHeadListView_disableVerticalTouchOnHeader, false);
            indicatorBgColor = a.getColor(R.styleable.PagedHeadListView_indicatorBgColor, getResources().getColor(R.color.white));
            indicatorColor = a.getColor(R.styleable.PagedHeadListView_indicatorColor, getResources().getColor(R.color.black));
            indicatorType = a.getInt(R.styleable.PagedHeadListView_indicatorType, IndicatorTypes.NUMBER.ordinal());
            pageTransformer = a.getInt(R.styleable.PagedHeadListView_pageTransformer, PageTransformerTypes.NORMAL.ordinal());

            a.recycle();
        }

        initializePagedHeader();
    }

    private void initializePagedHeader() {

        headerView = View.inflate(getContext(), R.layout.paged_header, null);

        mPager = (ViewPager) headerView.findViewById(R.id.headerViewPager);
        //int marginPx = getResources().getDimensionPixelSize(R.dimen.page_margin);
        //mPager.setPadding(marginPx, 0, marginPx, 0);
        //mPager.setClipToPadding(false);
        //mPager.setPageMargin(marginPx);

        mPager.setClipToPadding(false);
        mPager.setPageMargin(24);
        mPager.setPadding(68, 28, 68, 28);
        mPager.setOffscreenPageLimit(10);
        //mPager.setOffscreenPageLimit(3);


        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        headerViewPagerAdapter = new RecommendViewPagerAdapter(fragmentManager);

        if(indicatorType==3){
            indicator = (PagedHeadIndicatorNumber)headerView.findViewById(R.id.headerViewIndicator);

        } else {
            indicator = new PagedHeadIndicator(getContext());
            indicator.setBgColor(indicatorBgColor);
            indicator.setColor(indicatorColor);

        }

        addView(headerView);
        mPager.setAdapter(headerViewPagerAdapter);
        mPager.setOnPageChangeListener(indicator);
        /*mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("", "onPageScrolled: " + position);

                RecommendFragment currentFragment = (RecommendFragment) ((RecommendViewPagerAdapter) mPager.getAdapter()).getItem(position);


                float scale = 1 - (positionOffset * RATIO_SCALE);

                // Just a shortcut to findViewById(R.id.image).setScale(scale);
                currentFragment.scaleImage(scale);


                if (position + 1 < mPager.getAdapter().getCount()) {
                    currentFragment = (RecommendFragment) ((RecommendViewPagerAdapter) mPager.getAdapter()).getItem(position + 1);
                    scale = positionOffset * RATIO_SCALE + (1 - RATIO_SCALE);
                    currentFragment.scaleImage(scale);
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("", "onPageSelected: " + position);
                //currentPageTv.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("", "onPageScrollStateChanged: " + state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    RecommendFragment fragment = (RecommendFragment) ((RecommendViewPagerAdapter) mPager.getAdapter()).getItem(mPager.getCurrentItem());
                    fragment.scaleImage(1);
                    if (mPager.getCurrentItem() > 0) {
                        fragment = (RecommendFragment) ((RecommendViewPagerAdapter) mPager.getAdapter()).getItem(mPager.getCurrentItem() - 1);
                        fragment.scaleImage(1 - RATIO_SCALE);
                    }

                    if (mPager.getCurrentItem() + 1 < mPager.getAdapter().getCount()) {
                        fragment = (RecommendFragment) ((RecommendViewPagerAdapter) mPager.getAdapter()).getItem(mPager.getCurrentItem() + 1);
                        fragment.scaleImage(1 - RATIO_SCALE);
                    }
                }

            }
        });

        if (disableVerticalTouchOnHeader)
            mPager.setOnTouchListener(touchListenerForHeaderIntercept);*/

        //setHeaderPageTransformer(PageTransformerTypes.values()[pageTransformer]);

    }

    public void setHeaderPageTransformer(PageTransformerTypes pageTransformerType) {
        if (pageTransformerType.equals(PageTransformerTypes.ZOOMOUT)) {
            mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        }
        else if (pageTransformerType.equals(PageTransformerTypes.ROTATE)) {
            mPager.setPageTransformer(true, new RotationPageTransformer());
        }
        else if (pageTransformerType.equals(PageTransformerTypes.SCALE)) {
            mPager.setPageTransformer(true, new ScalePageTransformer());
        }
        else if (pageTransformerType.equals(PageTransformerTypes.FLIP)) {
            mPager.setPageTransformer(true, new FlipPageTransformer());
        }
        else if (pageTransformerType.equals(PageTransformerTypes.ACCORDION)) {
            mPager.setPageTransformer(true, new AccordionPageTransformer());
        } else if (pageTransformerType.equals(PageTransformerTypes.DEPTH)){
            mPager.setPageTransformer(true, new DepthPageTransformer());
        } else if(pageTransformerType.equals(PageTransformerTypes.NORMAL)){
            mPager.setPageTransformer(false, null);
        }

    }

    /**
     * Created to allow custom page transformers supplied by the users
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param customPageTransformer PageTransformer that will modify each page's animation properties
     */
    public void setHeaderPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer customPageTransformer) {
        mPager.setPageTransformer(reverseDrawingOrder, customPageTransformer);
    }

    /**
     * Mapped to allow users to listen to page change events
     * @param onPageChangeListener
     */
    public void setOnHeaderPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mPager.setOnPageChangeListener(onPageChangeListener);
    }

    public void addFragmentToHeader(Fragment fragmentToAdd) {
        indicator.addPage();
        headerViewPagerAdapter.addFragment(fragmentToAdd);
        //headerViewPagerAdapter.notifyChangeInPosition(1);
        headerViewPagerAdapter.notifyDataSetChanged();
    }

    public void removeFragmentFromHeader(String bookmark_id){
        indicator.removePage();
        headerViewPagerAdapter.removeFragment(bookmark_id);
        headerViewPagerAdapter.notifyDataSetChanged();
        //headerViewPagerAdapter.notifyChangeInPosition(1);


    }

    public void setHeaderOffScreenPageLimit(int offScreenPageLimit){
        mPager.setOffscreenPageLimit(offScreenPageLimit);
    }

    public void setIndicatorBgColor(int bgColor) {
        indicator.setBgColor(bgColor);
    }

    public void setIndicatorColor(int color) {
        indicator.setColor(color);
    }

    public void disableVerticalTouchOnHeader() {
        mPager.setOnTouchListener(null);
        mPager.setOnTouchListener(touchListenerForHeaderIntercept);
    }

    /**
     * Height in pixels for the header
     * @param newHeaderHeight
     */
    public void setHeaderHeight(int newHeaderHeight) {
        AbsListView.LayoutParams headerViewParams = (AbsListView.LayoutParams) headerView.getLayoutParams();
        headerViewParams.height = newHeaderHeight;
        headerView.setLayoutParams(headerViewParams);
    }

    public void showNumberIndicator(){
        if(indicator.getVisibility()!=View.VISIBLE){
            indicator.setVisibility(View.VISIBLE);
            //mPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 750));

        }
    }

    public void hideNumberIndicator(){
        if(indicator.getVisibility()!=View.GONE){
            indicator.setVisibility(View.GONE);
            //mPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 900));
        }

    }

    public int getPageCount(){
        return mPager.getAdapter().getCount();
    }
}
