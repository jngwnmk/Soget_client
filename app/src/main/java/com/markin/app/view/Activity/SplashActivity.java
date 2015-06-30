package com.markin.app.view.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.connector.user.UserLoginTask;
import com.markin.app.model.Authorization;
import com.markin.app.model.User;

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
        if(isTutorialNeed()){
            finish();
            startActivity(new Intent(SplashActivity.this, TutorialActivity.class));
        } else {
            autoLogin();
        }
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

    private boolean isTutorialNeed(){
        SharedPreferences sharedPreferences = getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE);
        boolean needTutorial = sharedPreferences.getBoolean("tutorial",true);
        return needTutorial;

    }

    private void handleSendText(Intent intent) {
        String shared_text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (shared_text != null) {
            shared_url = shared_text;
            Toast.makeText(getApplicationContext(), shared_url, Toast.LENGTH_SHORT).show();
            MainActivity.autoMarking = true;

        }
    }

    private void autoLogin(){
        user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        //Login info has
        if(user!=null){
            new UserLoginTask(SplashActivity.this, user).execute();
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