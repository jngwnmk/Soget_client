package com.markin.app.view.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.friend.FriendListTask;
import com.markin.app.connector.friend.FriendReceiveListTask;
import com.markin.app.connector.friend.FriendSentListTask;
import com.markin.app.connector.invitation.InvitationCodeGetTask;
import com.markin.app.connector.user.UserInfoGetTask;
import com.markin.app.model.Friend;
import com.markin.app.model.User;
import com.markin.app.view.Activity.InvitatonSendActivity;
import com.markin.app.view.Adapter.FriendAdatper;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-18.
 */
public class FriendsFragment extends Fragment {

    private ImageButton invitationUseBtn = null;
    private TextView invitationTv = null;
    private TextView invitationNumTv = null;

    private ExpandableListView friendList = null;
    private ArrayList<String> sentInvitations = new ArrayList<>();
    private ArrayList<Friend> friends = new ArrayList<Friend>();
    private FriendAdatper friendAdatper = null;

    private ProgressDialog pDialog;
    ArrayList<String> invitationNum = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend_layout, container, false);

        /*invitationUseBtn = (ImageButton)rootView.findViewById(R.id.invitation_use_btn);
        invitationUseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (invitationNum.size() != 0) {
                    Intent intent = new Intent(getActivity(), InvitatonSendActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(StaticValues.INVITATIONCODE, invitationNum.get(0));
                    bundle.putInt(StaticValues.INVITATIONNUM, invitationNum.size());
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            }
        });*/

        invitationTv = (TextView)rootView.findViewById(R.id.invitation_tv);
        invitationTv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));
        invitationTv.setTextColor(getResources().getColor(R.color.charcol_text_color_80));

        ArrayList<String> titles = new ArrayList<>();
        titles.add("연결 중인 친구");
        titles.add("연결된 친구");

        friendList = (ExpandableListView)rootView.findViewById(R.id.friend_list);
        friendAdatper = new FriendAdatper(inflater.getContext(), titles, friends, sentInvitations);
        friendList.setAdapter(friendAdatper);
        /*friendList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });*/
       /* friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (friends.get(position).getType().equals(Friend.FRIEND.FRIEND)) {
                    String user_id = friends.get(position).getUserInfo().getUserId();
                    Intent intent = new Intent(getActivity(), FriendArchiveActivity.class);
                    intent.putExtra(FriendArchiveActivity.FRIENDID, user_id);
                    startActivity(intent);

                }
            }
        });*/

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
        new FriendListTask(onTaskCompleted, user_id, token).execute();
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
        new FriendSentListTask(onTaskCompleted, user_id, token).execute();
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
        new FriendReceiveListTask(onTaskCompleted, user_id, token).execute();
    }

    private void getInvitation(){
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    invitationNum.clear();
                    invitationNum.addAll((ArrayList<String>) object);
                }
                invitationNumTv.setText(invitationNum.size() + "장");
            }
        };
        String user_id = (AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new InvitationCodeGetTask(onTaskCompleted, user_id, token).execute();

    }

    private void getFriendSentCode(){
        String user_id = (AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new UserInfoGetTask(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    User user = (User)object;
                    sentInvitations.clear();
                    ArrayList<String> friendSent = new ArrayList<String>();
                    for(int i = 0 ; i < user.getInvitation_sent().size() ; ++i){
                        friendSent.add(user.getInvitation_sent().get(i));
                    }
                    sentInvitations.addAll(friendSent);
                    updateFriendskList();
                }
            }
        }, user_id, token).execute();
    }

    private void getDummyFriendReceiveList(){

    }

    private void getDummyFriendSendList(){


    }

    private void getDummyFriendSentCode(){
        sentInvitations.clear();
        sentInvitations.add("878101");
        sentInvitations.add("418009");
    }

    private void getDummyFriendList(){
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();

        user1.setUserId("areumy");
        user1.setName("이아름");
        user1.setBookmarks(new ArrayList<String>());

        user2.setUserId("waniwani");
        user2.setName("전창완");
        user2.setBookmarks(new ArrayList<String>());

        user3.setUserId("soojin_ji");
        user3.setName("지수진");
        user3.setBookmarks(new ArrayList<String>());

        user4.setUserId("sejinlee");
        user4.setName("이세진");
        user4.setBookmarks(new ArrayList<String>());

        friends.clear();
        friends.add(new Friend(user1, Friend.FRIEND.FRIEND));
        friends.add(new Friend(user2, Friend.FRIEND.FRIEND));
        friends.add(new Friend(user3, Friend.FRIEND.FRIEND));
        friends.add(new Friend(user4, Friend.FRIEND.FRIEND));

    }


    @Override
    public void onResume(){
        super.onResume();
        //getFriendsReceiveList();
        //getFriendsSentList();
        getFriendsList();
        //getInvitation();

        //getDummyFriendSentCode();
        //getDummyFriendList();

        getFriendSentCode();
        if(friendList!=null){
            for(int i = 0 ; i < friendAdatper.getGroupCount() ; ++i){
                friendList.expandGroup(i);
            }

            friendList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                    return true;
                }
            });


        }
    }

}
