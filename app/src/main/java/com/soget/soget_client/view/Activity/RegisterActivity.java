package com.soget.soget_client.view.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.connector.invitation.InvitationCodeCheckTask;
import com.soget.soget_client.connector.user.UserCheckIdDuplicateTask;
import com.soget.soget_client.connector.user.UserLoginTask;
import com.soget.soget_client.connector.user.UserRegisterTask;
import com.soget.soget_client.model.Authorization;
import com.soget.soget_client.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TextView register_tv;
    private TextView register_info_tv;
    private TextView register_condition_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout_temp);

        setLayout();
    }

    private void setLayout(){
        invitation_code_edit = (EditText)findViewById(R.id.invitation_code_edit);
        invitation_code_edit.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-SemiBold.otf"));
        invitation_code_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Toast.makeText(getApplicationContext(), "체크", Toast.LENGTH_SHORT).show();

                    if(!invitation_code_edit.getText().toString().equals("")){
                        new InvitationCodeCheckTask(new OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted(Object object) {
                                if(object!=null){
                                    Boolean result = (Boolean)object;
                                    if(!result){
                                        invitation_code_edit.setError("Invalid Invitation Code");
                                    }
                                    Toast.makeText(getApplicationContext(), ((Boolean)object).toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, invitation_code_edit.getText().toString()).execute();
                    } else {
                        invitation_code_edit.setError("Please Enter Invitation Code");
                    }
                }
            }
        });

        user_name_edit = (EditText)findViewById(R.id.user_name_edit);
        user_name_edit.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-SemiBold.otf"));
        user_name_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (user_name_edit.getText().toString().equals("")) {
                        user_name_edit.setError("Please Enter Your Name");
                    }
                }
            }
        });
        user_id_edit = (EditText)findViewById(R.id.user_id_edit);
        user_id_edit.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-SemiBold.otf"));
        user_id_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!user_id_edit.getText().toString().equals("")) {
                        new UserCheckIdDuplicateTask(new OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted(Object object) {
                                if (object != null) {
                                    Boolean result = (Boolean) object;
                                    if (!result) {
                                        user_id_edit.setError("Duplicated User Id");
                                    }
                                    Toast.makeText(getApplicationContext(), ((Boolean) object).toString(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        }, user_id_edit.getText().toString()).execute();
                    } else {
                        user_id_edit.setError("Please Enter User Id");
                    }
                }
            }
        });
        user_email_edit = (EditText)findViewById(R.id.user_email_edit);
        user_email_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (user_email_edit.getText().toString().equals("")) {
                    user_email_edit.setError("Please Enter Email");
                }
            }
        });
        user_email_edit.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-SemiBold.otf"));
        user_pwd_edit = (EditText)findViewById(R.id.user_pwd_edit);
        user_pwd_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (user_pwd_edit.getText().toString().equals("")) {
                    user_pwd_edit.setError("Please Enter Password");
                }
            }
        });
        user_pwd_edit.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-SemiBold.otf"));

        register_info_tv = (TextView)findViewById(R.id.register_info_tv);
        register_info_tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-Medium.otf"));

        register_tv = (TextView)findViewById(R.id.register_tv);
        register_tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-SemiBold.otf"));

        register_btn = (ImageButton)findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String invitation_code = invitation_code_edit.getText().toString();
                String user_name = user_name_edit.getText().toString();
                String user_id = user_id_edit.getText().toString();
                String user_email = user_email_edit.getText().toString();
                String user_pwd = user_pwd_edit.getText().toString();


                if(!isValidInvitationCode(invitation_code)){
                    invitation_code_edit.setError("Invalid Invitation Code");
                    return ;
                }

                if(!isValidUserName(user_name)){
                    user_name_edit.setError("Invalid User Name");
                    return ;
                }

                if(!isValidUserID(user_id)){
                    user_id_edit.setError("Invalid User ID");
                    return ;
                }

                if (!isValidEmail(user_email)) {
                    user_email_edit.setError("Invalid Email");
                    return ;
                }

                if (!isValidPassword(user_pwd)) {
                    user_pwd_edit.setError("Invalid Password");
                    return ;
                }

                User new_user = new User();
                new_user.setInvitationCode(invitation_code);
                new_user.setName(user_name);
                new_user.setUserId(user_id);
                new_user.setEmail(user_email);
                new_user.setPassword(user_pwd);
                new_user.setFacebookProfile(user_email);

                new UserRegisterTask(RegisterActivity.this, new_user).execute();


            }
        });

        register_condition_tv = (TextView)findViewById(R.id.register_condition_tv);
        register_condition_tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-Bold.otf"));

    }

    //validating invitation_code
    private boolean isValidInvitationCode(String invitationCode){
        if(invitationCode.equals("")){
            return false;
        } else {
            return true;
        }

    }

    //validating user_name
    private boolean isValidUserName(String userName){
        if(userName.equals("")){
            return false;
        } else {
            if (!userName.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")) {
                //특수문자가 있을 경우

                return false;
            } else {
                //특수문자가 없을 경우
                return true;
            }
        }
    }

    //validating user_id
    private boolean isValidUserID(String userId){
        if(userId.equals("")){
            return false;
        }
        else {
            if(!userId.matches("^[a-zA-Z0-9]*$"))
            {
                //특수문자가 있을 경우
                return false;
            }
            else
            {
                //특수문자가 없을 경우
                return true;
            }
        }

    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (!pass.equals("") && pass.length() >= 6) {
            return true;
        }
        return false;
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

            new UserLoginTask(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(Object authorization) {
                    AuthManager.getAuthManager().login(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE),((User)user).getUserId(),((User)user).getPassword(),((User)user).getName(),((User)user).getEmail(),((Authorization)authorization).getAccess_token());
                    finish();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            }, (User)user).execute();
            Log.d("RegisterActivity", user.toString());
        } else {
            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(RegisterActivity.this, IntroActivity.class));
    }

}
