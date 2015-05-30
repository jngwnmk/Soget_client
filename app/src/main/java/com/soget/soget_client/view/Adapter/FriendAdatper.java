package com.soget.soget_client.view.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soget.soget_client.R;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.connector.friend.FriendAcceptTask;
import com.soget.soget_client.connector.friend.FriendRequestTask;
import com.soget.soget_client.model.Friend;
import com.soget.soget_client.view.Activity.FriendSearchActivity;
import com.soget.soget_client.view.Activity.MainActivity;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-23.
 */
public class FriendAdatper extends BaseAdapter{
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Friend> friends;

    public FriendAdatper(Context context, ArrayList<Friend> friends){
        this.context = context;
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
        if(friendWrapper==null){
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
                friendWrapper.getFriendNumBookmark().setText(friend.getUserInfo().getBookmarks().size() +" Gets");
            }
            else {
                friendWrapper.getFriendNumBookmark().setText("0 Gets");
            }

        } else if(friend.getType().equals(Friend.FRIEND.FRIENDRECEIVE)){
            friendWrapper.getFriendBtn().setText(context.getResources().getString(R.string.accept_btn));
            friendWrapper.getFriendBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Accept",Toast.LENGTH_SHORT).show();
                    String user_id = (AuthManager.getAuthManager().getLoginInfo(context.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
                    String token = AuthManager.getAuthManager().getToken(context.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                    String friend_id = friend.getUserInfo().getUserId();
                    new FriendAcceptTask(user_id,friend_id,token).execute();
                    friend.setType(Friend.FRIEND.FRIEND);
                    notifyDataSetChanged();

                }
            });
            friendWrapper.getFriendName().setText(friend.getUserInfo().getName()+"("+friend.getUserInfo().getUserId()+")");
            friendWrapper.getFriendName().setTextColor(context.getResources().getColor(R.color.oxford_blue));
            friendWrapper.getFriendNumBookmark().setVisibility(View.GONE);

        } else if(friend.getType().equals(Friend.FRIEND.FRIENDSENT)){
            friendWrapper.getFriendBtn().setText(context.getResources().getString(R.string.waiting_btn));
            friendWrapper.getFriendBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Wait",Toast.LENGTH_SHORT).show();
                }
            });
            friendWrapper.getFriendBtn().setBackgroundResource(R.drawable.friends_waiting_box);
            friendWrapper.getFriendName().setText(friend.getUserInfo().getName()+"("+friend.getUserInfo().getUserId()+")");
            friendWrapper.getFriendName().setTextColor(context.getResources().getColor(R.color.oxford_blue));
            friendWrapper.getFriendNumBookmark().setVisibility(View.GONE);

        } else if(friend.getType().equals(Friend.FRIEND.NOTFRIEND)){
            friendWrapper.getFriendBtn().setText(context.getResources().getString(R.string.accept_btn));
            friendWrapper.getFriendBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Request",Toast.LENGTH_SHORT).show();
                    String user_id = (AuthManager.getAuthManager().getLoginInfo(context.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
                    String token = AuthManager.getAuthManager().getToken(context.getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                    String friend_id = friend.getUserInfo().getUserId();
                    new FriendRequestTask(user_id,friend_id,token).execute();
                    friend.setType(Friend.FRIEND.FRIENDSENT);
                    notifyDataSetChanged();

                }
            });
            friendWrapper.getFriendName().setText(friend.getUserInfo().getName()+"("+friend.getUserInfo().getUserId()+")");
            friendWrapper.getFriendName().setTextColor(context.getResources().getColor(R.color.oxford_blue));
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
            }
            return friendName;
        }

        public TextView getFriendNumBookmark() {
            if(friendNumBookmark==null){
                friendNumBookmark = (TextView)base.findViewById(R.id.num_bookmark);
            }
            return friendNumBookmark;
        }

        public TextView getFriendBtn() {
            if(friendBtn==null){
                friendBtn = (TextView)base.findViewById(R.id.friend_btn);
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
