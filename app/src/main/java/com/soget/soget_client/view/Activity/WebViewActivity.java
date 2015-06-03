package com.soget.soget_client.view.Activity;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.common.StaticValues;
import com.soget.soget_client.connector.bookmark.BookmarkAddTask;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.model.User;
import com.soget.soget_client.view.Fragment.AddBookmarkDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by wonmook on 2015-04-01.
 */
public class WebViewActivity extends ActionBarActivity {

    private ImageButton markinBtn = null;
    private ImageButton closeBtn =null;
    private WebView webView = null;
    private ProgressDialog pDialog;

    private String url = "";
    private String bookmarkId = "";
    public static String WEBVIEWURL ="URL";
    private Bookmark ref_bookmark = new Bookmark();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        //ref_bookmark = getIntent().getExtras().getParcelable(StaticValues.BOOKMARK);
        url = getIntent().getExtras().getString(WEBVIEWURL);
        bookmarkId = getIntent().getExtras().getString(StaticValues.BOOKMARKID);
        boolean isMyBookmark = getIntent().getExtras().getBoolean(StaticValues.ISMYBOOKMARK);

        markinBtn = (ImageButton)findViewById(R.id.markin_btn);
        closeBtn = (ImageButton)findViewById(R.id.close_btn);
        webView = (WebView)findViewById(R.id.webView);

        markinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref_bookmark.setUrl(url);
                ref_bookmark.setId(bookmarkId);
                showAddDialog(ref_bookmark.getUrl(), ref_bookmark);

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

    public void showAddDialog(String url,Bookmark ref_bookmark){
        //Show Add Dialog
        FragmentManager fm = getFragmentManager();
        AddBookmarkDialog addBookmarkDialog = new AddBookmarkDialog();
        addBookmarkDialog.updateInputUrl(url);
        addBookmarkDialog.setRefBookmark(ref_bookmark);
        addBookmarkDialog.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    markinBtn.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Added to my archive!!!", Toast.LENGTH_SHORT).show();

                }

            }
        });
        addBookmarkDialog.show(fm,"add_bookmark_dialog");
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
