package com.markin.app.view.Activity;

import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.StaticValues;
import com.markin.app.model.Bookmark;
import com.markin.app.view.Fragment.AddBookmarkDialog;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-04-01.
 */
public class WebViewActivity extends ActionBarActivity {

    private ImageButton markinBtn = null;
    private ImageButton closeBtn =null;
    private WebView webView = null;
    private ProgressBar progressBar = null;
    private String url = "";
    private String bookmarkId = "";
    private ArrayList<String> tags = new ArrayList<String>();
    public static String WEBVIEWURL ="URL";
    private Bookmark ref_bookmark = new Bookmark();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        url = getIntent().getExtras().getString(WEBVIEWURL);
        bookmarkId = getIntent().getExtras().getString(StaticValues.BOOKMARKID);
        tags = getIntent().getExtras().getStringArrayList(StaticValues.BOOKMARKTAG);
        boolean isMyBookmark = getIntent().getExtras().getBoolean(StaticValues.ISMYBOOKMARK);

        markinBtn = (ImageButton)findViewById(R.id.markin_btn);
        closeBtn = (ImageButton)findViewById(R.id.close_btn);
        webView = (WebView)findViewById(R.id.webView);

        markinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref_bookmark.setUrl(url);
                ref_bookmark.setId(bookmarkId);
                ref_bookmark.setTags(tags);
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
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClientClass());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });


        webView.loadUrl(url);
        progressBar = (ProgressBar) findViewById(R.id.webview_progress);

    }

    public void showAddDialog(String url,Bookmark ref_bookmark){
        //Show Add Dialog
        /*FragmentManager fm = getFragmentManager();
        AddBookmarkDialog addBookmarkDialog = new AddBookmarkDialog();
        addBookmarkDialog.updateInputUrl(url);
        addBookmarkDialog.setRefBookmark(ref_bookmark);
        addBookmarkDialog.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if (object != null) {
                    markinBtn.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getApplicationContext(), "Added to my archive!!!", Toast.LENGTH_SHORT).show();

                }

            }
        });
        addBookmarkDialog.show(fm,"add_bookmark_dialog");*/
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

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(getApplicationContext(),"Loading Error",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);

        }
    }
}
