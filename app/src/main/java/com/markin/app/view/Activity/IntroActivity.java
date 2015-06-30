package com.markin.app.view.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.markin.app.R;


public class IntroActivity extends Activity  {

    private TextView    intro_desc_0_tv = null;
    private TextView    intro_desc_tv = null;
    private TextView    register_tv = null;
    private ImageButton register_btn = null;
    private TextView    request_tv = null;
    private TextView    login_tv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_layout);
        setLayout();
    }

    private void setLayout(){
        intro_desc_0_tv = (TextView)findViewById(R.id.markin_intro_logo_tv);
        intro_desc_0_tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-SemiBold.otf"));

        intro_desc_tv = (TextView)findViewById(R.id.markin_intro_tv);
        intro_desc_tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-SemiBold.otf"));

        register_btn  = (ImageButton)findViewById(R.id.intro_register_btn);
        login_tv = (TextView)findViewById(R.id.intro_login_tv);
        request_tv = (TextView)findViewById(R.id.intro_invitation_request_tv);
        register_tv = (TextView)findViewById(R.id.register_tv);
        register_tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-SemiBold.otf"));

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Login Activity
                finish();
                startActivity(new Intent(IntroActivity.this, RegisterActivity.class));
            }
        });

        login_tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-Bold.otf"));
        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Register Activity
                finish();
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            }
        });

        request_tv.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AppleSDGothicNeo-Bold.otf"));
        request_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, NormalWebViewActivity.class);
                intent.putExtra(NormalWebViewActivity.WEBVIEWURL,"https://medium.com/@MarkIn_app/markin-01-c043b4d4f1cd");
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Request", Toast.LENGTH_SHORT).show();
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


}
