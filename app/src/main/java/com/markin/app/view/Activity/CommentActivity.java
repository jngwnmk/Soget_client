package com.markin.app.view.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.comment.CommentAddTask;
import com.markin.app.connector.comment.CommentGetTask;
import com.markin.app.model.Comment;
import com.markin.app.view.Adapter.CommentAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-04-01.
 */
public class CommentActivity extends ActionBarActivity{
    private ImageButton backBtn =null;
    private LinearLayout headerBoarder = null;
    private EditText commentEt = null;
    private ImageButton addCommentBtn = null;
    private TextView    addCommentTv  = null;
    private ListView commentList = null;
    private CommentAdapter commentAdapter = null;
    private ArrayList<Comment> comments = new ArrayList<Comment>();
    private LinearLayout commentLayout = null;
    private String bookmark_id = "";
    private ProgressDialog pDialog;
    private int markin_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading....");
        bookmark_id = getIntent().getExtras().getString(StaticValues.BOOKMARKID);
        markin_num = getIntent().getExtras().getInt(StaticValues.MARKINNUM,0);
        initLayout();
        getComments(bookmark_id);

    }

    private void initLayout() {

        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        commentEt = (EditText) findViewById(R.id.comment_et);
        addCommentBtn = (ImageButton) findViewById(R.id.comment_add_btn);
        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object object) {
                        if(object!=null){
                            Comment comment = ((Comment)object);
                            comments.add(comment);
                            commentAdapter.notifyDataSetChanged();
                        }
                        pDialog.dismiss();
                    }
                };

                String user_id = (AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
                String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                Comment comment = new Comment();
                comment.setContent(commentEt.getText().toString());
                comment.setUserId(user_id);
                pDialog.show();
                commentEt.setText("");

                new CommentAddTask(onTaskCompleted,bookmark_id, comment, token).execute();

            }
        });
        addCommentTv = (TextView)findViewById(R.id.comment_add_btn_tv);
        addCommentTv.setTextColor(getResources().getColor(R.color.sub_text_color_80));
        addCommentTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Regular.otf"));

        commentList = (ListView) findViewById(R.id.comment_list);
        commentAdapter = new CommentAdapter(getApplicationContext(), comments);
        commentList.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();

    }

    private void getComments(String bookmark_id){
        OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    comments.clear();
                    comments.addAll((ArrayList<Comment>)object);
                    commentAdapter.notifyDataSetChanged();
                }
                pDialog.dismiss();
            }
        };
        String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        pDialog.show();
        new CommentGetTask(onTaskCompleted, bookmark_id, token).execute();
    }



}
