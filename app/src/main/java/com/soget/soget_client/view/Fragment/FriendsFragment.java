package com.soget.soget_client.view.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.connector.FriendListRequestTask;
import com.soget.soget_client.connector.FriendReceiveListRequestTask;
import com.soget.soget_client.connector.FriendSentListRequestTask;
import com.soget.soget_client.model.Friend;
import com.soget.soget_client.model.User;
import com.soget.soget_client.view.Adapter.FriendAdatper;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-18.
 */
public class FriendsFragment extends Fragment{

    private Button settingBtn = null;
    private Button searchBtn = null;
    private ListView friendList = null;
    private ArrayList<Friend> friends = new ArrayList<Friend>();
    private FriendAdatper friendAdatper = null;
    private ProgressDialog pDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.friend_layout,container, false);
        settingBtn = (Button)rootView.findViewById(R.id.setting_btn);
        searchBtn = (Button)rootView.findViewById(R.id.search_btn);
        friendList = (ListView)rootView.findViewById(R.id.friend_list);
        friendAdatper = new FriendAdatper(inflater.getContext(),friends);
        friendList.setAdapter(friendAdatper);

        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Loading....");
        pDialog.show();

        return rootView;
    }

    private void updateFriendskList(){
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                friendAdatper.notifyDataSetChanged();
            }
        });
    }

    private void getFriendsList(){
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    Log.d("FriendsFragment", ((ArrayList<User>) object).toString());
                    //friends.clear();
                    ArrayList<Friend> bothFriends = new ArrayList<Friend>();
                    for(int i =0 ; i < ((ArrayList<User>) object).size(); ++i){
                        bothFriends.add(new Friend(((ArrayList<User>) object).get(i), Friend.FRIEND.FRIEND));
                    }
                    friends.addAll(bothFriends);
                    updateFriendskList();
                }

                pDialog.dismiss();
            }
        };
        String user_id = (AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new FriendListRequestTask(onTaskCompleted,user_id, token).execute();
    }

    private void getFriendsSentList(){
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    Log.d("FriendsFragment", ((ArrayList<User>) object).toString());
                    //friends.clear();
                    ArrayList<Friend> sentFriends = new ArrayList<Friend>();
                    for(int i =0 ; i < ((ArrayList<User>) object).size(); ++i){
                        sentFriends.add(new Friend(((ArrayList<User>) object).get(i), Friend.FRIEND.FRIENDSENT));
                    }
                    friends.addAll(sentFriends);
                    updateFriendskList();
                }

                pDialog.dismiss();
            }
        };
        String user_id = (AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new FriendSentListRequestTask(onTaskCompleted,user_id, token).execute();
    }

    private void getFriendsReceiveList(){
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    Log.d("FriendsFragment", ((ArrayList<User>) object).toString());
                    //friends.clear();
                    ArrayList<Friend> receiveFriends = new ArrayList<Friend>();
                    for(int i =0 ; i < ((ArrayList<User>) object).size(); ++i){
                        receiveFriends.add(new Friend(((ArrayList<User>) object).get(i), Friend.FRIEND.FRIENDRECEIVE));
                    }
                    friends.addAll(receiveFriends);
                    updateFriendskList();
                }

                pDialog.dismiss();
            }
        };
        String user_id = (AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new FriendReceiveListRequestTask(onTaskCompleted,user_id, token).execute();
    }


    @Override
    public void onResume(){
        super.onResume();
        friends.clear();
        getFriendsReceiveList();
        getFriendsSentList();
        getFriendsList();
    }

}
