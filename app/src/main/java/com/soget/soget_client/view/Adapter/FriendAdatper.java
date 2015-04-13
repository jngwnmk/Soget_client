package com.soget.soget_client.view.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soget.soget_client.R;
import com.soget.soget_client.model.Friend;

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

        Friend friend = (Friend)getItem(position);
        friendWrapper.getFriendName().setText(friend.getUserInfo().getName());
        if(friend.getUserInfo().getBookmarks()!=null)
        {
            friendWrapper.getFriendNumBookmark().setText(friend.getUserInfo().getBookmarks().size() +" Gets");
        }
        else {
            friendWrapper.getFriendNumBookmark().setText("0 Gets");
        }

        if(friend.getType().equals(Friend.FRIEND.FRIEND)){
            friendWrapper.getFriendBtn().setVisibility(View.INVISIBLE);
            friendWrapper.getBackgroundLayout().setBackgroundColor(context.getResources().getColor(R.color.white));
        } else if(friend.getType().equals(Friend.FRIEND.FRIENDRECEIVE)){
            friendWrapper.getFriendBtn().setText("FriendReceive");
            friendWrapper.getBackgroundLayout().setBackgroundColor(context.getResources().getColor(R.color.blur_grey));
        } else if(friend.getType().equals(Friend.FRIEND.FRIENDSENT)){
            friendWrapper.getFriendBtn().setText("FriendSent");
            friendWrapper.getBackgroundLayout().setBackgroundColor(context.getResources().getColor(R.color.blur_grey));
        }

        return row;
    }

    private class FriendWrapper{
        private View base;
        private TextView friendName;
        private TextView friendNumBookmark;
        private Button   friendBtn;
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

        public Button getFriendBtn() {
            if(friendBtn==null){
                friendBtn = (Button)base.findViewById(R.id.friend_btn);
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