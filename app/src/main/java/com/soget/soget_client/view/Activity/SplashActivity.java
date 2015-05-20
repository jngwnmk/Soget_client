package com.soget.soget_client.view.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.connector.LoginRequestTask;
import com.soget.soget_client.model.Authorization;
import com.soget.soget_client.model.User;
import com.soget.soget_client.view.Fragment.ArchiveFragment;

/**
 * Created by wonmook on 15. 5. 20..
 */
public class SplashActivity extends Activity implements OnTaskCompleted{

    private User user = null;
    private String shared_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        autoLogin();
    }

    @Override
    protected void onResume(){
        super.onResume();
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

    private void handleSendText(Intent intent) {
        String shared_text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (shared_text != null) {
            shared_url = shared_text;
            Toast.makeText(getApplicationContext(), shared_url, Toast.LENGTH_SHORT).show();
            MainActivity.autoMarking = true;
            /*FragmentManager fm = getFragmentManager();
            ArchiveFragment archiveFragment = (ArchiveFragment)fm.findFragmentById(R.id.tab_2);
            if(archiveFragment!=null){
                if(!"".equals(shared_url)){
                    archiveFragment.showAddDialog(shared_url);
                }

            }*/
        }
    }

    private void autoLogin(){
        user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        //Login info has
        if(user!=null){
            new LoginRequestTask(SplashActivity.this, user).execute();
        }
        //Doesn't
        else {
            finish();
            startActivity(new Intent(SplashActivity.this,IntroActivity.class));
        }
    }

    @Override
    public void onTaskCompleted(Object authorization) {
        //Login sucess
        if(authorization!=null){
            Log.d("MainActivity", ((Authorization) authorization).toString());
            //Save authorization info to shared preference
            AuthManager.getAuthManager().login(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE), user.getUserId(), user.getPassword(), user.getName(), user.getEmail(), ((Authorization) authorization).getAccess_token());
            finish();
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            Bundle extra = new Bundle();
            extra.putString("SHARED_URL",shared_url);
            intent.putExtras(extra);
            startActivity(intent);
        } else {
            //Login fail
            finish();
            startActivity(new Intent(SplashActivity.this,IntroActivity.class));
        }
    }
}