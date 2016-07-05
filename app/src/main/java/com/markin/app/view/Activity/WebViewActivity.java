package com.markin.app.view.Activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
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
import com.markin.app.common.AuthManager;
import com.markin.app.common.SogetUtil;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.bookmark.LikeCancelTask;
import com.markin.app.connector.bookmark.LikeTask;
import com.markin.app.model.Bookmark;
import com.markin.app.model.User;
import com.markin.app.view.Fragment.AddBookmarkDialog;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-04-01.
 */
public class WebViewActivity extends ActionBarActivity {

    private WebView webView = null;
    private ProgressBar progressBar = null;
    private String url = "";
    private String bookmarkId = "";
    private ArrayList<String> tags = new ArrayList<String>();
    public static String WEBVIEWURL ="URL";
    private Bookmark ref_bookmark = new Bookmark();

    private ImageButton prevBtn = null;
    private ImageButton webViewLikeBtn = null;
    private ImageButton webViewSocketBtn = null;
    private ImageButton webViewCommentBtn = null;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        url = getIntent().getExtras().getString(WEBVIEWURL);
        bookmarkId = getIntent().getExtras().getString(StaticValues.BOOKMARKID);
        tags = getIntent().getExtras().getStringArrayList(StaticValues.BOOKMARKTAG);
        boolean isMyBookmark = getIntent().getExtras().getBoolean(StaticValues.ISMYBOOKMARK);
        final boolean didLike = getIntent().getExtras().getBoolean(StaticValues.ISBOOKMARKLIKE);
        boolean didSocket= getIntent().getExtras().getBoolean(StaticValues.ISBOOKMARKSOCKET);
        boolean didComment = getIntent().getExtras().getBoolean(StaticValues.ISBOOKMARKCOMMENT);

        webView = (WebView)findViewById(R.id.webView);

        prevBtn = (ImageButton)findViewById(R.id.back_btn);
        webViewLikeBtn = (ImageButton)findViewById(R.id.webview_like_btn);
        webViewSocketBtn = (ImageButton)findViewById(R.id.webview_socket_btn);
        webViewCommentBtn = (ImageButton)findViewById(R.id.webview_comment_btn);

        setLikeBtn(didLike);
        setSocketBtn(didSocket);
        setCommentBtn(didComment);

        ref_bookmark.setUrl(url);
        ref_bookmark.setId(bookmarkId);

        webViewLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (didLike) {
                    likeCancel(ref_bookmark);
                } else {
                    like(ref_bookmark);
                }
            }
        });


        webViewCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openComment(bookmarkId);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

            }
        });
/*
        markinBtn = (ImageButton)findViewById(R.id.markin_btn);
        closeBtn = (ImageButton)findViewById(R.id.close_btn);
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
*/


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

    private void like(final Bookmark bookmark){
        try{
            User user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                new LikeTask(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        if((Integer)object!=-1){
                            setLikeBtn(true);
                        }
                    }
                },user_id, bookmark.getId(), token).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void likeCancel(final Bookmark bookmark) {
        try {
            User user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user !=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                new LikeCancelTask(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        if((Integer)object!=-1){
                           setLikeBtn(false);
                        }
                    }
                }, user_id, bookmark.getId(), token).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }


    private void openComment(String bookmark_id){
        Intent intent = new Intent(this, CommentActivity.class);
        Bundle extras = new Bundle();
        extras.putString(StaticValues.BOOKMARKID, bookmark_id);
        intent.putExtras(extras);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
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

    private void setLikeBtn(boolean hasLike){
        if(hasLike){
            webViewLikeBtn.setImageResource(R.drawable.webview_like_icon_pressed);
        } else {
            webViewLikeBtn.setImageResource(R.drawable.webview_like_icon);
        }
    }

    private void setSocketBtn(boolean hasSocket){
        if(hasSocket){
            webViewSocketBtn.setImageResource(R.drawable.webview_socket_icon_selected);
        } else {
            webViewSocketBtn.setImageResource(R.drawable.webview_socket_icon);
        }
    }

    private void setCommentBtn(boolean hasComment){
        if(hasComment){
            webViewCommentBtn.setImageResource(R.drawable.webview_comment_icon_pressed);
        } else {
            webViewCommentBtn.setImageResource(R.drawable.webview_comment_icon);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case StaticValues.RESULTCODE.COMMENT:
                if(requestCode==1){
                     boolean hasComment = data.getBooleanExtra(StaticValues.COMMENT, false);
                     setCommentBtn(hasComment);

                }
                break;
            default:
                break;
        }
    }
}
