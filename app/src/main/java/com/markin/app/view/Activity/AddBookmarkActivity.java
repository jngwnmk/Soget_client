package com.markin.app.view.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.callback.AddBookmarkListener;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.connector.bookmark.BookmarkAddTask;
import com.markin.app.connector.bookmark.BookmarkMakeTask;
import com.markin.app.connector.category.CategoryGetTask;
import com.markin.app.connector.comment.CommentAddTask;
import com.markin.app.connector.recommend.RecommendGetTask;
import com.markin.app.model.Bookmark;
import com.markin.app.model.Category;
import com.markin.app.model.Comment;
import com.markin.app.model.Follower;
import com.markin.app.model.User;
import com.markin.app.view.Adapter.AddBookmarkPageAdapter;
import com.markin.app.view.Fragment.AddBookmarkBodyFirstFragment;
import com.markin.app.view.Fragment.AddBookmarkDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by wonmook on 2016. 4. 27..
 */
public class AddBookmarkActivity extends FragmentActivity implements AddBookmarkListener{

    public static final int ADDBOOKMARK_CANCEL = 0;
    public static final int ADDBOOKMARK_SUCCESS = 1;
    public static final int DISCARDBOOKMARK_CANCEL = 2;
    public static final int DISCARDBOOKMARK_SUCCESS = 3;


    private ViewPager viewPager;
    private AddBookmarkPageAdapter addBookmarkPageAdapter;
    private ArrayList<Category> categories = new ArrayList<>();
    private TextView  cancelTv;
    private TextView  addTv;
    private String    currentCategory = "Listening";
    private String    url = "";
    private boolean   isMakeBookmark = true;
    private Bookmark refBookmark = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_bookmark_layout);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        // 인텐트 정보가 있는 경우 실행 (Make New Bookmark from Web Browser)
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                isMakeBookmark = true;
                String sharedUrl = intent.getStringExtra(Intent.EXTRA_TEXT);    // 가져온 인텐트의 텍스트 정보
                if(!"".equals(sharedUrl)){
                    Toast.makeText(getApplicationContext(), sharedUrl, Toast.LENGTH_SHORT).show();
                    url = ensure_has_protocol(sharedUrl);
                } else {
                    Toast.makeText(getApplicationContext(), "Empty URL", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        } else {
            isMakeBookmark = false;
            intent.setExtrasClassLoader(Bookmark.class.getClassLoader());
            refBookmark = intent.getParcelableExtra("Bookmark");

        }
        cancelTv = (TextView)findViewById(R.id.cancel_tv);
        cancelTv.setTextColor(getResources().getColor(R.color.charcol_text_color_66));
        cancelTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(ADDBOOKMARK_CANCEL);
                finish();
            }
        });
        addTv = (TextView)findViewById(R.id.add_tv);
        addTv.setTextColor(getResources().getColor(R.color.charcol_text_color_80));
        addTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMakeBookmark){
                    makeBookmark();
                } else {
                    addBookmark(refBookmark);
                }
            }
        });

        viewPager = (ViewPager)findViewById(R.id.add_dialog_pager);
        addBookmarkPageAdapter = new AddBookmarkPageAdapter(getSupportFragmentManager());
        addBookmarkPageAdapter.setCurrentCategory(currentCategory);
        viewPager.setAdapter(addBookmarkPageAdapter);
    }

    private void addBookmark(Bookmark refBookmark){
        try{
            User user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                ArrayList<String> categorySet = new ArrayList<String>();
                categorySet.add(currentCategory);
                refBookmark.setCategory(categorySet);
                new BookmarkAddTask(this, new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        if(object!=null){
                            String comment = ((AddBookmarkBodyFirstFragment)addBookmarkPageAdapter.getItem(0)).getCommentEt().getText().toString();
                            if(!"".equals(comment)){
                                addComment(((Bookmark)object).getId(),comment);
                            }
                        }
                    }
                }, user_id, refBookmark.getId(), refBookmark, token).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void makeBookmark(){
        try{
            User user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                ArrayList<String> categorySet = new ArrayList<String>();
                categorySet.add(currentCategory);
                Bookmark newBookmark = new Bookmark();
                newBookmark.setUrl(url);
                newBookmark.setPrivacy(false);
                newBookmark.setInitUserId(user_id);
                newBookmark.setCategory(categorySet);

                new BookmarkMakeTask(this, new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        if(object!=null){
                            String comment = ((AddBookmarkBodyFirstFragment)addBookmarkPageAdapter.getItem(0)).getCommentEt().getText().toString();
                            if(!"".equals(comment)){
                                addComment(((Bookmark)object).getId(),comment);

                            }
                        }
                    }
                }, newBookmark, token).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void addComment(final String bookmarkId, String commentStr){
        OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                Toast.makeText(getApplicationContext(), "Add completed!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("bookmarkId", bookmarkId);
                setResult(ADDBOOKMARK_SUCCESS, intent);
                finish();
            }
        };

        String user_id = (AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        Comment comment = new Comment();
        comment.setContent(commentStr);
        comment.setUserId(user_id);


        new CommentAddTask(onTaskCompleted,bookmarkId, comment, token).execute();
    }

    private String ensure_has_protocol(String a_url)
    {
        if (a_url.startsWith("http://")||a_url.startsWith("https://"))
        {
            return a_url;
        } else {
            return "http://"+a_url;
        }
        //return a_url;
    }

    @Override
    protected void onResume(){
        super.onResume();

    }


    @Override
    public void selectCategory(String category) {

        currentCategory = category;
        viewPager.setCurrentItem(0);
        addBookmarkPageAdapter.setCurrentCategory(category);
        AddBookmarkBodyFirstFragment fragment = (AddBookmarkBodyFirstFragment)addBookmarkPageAdapter.getItem(0);
        fragment.setCateogry(category);
        addBookmarkPageAdapter.notifyDataSetChanged();

    }

    @Override
    public void gotoCategoryList() {
        viewPager.setCurrentItem(1);
    }
}
