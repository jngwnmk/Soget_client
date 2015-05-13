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
public class MainActivity extends ActionBarActivity implements OnTaskCompleted {

    //ActionBar.Tab homeTab, archiveTab, friendTab;
    //Fragment homeFragment = new HomeFragment();
    //Fragment archiveFragment = new ArchiveFragment();
    //Fragment friendFragment = new FriendsFragment();

    private String shared_url = "";
    private User user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);



        //Auto Login
        autoLogin();


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
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        //When shared url is received.
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }
    }
    private void autoLogin(){
        user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        //Login info has
        if(user!=null){
            new LoginRequestTask(MainActivity.this, user).execute();
        }
        //Doesn't
        else {
            finish();
            startActivity(new Intent(MainActivity.this,IntroActivity.class));
        }
    }

    private void handleSendText(Intent intent) {
        String shared_text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (shared_text != null) {
            shared_url = shared_text;
            Toast.makeText(getApplicationContext(),shared_url,Toast.LENGTH_SHORT).show();

            FragmentManager fm = getFragmentManager();
            ArchiveFragment archiveFragment = (ArchiveFragment)fm.findFragmentById(R.id.tab_2);
            if(archiveFragment!=null){
                if(!"".equals(shared_url)){
                    archiveFragment.showAddDialog(shared_url);
                }

            }
        }
    }

    @Override
    public void onTaskCompleted(Object authorization) {
        //Login sucess
        if(authorization!=null){
            Log.d("MainActivity", ((Authorization) authorization).toString());
            //Save authorization info to shared preference
            AuthManager.getAuthManager().login(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE), user.getUserId(), user.getPassword(), ((Authorization) authorization).getAccess_token());
        } else {
            //Login fail
            finish();
            startActivity(new Intent(MainActivity.this,IntroActivity.class));
        }

    }

    @Override
    public void onBackPressed() {
        finish();

    }
}
