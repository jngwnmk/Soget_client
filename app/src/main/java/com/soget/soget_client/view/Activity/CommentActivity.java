package com.soget.soget_client.view.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.common.StaticValues;
import com.soget.soget_client.connector.AddCommentRequestTask;
import com.soget.soget_client.connector.GetCommentRequestTask;
import com.soget.soget_client.model.Comment;
import com.soget.soget_client.view.Adapter.CommentAdapter;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-04-01.
 */
public class CommentActivity extends ActionBarActivity{
    private ImageButton backBtn =null;
    private TextView markinNum = null;
    private EditText commentEt = null;
    private ImageButton addCommentBtn = null;
    private ListView commentList = null;
    private CommentAdapter commentAdapter = null;
    private ArrayList<Comment> comments = new ArrayList<Comment>();

    private String bookmark_id = "";
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading....");
        bookmark_id = getIntent().getExtras().getString(StaticValues.BOOKMARKID);
        initLayout();
        Toast.makeText(this,bookmark_id,Toast.LENGTH_SHORT).show();
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
        markinNum = (TextView) findViewById(R.id.comment_markin_num_desc);
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
                        } else {
                            Toast.makeText(getApplicationContext(), "Null",Toast.LENGTH_SHORT).show();
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
                new AddCommentRequestTask(onTaskCompleted,bookmark_id, comment, token).execute();

            }
        });
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
        new GetCommentRequestTask(onTaskCompleted, bookmark_id, token).execute();
    }



}
