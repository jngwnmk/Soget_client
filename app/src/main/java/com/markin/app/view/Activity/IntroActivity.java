package com.markin.app.view.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.common.StaticValues;


public class IntroActivity extends Activity  {

    private Button register_btn    = null;
    private Button login_btn       = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);
        setLayout();

        Intent receiveIntent = getIntent();
        if(receiveIntent!=null){
            String invitation_num = receiveIntent.getStringExtra(StaticValues.INVITATIONNUM);
            String invitation_username = receiveIntent.getStringExtra(StaticValues.INVITATIONUSERNAME);
            if(invitation_num!=null && !invitation_num.equals("") && invitation_username!=null && !invitation_username.equals("")){
                finish();
                Intent registerIntent = new Intent(IntroActivity.this, RegisterActivity.class);
                registerIntent.putExtra(StaticValues.INVITATIONNUM, invitation_num);
                registerIntent.putExtra(StaticValues.INVITATIONUSERNAME, invitation_username);
                startActivity(registerIntent);
            }
        }
    }

    private void setLayout(){

        register_btn  = (Button)findViewById(R.id.intro_register_btn);
        login_btn     = (Button)findViewById(R.id.intro_login_btn);

        register_btn.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-SemiBold.otf"));
        login_btn.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Bold.otf"));

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Register Activity
                finish();
                startActivity(new Intent(IntroActivity.this, RegisterActivity.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start Login Activity
                finish();
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            }
        });

    }

}
