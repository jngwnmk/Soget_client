package com.soget.soget_client.view.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.connector.UserSearchRequestTask;
import com.soget.soget_client.model.Friend;
import com.soget.soget_client.model.User;
import com.soget.soget_client.view.Adapter.FriendAdatper;
import java.util.ArrayList;

/**
 * Created by wonmook on 15. 5. 16..
 */
public class FriendSearchActivity extends ActionBarActivity {

    private EditText          friendSearchEt   = null;
    private TextView          closeTv          = null;
    private ImageButton       cancelBtn        = null;
    private ListView          friendSearchList = null;
    private ArrayList<Friend> friends          = new ArrayList<Friend>();
    private FriendAdatper     friendAdatper    = null;
    private ProgressDialog    pDialog          = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_search_layout);



        initLayout();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading....");

    }

    private void initLayout(){
        friendSearchEt = (EditText)findViewById(R.id.friend_search_et);
        friendSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_SEARCH)
                {
                    // search pressed and perform your functionality.
                    Toast.makeText(getApplicationContext(), "Action", Toast.LENGTH_SHORT).show();
                    OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(Object object) {
                            if(object!=null){
                                friends.clear();
                                ArrayList<Friend> users = new ArrayList<Friend>();
                                for (int i = 0; i < ((ArrayList<User>) object).size(); ++i) {
                                    users.add(new Friend(((ArrayList<User>) object).get(i), Friend.FRIEND.FRIEND));
                                }
                                System.out.println(users.toString());
                                friends.addAll(users);
                                friendAdatper.notifyDataSetChanged();
                            }
                            pDialog.dismiss();
                        }
                    };
                    String user_id = (AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
                    String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                    pDialog.show();
                    new UserSearchRequestTask(onTaskCompleted, user_id, token, friendSearchEt.getText().toString()).execute();
                }
                return false;
            }
        });

        closeTv = (TextView)findViewById(R.id.friend_search_close_btn);
        closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 finish();
            }
        });

        cancelBtn = (ImageButton)findViewById(R.id.friend_search_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  friendSearchEt.setText("");
            }
        });

        friendSearchList = (ListView)findViewById(R.id.friend_search_list);
        friendAdatper = new FriendAdatper(getApplicationContext(), friends);
        friendSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String user_id = friends.get(position).getUserInfo().getUserId();
                    Intent intent = new Intent(FriendSearchActivity.this, FriendArchiveActivity.class);
                    intent.putExtra(FriendArchiveActivity.FRIENDID, user_id);
                    startActivity(intent);
            }
        });
        friendSearchList.setAdapter(friendAdatper);
        friendAdatper.notifyDataSetChanged();

    }

}
