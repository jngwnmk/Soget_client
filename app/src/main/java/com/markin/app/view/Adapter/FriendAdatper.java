package com.markin.app.view.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.common.AuthManager;
import com.markin.app.connector.friend.FriendAcceptTask;
import com.markin.app.connector.friend.FriendRequestTask;
import com.markin.app.model.Friend;
import com.markin.app.view.component.ConfirmToast;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-23.
 */
public class FriendAdatper extends BaseAdapter{
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Friend> friends;

    public FriendAdatper(Context context, ArrayList<Friend> friends){
        this.mContext = context;
        this.friends = friends;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        System.out.println("getCount():"+friends.size());
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FriendWrapper friendWrapper =null;
        if(row==null){
            row = layoutInflater.inflate(R.layout.friend_list_row, null);
            friendWrapper = new FriendWrapper(row);
            row.setTag(friendWrapper);
        } else {
            friendWrapper = (FriendWrapper)row.getTag();

        }
        //Set title
        final Friend friend = (Friend)getItem(position);
        System.out.println("FriendAdapter(GetView):"+friend.getType());


        if(friend.getType().equals(Friend.FRIEND.FRIEND)){
            friendWrapper.getFriendBtn().setVisibility(View.INVISIBLE);
            friendWrapper.getFriendName().setText(friend.getUserInfo().getName());
            if(friend.getUserInfo().getBookmarks()!=null)
            {
                friendWrapper.getFriendNumBookmark().setText(friend.getUserInfo().getBookmarks().size() +" MarkIn'");
            }
            else {
                friendWrapper.getFriendNumBookmark().setText("0 MarkIn'");
            }

        } else if(friend.getType().equals(Friend.FRIEND.FRIENDRECEIVE)){
            friendWrapper.getFriendBtn().setText(mContext.getResources().getString(R.string.accept_btn));
            friendWrapper.getFriendBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Accept",Toast.LENGTH_SHORT).show();
                    String user_id = (AuthManager.getAuthManager().getLoginInfo(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
                    String token = AuthManager.getAuthManager().getToken(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                    String friend_id = friend.getUserInfo().getUserId();
                    new FriendAcceptTask(user_id,friend_id,token).execute();
                    friend.setType(Friend.FRIEND.FRIEND);
                    notifyDataSetChanged();

                }
            });
            friendWrapper.getFriendBtn().setTextColor(mContext.getResources().getColor(R.color.white));
            friendWrapper.getFriendBtn().setBackgroundResource(R.drawable.friend_add_btn);
            friendWrapper.getFriendName().setText(friend.getUserInfo().getName() + "(" + friend.getUserInfo().getUserId() + ")");
            friendWrapper.getFriendName().setTextColor(mContext.getResources().getColor(R.color.friend_receive_request_btn));
            friendWrapper.getFriendNumBookmark().setVisibility(View.GONE);

        } else if(friend.getType().equals(Friend.FRIEND.FRIENDSENT)){
            friendWrapper.getFriendBtn().setText(mContext.getResources().getString(R.string.waiting_btn));
            friendWrapper.getFriendBtn().setTextColor(mContext.getResources().getColor(R.color.friend_receive_request_btn_66));
            friendWrapper.getFriendBtn().setBackgroundResource(R.drawable.friends_waiting_box);
            friendWrapper.getFriendName().setText(friend.getUserInfo().getName() + "(" + friend.getUserInfo().getUserId() + ")");
            friendWrapper.getFriendName().setTextColor(mContext.getResources().getColor(R.color.friend_receive_request_btn));
            friendWrapper.getFriendNumBookmark().setVisibility(View.GONE);

        } else if(friend.getType().equals(Friend.FRIEND.NOTFRIEND)){
            friendWrapper.getFriendBtn().setText(mContext.getResources().getString(R.string.accept_btn));
            friendWrapper.getFriendBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmToast toast = new ConfirmToast(mContext);
                    toast.showToast("친구요청하였습니다.", Toast.LENGTH_SHORT);
                    String user_id = (AuthManager.getAuthManager().getLoginInfo(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
                    String token = AuthManager.getAuthManager().getToken(mContext.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                    String friend_id = friend.getUserInfo().getUserId();
                    new FriendRequestTask(user_id, friend_id, token).execute();
                    friend.setType(Friend.FRIEND.FRIENDSENT);
                    notifyDataSetChanged();

                }
            });
            friendWrapper.getFriendBtn().setTextColor(mContext.getResources().getColor(R.color.white));
            friendWrapper.getFriendBtn().setBackgroundResource(R.drawable.friend_add_btn);
            friendWrapper.getFriendName().setText(friend.getUserInfo().getName() + "(" + friend.getUserInfo().getUserId() + ")");
            friendWrapper.getFriendName().setTextColor(mContext.getResources().getColor(R.color.friend_receive_request_btn));
            friendWrapper.getFriendNumBookmark().setVisibility(View.GONE);
        }

        return row;
    }

    private class FriendWrapper{
        private View base;
        private TextView friendName;
        private TextView friendNumBookmark;
        private TextView   friendBtn;
        private LinearLayout backgroundLayout;

        public FriendWrapper(View base){
            this.base = base;
        }

        public TextView getFriendName() {
            if(friendName==null){
                friendName = (TextView)base.findViewById(R.id.friend_name);
                friendName.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

            }
            return friendName;
        }

        public TextView getFriendNumBookmark() {
            if(friendNumBookmark==null){
                friendNumBookmark = (TextView)base.findViewById(R.id.num_bookmark);
                friendNumBookmark.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/FrutigerLTStd-Bold.otf"));

            }
            return friendNumBookmark;
        }

        public TextView getFriendBtn() {
            if(friendBtn==null){
                friendBtn = (TextView)base.findViewById(R.id.friend_btn);
                friendBtn.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

            }
            return friendBtn;
        }

        public LinearLayout getBackgroundLayout() {
            if(backgroundLayout==null){
                backgroundLayout =(LinearLayout)base.findViewById(R.id.friend_list_background);
            }
            return backgroundLayout;
        }
    }
}
