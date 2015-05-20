package com.soget.soget_client.view.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.common.StaticValues;
import com.soget.soget_client.connector.FriendListRequestTask;
import com.soget.soget_client.connector.FriendReceiveListRequestTask;
import com.soget.soget_client.connector.FriendSentListRequestTask;
import com.soget.soget_client.connector.InvitationCodeGetRequestTask;
import com.soget.soget_client.model.Friend;
import com.soget.soget_client.model.User;
import com.soget.soget_client.view.Activity.FriendArchiveActivity;
import com.soget.soget_client.view.Activity.FriendSearchActivity;
import com.soget.soget_client.view.Activity.InvitatonSendActivity;
import com.soget.soget_client.view.Activity.SettingActivity;
import com.soget.soget_client.view.Adapter.FriendAdatper;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-18.
 */
public class FriendsFragment extends Fragment {

    private LinearLayout invitationLayoutBtn = null;
    private ImageButton settingBtn = null;
    private ImageButton searchBtn = null;
    private TextView invitationNumTv = null;
    private ListView friendList = null;
    private ArrayList<Friend> friends = new ArrayList<Friend>();
    private FriendAdatper friendAdatper = null;
    private ProgressDialog pDialog;
    ArrayList<String> invitationNum = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend_layout, container, false);

        settingBtn = (ImageButton) rootView.findViewById(R.id.setting_btn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Setting", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        searchBtn = (ImageButton) rootView.findViewById(R.id.friend_search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FriendSearchActivity.class);
                startActivity(intent);
            }
        });

        invitationLayoutBtn = (LinearLayout) rootView.findViewById(R.id.invitation_layout_btn);
        invitationLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(invitationNum.size()!=0){
                    Intent intent = new Intent(getActivity(),InvitatonSendActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(StaticValues.INVITATIONCODE,invitationNum.get(0));
                    bundle.putInt(StaticValues.INVITATIONNUM, invitationNum.size());
                    intent.putExtras(bundle);
                    startActivity(intent);

                }


            }
        });
        invitationNumTv = (TextView) rootView.findViewById(R.id.invitation_num_tv);

        friendList = (ListView) rootView.findViewById(R.id.friend_list);


        friendAdatper = new FriendAdatper(inflater.getContext(), friends);
        friendList.setAdapter(friendAdatper);
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (friends.get(position).getType().equals(Friend.FRIEND.FRIEND)) {
                    String user_id = friends.get(position).getUserInfo().getUserId();
                    Intent intent = new Intent(getActivity(), FriendArchiveActivity.class);
                    intent.putExtra(FriendArchiveActivity.FRIENDID, user_id);
                    startActivity(intent);

                }
            }
        });

        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Loading....");

        return rootView;
    }

    private void updateFriendskList() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                friendAdatper.notifyDataSetChanged();
            }
        });
    }

    private void getFriendsList() {
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if (object != null) {
                    Log.d("FriendsFragment", ((ArrayList<User>) object).toString());
                    //friends.clear();
                    ArrayList<Friend> bothFriends = new ArrayList<Friend>();
                    for (int i = 0; i < ((ArrayList<User>) object).size(); ++i) {
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
        pDialog.show();
        new FriendListRequestTask(onTaskCompleted, user_id, token).execute();
    }

    private void getFriendsSentList() {
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if (object != null) {
                    Log.d("FriendsFragment", ((ArrayList<User>) object).toString());
                    //friends.clear();
                    ArrayList<Friend> sentFriends = new ArrayList<Friend>();
                    for (int i = 0; i < ((ArrayList<User>) object).size(); ++i) {
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
        new FriendSentListRequestTask(onTaskCompleted, user_id, token).execute();
    }

    private void getFriendsReceiveList() {
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                friends.clear();
                if (object != null) {
                    Log.d("FriendsFragment", ((ArrayList<User>) object).toString());
                    //friends.clear();
                    ArrayList<Friend> receiveFriends = new ArrayList<Friend>();
                    for (int i = 0; i < ((ArrayList<User>) object).size(); ++i) {
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
        new FriendReceiveListRequestTask(onTaskCompleted, user_id, token).execute();
    }

    private void getInvitation(){
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    invitationNum.clear();
                    invitationNum.addAll((ArrayList<String>)object);
                }
                invitationNumTv.setText(invitationNum.size()+"장");
            }
        };
        String user_id = (AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new InvitationCodeGetRequestTask(onTaskCompleted, user_id, token).execute();

    }

    @Override
    public void onResume(){
        super.onResume();
        getFriendsReceiveList();
        getFriendsSentList();
        getFriendsList();
        getInvitation();

    }

}
