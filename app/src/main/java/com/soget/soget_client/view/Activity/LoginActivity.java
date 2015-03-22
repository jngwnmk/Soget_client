package com.soget.soget_client.view.Activity;

/**
 * Created by wonmook on 2015-03-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.connector.LoginRequestTask;
import com.soget.soget_client.model.Authorization;
import com.soget.soget_client.model.User;


/**
 * Created by wonmook on 2015-03-15.
 */
public class LoginActivity extends Activity implements OnTaskCompleted {

    private User user;
    private EditText user_id_edit;
    private EditText user_pwd_edit;
    private Button login_btn;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        setLayout();
    }

    private void setLayout(){
        user_id_edit = (EditText)findViewById(R.id.user_id_edit);
        user_pwd_edit = (EditText)findViewById(R.id.user_pwd_edit);
        if(user!=null){
            user_id_edit.setText(user.getUserId());
            user_pwd_edit.setText(user.getPassword());
        } else {
            user = new User();
        }
        login_btn = (Button)findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUserId(user_id_edit.getText().toString());
                user.setPassword(user_pwd_edit.getText().toString());
                new LoginRequestTask(LoginActivity.this, user).execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTaskCompleted(Object authorization) {
        Log.d("LoginActivity", ((Authorization)authorization).toString());
        //Save authorization info to shared preference
        AuthManager.getAuthManager().login(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE),user.getUserId(),user.getPassword(),((Authorization)authorization).getAccess_token());
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

}
