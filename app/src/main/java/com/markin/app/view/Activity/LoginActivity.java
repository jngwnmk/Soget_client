package com.markin.app.view.Activity;

/**
 * Created by wonmook on 2015-03-16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.user.UserInfoGetTask;
import com.markin.app.connector.user.UserLoginTask;
import com.markin.app.model.Authorization;
import com.markin.app.model.User;


/**
 * Created by wonmook on 2015-03-15.
 */
public class LoginActivity extends Activity implements OnTaskCompleted {


    private User user;
    private LinearLayout login_root_layout;
    private EditText user_id_edit;
    private EditText user_pwd_edit;
    private Button login_btn;
    private SharedPreferences sharedPreferences;
    private TextView pwd_forget_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        setLayout();
    }

    private void setLayout(){

        login_root_layout = (LinearLayout)findViewById(R.id.login_root_layout);
        login_root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        user_id_edit = (EditText)findViewById(R.id.user_id_edit);
        user_pwd_edit = (EditText)findViewById(R.id.user_pwd_edit);
        user_id_edit.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));
        user_pwd_edit.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));


        user_id_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){

                    if(user_id_edit.getText().toString().equals("")){
                        user_id_edit.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.line_default);
                        user_id_edit.setTextColor(getResources().getColor(R.color.white));
                        user_id_edit.setError("Please Enter User Id");
                    } else {
                        user_id_edit.setError(null);
                        user_id_edit.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.line_done);
                        user_id_edit.setTextColor(getResources().getColor(R.color.white_33));
                    }
                } else {
                    user_id_edit.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.line_highlight);
                    user_id_edit.setTextColor(getResources().getColor(R.color.sub_text_color_80));
                }
            }
        });

        user_pwd_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (user_pwd_edit.getText().toString().equals("")) {
                        user_pwd_edit.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.line_default);
                        user_pwd_edit.setTextColor(getResources().getColor(R.color.white));
                        user_pwd_edit.setError("Please Enter Password");
                    } else {
                        user_pwd_edit.setError(null);
                        user_pwd_edit.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.line_done);
                        user_pwd_edit.setTextColor(getResources().getColor(R.color.white_33));
                    }
                } else {
                    user_pwd_edit.setCompoundDrawablesWithIntrinsicBounds(0,0,0,R.drawable.line_highlight);
                    user_pwd_edit.setTextColor(getResources().getColor(R.color.sub_text_color_80));
                }
            }
        });

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
                new UserLoginTask(LoginActivity.this, user).execute();
            }
        });


        pwd_forget_tv = (TextView)findViewById(R.id.pwd_forget_tv);
        pwd_forget_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "forget Password", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, NormalWebViewActivity.class);
                intent.putExtra(NormalWebViewActivity.WEBVIEWURL, StaticValues.BLOG_URL);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

            }
        });
        pwd_forget_tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

    }

    private void hideKeyboard(){

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

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
        //Login success
        if(authorization!=null){
            Log.d("LoginActivity", ((Authorization) authorization).toString());
            //Save authorization info to shared preference
            getUserInfo(user.getUserId(), ((Authorization) authorization).getAccess_token());
        } else {
            Toast.makeText(getApplicationContext(),"Wrong Login Info", Toast.LENGTH_SHORT).show();
        }

    }

    private void getUserInfo(String user_id, final String token){
        //String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new UserInfoGetTask(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    user = (User)object;
                    AuthManager.getAuthManager().login(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE), user.getUserId(), user.getPassword(), user.getName(), user.getEmail(), token);
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        }, user_id, token).execute();
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(LoginActivity.this, IntroActivity.class));
    }

}

