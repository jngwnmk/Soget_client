package com.soget.soget_client.view.Activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.common.SettingManager;
import com.soget.soget_client.connector.LoginRequestTask;
import com.soget.soget_client.model.Authorization;
import com.soget.soget_client.model.User;
import com.soget.soget_client.view.Fragment.ArchiveFragment;
import com.soget.soget_client.view.Fragment.TabsFragment;

/**
 * Created by wonmook on 2015-03-18.
 */
public class MainActivity extends ActionBarActivity{

    private String shared_url = "";
    public static boolean autoMarking = false;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        //Change to Archive Tab
        FragmentManager fm = getFragmentManager();
        TabsFragment tabsFragment =(TabsFragment) fm.findFragmentById(R.id.tabs_fragment);
        if (tabsFragment != null)
            tabsFragment.onTabChanged(TabsFragment.TAB_ARCHIVE);


    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("onResume():MainActivity");
        Intent intent = getIntent();
        shared_url = intent.getExtras().getString("SHARED_URL");
        showAddDialog();

    }

    private void showAddDialog() {
        FragmentManager fm = getFragmentManager();
        ArchiveFragment archiveFragment = (ArchiveFragment)fm.findFragmentById(R.id.tab_2);
        if(archiveFragment!=null){
            if((!"".equals(shared_url))&&autoMarking){
                archiveFragment.showAddDialog(shared_url);
                shared_url = "";
                autoMarking = false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}
