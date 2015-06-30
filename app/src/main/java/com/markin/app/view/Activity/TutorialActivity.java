package com.markin.app.view.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.markin.app.R;
import com.markin.app.callback.PageMove;
import com.markin.app.common.AuthManager;
import com.markin.app.view.Fragment.TutorialOneSlide;
import com.markin.app.view.Fragment.TutorialThreeSlide;
import com.markin.app.view.Fragment.TutorialTwoSlide;

/**
 * Created by wonmook on 15. 6. 12..
 */
public class TutorialActivity extends FragmentActivity {
    private static final int PAGE_NUM = 3;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.tutorial_layout);

        viewPager = (ViewPager)findViewById(R.id.tutorial_pager);
        pagerAdapter = new ScreenSlidePageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);


    }

    private class ScreenSlidePageAdapter extends FragmentStatePagerAdapter{
        public ScreenSlidePageAdapter(FragmentManager fm){
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                TutorialOneSlide tutorialOneSlide = new TutorialOneSlide();
                tutorialOneSlide.setPageMove(new PageMove() {
                    @Override
                    public void next() {
                        viewPager.setCurrentItem(1);
                    }

                    @Override
                    public void prev() {

                    }

                    @Override
                    public void complete() {

                    }
                });
                return tutorialOneSlide;
            } else if(position==1){
                TutorialTwoSlide tutorialTwoSlide = new TutorialTwoSlide();
                tutorialTwoSlide.setPageMove(new PageMove() {
                    @Override
                    public void next() {
                        viewPager.setCurrentItem(2);
                    }

                    @Override
                    public void prev() {
                        viewPager.setCurrentItem(0);
                    }

                    @Override
                    public void complete() {

                    }
                });
                return tutorialTwoSlide;
            } else if(position==2){
                TutorialThreeSlide tutorialThreeSlide = new TutorialThreeSlide();
                tutorialThreeSlide.setPageMove(new PageMove() {
                    @Override
                    public void next() {

                    }

                    @Override
                    public void prev() {
                        viewPager.setCurrentItem(1);
                    }

                    @Override
                    public void complete() {
                        SharedPreferences sharedPreferences = getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("tutorial", false);
                        editor.commit();
                        finish();
                        startActivity(new Intent(TutorialActivity.this, IntroActivity.class));
                    }
                });
                return tutorialThreeSlide;
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_NUM;
        }
    }
}
