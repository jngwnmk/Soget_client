package com.soget.soget_client.view.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.common.StaticValues;
import com.soget.soget_client.connector.bookmark.BookmarkAddTask;
import com.soget.soget_client.connector.discover.DiscoverDiscardTask;
import com.soget.soget_client.connector.discover.DiscoverGetTask;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.model.User;
import com.soget.soget_client.view.Activity.SettingActivity;
import com.soget.soget_client.view.Activity.WebViewActivity;
import com.soget.soget_client.view.Adapter.DiscoverAdapter;
import com.soget.soget_client.view.component.CardStackMoveListener;
import com.soget.soget_client.view.component.MyCardStackView;
import com.soget.soget_client.view.component.SwipeTouchListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-18.
 */
public class DiscoverFragment extends Fragment{
    private ImageButton settingBtn = null;
    private ImageButton addBtn = null;
    private MyCardStackView cardStackView = null;
    private ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
    private DiscoverAdapter discoverAdapter = null;
    private ProgressDialog pDialog;
    private ImageView backgroundImg =null;
    private TextView cardNumTextView = null;
    private TextView totalCardNumTextView = null;
    private int currentIndex = 0;
    private FrameLayout discoverRefreshBtn = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.home_layout,container, false);





        settingBtn = (ImageButton)rootView.findViewById(R.id.setting_btn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Setting",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });

        addBtn = (ImageButton)rootView.findViewById(R.id.add_get_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog("",null);
            }
        });
        discoverRefreshBtn = (FrameLayout)rootView.findViewById(R.id.discover_refresh_btn);
        discoverRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDiscoverList();
            }
        });
        backgroundImg = (ImageView)rootView.findViewById(R.id.discover_background_img);
        cardNumTextView = (TextView)rootView.findViewById(R.id.discover_circle_current_idx);
        cardNumTextView.setText(String.valueOf(bookmarks.size()));
        totalCardNumTextView = (TextView)rootView.findViewById(R.id.discover_circle_total_num);
        discoverAdapter = new DiscoverAdapter(getActivity(),bookmarks);
        cardStackView = (MyCardStackView)rootView.findViewById(R.id.discover_stack_view);
        cardStackView.setOrientation(SwipeTouchListener.Orientation.Vertical);
        cardStackView.setCardStackMoveListener(new CardStackMoveListener() {

            @Override
            public void cardAdd() {
                //addBookmark(bookmarks.get(currentIndex));
                showAddDialog(bookmarks.get(currentIndex).getUrl(),bookmarks.get(currentIndex));
                treatCardAction();
                //Add to My Archive
                Toast.makeText(getActivity().getApplicationContext(), "Added to my archive!!!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void cardDelete() {
                trashBookmark(bookmarks.get(currentIndex).getId());
                treatCardAction();
                //Discard to trashcans
                Toast.makeText(getActivity().getApplicationContext(), "Discard!!!", Toast.LENGTH_SHORT).show();
            }

            private void treatCardAction(){
                //SettingManager.setLastDiscoverDate(getActivity().getSharedPreferences(SettingManager.LASTDISCOVER,Context.MODE_PRIVATE),bookmarks.get(currentIndex).getDate());
                //bookmarks.remove(0);
                ++currentIndex;
                cardNumTextView.setText(String.valueOf(bookmarks.size()-currentIndex));
                if(bookmarks.size()==currentIndex){
                    System.out.println("Reload");
                    bookmarks.clear();
                    currentIndex = 0;
                    getDiscoverList();
                    return;
                } else {
                    Picasso.with(getActivity().getApplicationContext()).load(bookmarks.get(currentIndex).getImg_url())
                            .placeholder(R.drawable.picture_no_image).fit().centerCrop()
                            .into(backgroundImg);
                }



            }
        });
        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Loading....");
        getDiscoverList();
        return rootView;
    }

    public void showAddDialog(String url,Bookmark ref_bookmark){
        //Show Add Dialog
        FragmentManager fm = getFragmentManager();
        AddBookmarkDialog addBookmarkDialog = new AddBookmarkDialog();
        addBookmarkDialog.updateInputUrl(url);
        addBookmarkDialog.setRefBookmark(ref_bookmark);
        addBookmarkDialog.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object object) {
                //loadMyArchive();
            }
        });
        addBookmarkDialog.show(fm,"add_bookmark_dialog");
    }

    private void trashBookmark(String bookmark_id){
        User user = AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        if(user!=null){
            String user_id = user.getUserId();
            String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            //pDialog.show();
            new DiscoverDiscardTask(null,user_id, bookmark_id, token).execute();
        }
    }

    /*private void addBookmark(Bookmark ref_bookmark){

        //Show AddBookmarkDialog
        //showAddDialog();
        /*
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {
                pDialog.dismiss();
            }
        };
        User user = AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        if(user!=null){
            String user_id = user.getUserId();
            String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            pDialog.show();
            new BookmarkAddTask(onTaskCompleted,user_id, bookmark_id, token).execute();
        }

    }*/

    private void getDiscoverList(){

        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null) {
                    if(object!=null){
                        bookmarks.clear();
                        bookmarks.addAll((ArrayList<Bookmark>)object);
                        //What the fuck...Why do I have to call twice!!!!!!!??????
                        cardStackView.setAdapter(discoverAdapter);
                        discoverAdapter.notifyDataSetChanged();
                        //cardStackView.setAdapter(discoverAdapter);
                        //discoverAdapter.notifyDataSetChanged();
                        cardNumTextView.setText(String.valueOf(bookmarks.size()));
                        totalCardNumTextView.setText("/"+String.valueOf(bookmarks.size()));
                        if(bookmarks.size()>0){
                            Picasso.with(getActivity().getApplicationContext()).load(bookmarks.get(currentIndex).getImg_url())
                                    .placeholder(R.drawable.picture_no_image).fit().centerCrop()
                                    .into(backgroundImg);
                        } else {
                            System.out.println("Nothing to do recommend");
                        }

                    }

                }
                pDialog.dismiss();
            }
        };
        try{
            User user = AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                long date = System.currentTimeMillis();//SettingManager.getLastDiscoverDate(getActivity().getSharedPreferences(SettingManager.LASTDISCOVER,Context.MODE_PRIVATE));
                pDialog.show();
                new DiscoverGetTask(onTaskCompleted,user_id, token,date).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

    }

    public void loadDiscoverCard(){

        getDiscoverList();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("DiscoverFragment","onResume()");
    }
}
