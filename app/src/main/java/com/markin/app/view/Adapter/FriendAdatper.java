package com.markin.app.view.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
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
public class FriendAdatper extends BaseExpandableListAdapter{
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<String> groupTitles;
    private ArrayList<Friend> friends;
    private ArrayList<String> sentInvitations;

    public FriendAdatper(Context context, ArrayList<String> groupTitles, ArrayList<Friend> friends, ArrayList<String> sentInvitations){
        this.mContext = context;
        this.groupTitles = groupTitles;
        this.friends = friends;
        this.sentInvitations = sentInvitations;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(groupPosition==0){
            return sentInvitations.size();
        } else if(groupPosition==1){
            return friends.size();
        }
        return 0;
    }

    @Override
    public String getGroup(int groupPosition) {

        return groupTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(groupPosition==0){
            return sentInvitations.get(childPosition);
        } else if(groupPosition==1){
            return friends.get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        View row = convertView;
        GroupTitleWrapper groupTitleWrapper =null;
        if(row==null){
            row = layoutInflater.inflate(R.layout.friend_list_header, null);
            groupTitleWrapper = new GroupTitleWrapper(row);
            row.setTag(groupTitleWrapper);
        } else {
            groupTitleWrapper = (GroupTitleWrapper)row.getTag();

        }

        if(sentInvitations.size()==0 && groupPosition ==0){
            groupTitleWrapper.getTitle().setVisibility(View.GONE);
        } else{
            groupTitleWrapper.getTitle().setVisibility(View.VISIBLE);
            groupTitleWrapper.getTitle().setText(groupTitles.get(groupPosition));
        }
        return row;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        View row = convertView;
        FriendWrapper friendWrapper =null;
        if(row==null){
            row = layoutInflater.inflate(R.layout.friend_list_row, null);
            friendWrapper = new FriendWrapper(row);
            row.setTag(friendWrapper);
        } else {
            friendWrapper = (FriendWrapper)row.getTag();

        }
        if(groupPosition==0){
            String sentInvitation = (String)getChild(groupPosition, childPosition);
            friendWrapper.getFriendName().setText("코드번호 : "+sentInvitation);
            friendWrapper.getFriendNumBookmark().setVisibility(View.GONE);
            friendWrapper.getFriendBtn().setVisibility(View.INVISIBLE);

        } else if(groupPosition==1){
            final Friend friend = (Friend)getChild(groupPosition, childPosition);
            if(friend.getType().equals(Friend.FRIEND.FRIEND)){
                friendWrapper.getFriendBtn().setVisibility(View.INVISIBLE);
                friendWrapper.getFriendName().setText(friend.getUserInfo().getName());
                if(friend.getUserInfo().getBookmarks()!=null)
                {
                    friendWrapper.getFriendNumBookmark().setText(friend.getUserInfo().getBookmarks().size() +" Socket");
                }
                else {
                    friendWrapper.getFriendNumBookmark().setText("0 Socket");
                }

            }
        }

        return row;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupTitleWrapper{
        private View base;
        private TextView title;

        public GroupTitleWrapper(View base) {this.base = base;}

        public TextView getTitle(){
            if(title==null){
                title = (TextView)base.findViewById(R.id.friend_list_header_title);
                title.setTypeface(Typeface.createFromAsset(mContext.getAssets(),"fonts/AppleSDGothicNeo-Medium.otf"));
                title.setTextColor(mContext.getResources().getColor(R.color.sub_text_color_80));
            }
            return title;
        }
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
