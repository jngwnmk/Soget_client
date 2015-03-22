package com.soget.soget_client.view.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.soget.soget_client.R;
import com.soget.soget_client.view.Fragment.ArchiveFragment;
import com.soget.soget_client.view.Fragment.FriendsFragment;
import com.soget.soget_client.view.Fragment.HomeFragment;

/**
 * Created by wonmook on 2015-03-18.
 */
public class MainActivity extends ActionBarActivity {

    ActionBar.Tab homeTab, archiveTab, friendTab;
    Fragment homeFragment = new HomeFragment();
    Fragment archiveFragment = new ArchiveFragment();
    Fragment friendFragment = new FriendsFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

    }




}
