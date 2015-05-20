package com.soget.soget_client.view.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.connector.LoginRequestTask;
import com.soget.soget_client.connector.RegisterRequestTask;
import com.soget.soget_client.model.Authorization;
import com.soget.soget_client.model.User;

/**
 * Created by wonmook on 2015-03-15.
 */
public class RegisterActivity extends ActionBarActivity implements OnTaskCompleted {

    private EditText invitation_code_edit;
    private EditText user_name_edit;
    private EditText user_id_edit;
    private EditText user_email_edit;
    private EditText user_pwd_edit;
    private ImageButton register_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout_temp);

        setLayout();
    }

    private void setLayout(){
        invitation_code_edit = (EditText)findViewById(R.id.invitation_code_edit);
        user_name_edit = (EditText)findViewById(R.id.user_name_edit);
        user_id_edit = (EditText)findViewById(R.id.user_id_edit);
        user_email_edit = (EditText)findViewById(R.id.user_email_edit);
        user_pwd_edit = (EditText)findViewById(R.id.user_pwd_edit);
        register_btn = (ImageButton)findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User new_user = new User();
                new_user.setInvitationCode(invitation_code_edit.getText().toString());
                new_user.setName(user_name_edit.getText().toString());
                new_user.setUserId(user_id_edit.getText().toString());
                new_user.setEmail(user_email_edit.getText().toString());
                new_user.setPassword(user_pwd_edit.getText().toString());
                new_user.setFacebookProfile(user_email_edit.getText().toString());

                new RegisterRequestTask(RegisterActivity.this, new_user).execute();
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
    public void onTaskCompleted(final Object user) {
        if(user!=null){
            AuthManager.getAuthManager().register(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE),((User)user).getUserId(),((User)user).getPassword(),((User)user).getName(), ((User)user).getEmail());

            new LoginRequestTask(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(Object authorization) {
                    AuthManager.getAuthManager().login(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE),((User)user).getUserId(),((User)user).getPassword(),((User)user).getName(),((User)user).getEmail(),((Authorization)authorization).getAccess_token());
                    finish();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            }, (User)user).execute();
        }
        Log.d("RegisterActivity", user.toString());
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(RegisterActivity.this, IntroActivity.class));
    }

}
