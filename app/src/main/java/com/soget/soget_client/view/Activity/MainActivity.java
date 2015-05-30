package com.soget.soget_client.view.Activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.soget.soget_client.R;
import com.soget.soget_client.view.Adapter.DiscoverAdapter;
import com.soget.soget_client.view.Fragment.ArchiveFragment;
import com.soget.soget_client.view.Fragment.DiscoverFragment;
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
            tabsFragment.onTabChanged(TabsFragment.TAB_HOME);


    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("onResume():MainActivity");
        Intent intent = getIntent();
        if(intent!=null){
            if(intent.getExtras()!=null){
                shared_url = intent.getExtras().getString("SHARED_URL");
            }
        }
        showAddDialog();

    }

    private void showAddDialog() {
        FragmentManager fm = getFragmentManager();
        DiscoverFragment discoverFragment = (DiscoverFragment)fm.findFragmentById(R.id.tab_1);
        if(discoverFragment!=null){
            if((!"".equals(shared_url))&&autoMarking){
                discoverFragment.showAddDialog(shared_url,null);
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
