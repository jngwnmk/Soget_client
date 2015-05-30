package com.soget.soget_client.view.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.common.StaticValues;
import com.soget.soget_client.connector.bookmark.BookmarkAddTask;
import com.soget.soget_client.model.User;

/**
 * Created by wonmook on 2015-04-01.
 */
public class WebViewActivity extends ActionBarActivity {

    private ImageButton markinBtn = null;
    private ImageButton closeBtn =null;
    private WebView webView = null;
    private ProgressDialog pDialog;

    private String bookmarkId = "";
    public static String WEBVIEWURL ="URL";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        String url = getIntent().getExtras().getString(WEBVIEWURL);
        bookmarkId = getIntent().getExtras().getString(StaticValues.BOOKMARKID);
        boolean isMyBookmark = getIntent().getExtras().getBoolean(StaticValues.ISMYBOOKMARK);

        markinBtn = (ImageButton)findViewById(R.id.markin_btn);
        closeBtn = (ImageButton)findViewById(R.id.close_btn);
        webView = (WebView)findViewById(R.id.webView);

        markinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToMyArchive();
            }
        });
        if(isMyBookmark){
            markinBtn.setVisibility(View.GONE);
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClientClass());

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading....");

    }

    private void AddToMyArchive(){
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {
                pDialog.dismiss();
            }
        };
        User user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        if(user!=null){
            String user_id = user.getUserId();
            String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            //pDialog.show();
            //new BookmarkAddTask(onTaskCompleted,user_id, bookmarkId, token).execute();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if((keyCode==KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            webView.loadUrl(url);
            return true;
        }
    }
}
