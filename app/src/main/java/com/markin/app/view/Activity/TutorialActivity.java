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
import com.markin.app.common.StaticValues;
import com.markin.app.view.Fragment.TutorialFourSlide;
import com.markin.app.view.Fragment.TutorialOneSlide;
import com.markin.app.view.Fragment.TutorialThreeSlide;
import com.markin.app.view.Fragment.TutorialTwoSlide;

/**
 * Created by wonmook on 15. 6. 12..
 */
public class TutorialActivity extends FragmentActivity {
    private static final int PAGE_NUM = 4;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.tutorial_layout);

        viewPager = (ViewPager)findViewById(R.id.tutorial_pager);
        pagerAdapter = new ScreenSlidePageAdapter(getSupportFragmentManager());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==3){
                    SharedPreferences sharedPreferences = getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("tutorial", false);
                    editor.commit();

                    Intent receiveIntent = getIntent();
                    if(receiveIntent!=null){
                        String invitation_num = receiveIntent.getStringExtra(StaticValues.INVITATIONNUM);
                        String invitation_username = receiveIntent.getStringExtra(StaticValues.INVITATIONUSERNAME);
                        if(invitation_num!=null && !invitation_num.equals("") && invitation_username!=null && !invitation_username.equals("")){
                            finish();

                            Intent introIntent = new Intent(TutorialActivity.this, IntroActivity.class);
                            introIntent.putExtra(StaticValues.INVITATIONNUM, invitation_num);
                            introIntent.putExtra(StaticValues.INVITATIONUSERNAME, invitation_username);
                            startActivity(introIntent);

                        } else {
                            finish();

                            Intent introIntent = new Intent(TutorialActivity.this, IntroActivity.class);
                            startActivity(introIntent);
                        }
                    } else {
                        finish();

                        Intent introIntent = new Intent(TutorialActivity.this, IntroActivity.class);
                        startActivity(introIntent);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                        viewPager.setCurrentItem(3);
                    }

                    @Override
                    public void prev() {
                        viewPager.setCurrentItem(1);
                    }

                    @Override
                    public void complete() {

                    }
                });
                return tutorialThreeSlide;
            } else if(position==3){
                TutorialFourSlide tutorialFourSlide = new TutorialFourSlide();
                return tutorialFourSlide;
            }
            return null;
        }

        @Override
        public int getCount() {
            return PAGE_NUM;
        }
    }
}
