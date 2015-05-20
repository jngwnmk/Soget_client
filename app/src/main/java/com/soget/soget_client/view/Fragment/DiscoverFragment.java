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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.common.SettingManager;
import com.soget.soget_client.connector.AddBookmarkRequestTask;
import com.soget.soget_client.connector.DiscoverRequestTask;
import com.soget.soget_client.connector.MyArchiveRequestTask;
import com.soget.soget_client.connector.TrashBookmarkRequestTask;
import com.soget.soget_client.connector.WebExtractor;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.model.User;
import com.soget.soget_client.view.Activity.IntroActivity;
import com.soget.soget_client.view.Activity.MainActivity;
import com.soget.soget_client.view.Activity.RegisterActivity;
import com.soget.soget_client.view.Activity.SettingActivity;
import com.soget.soget_client.view.Adapter.DiscoverAdapter;
import com.soget.soget_client.view.component.CardStackMoveListener;
import com.soget.soget_client.view.component.MyCardStackView;
import com.soget.soget_client.view.component.SwipeTouchListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-18.
 */
public class DiscoverFragment extends Fragment{
    private ImageButton settingBtn = null;
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
                addBookmark(bookmarks.get(currentIndex).getId());
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


    private void trashBookmark(String bookmark_id){
        User user = AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        if(user!=null){
            String user_id = user.getUserId();
            String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            //pDialog.show();
            new TrashBookmarkRequestTask(null,user_id, bookmark_id, token).execute();
        }
    }

    private void addBookmark(String bookmark_id){

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
            new AddBookmarkRequestTask(onTaskCompleted,user_id, bookmark_id, token).execute();
        }
    }

    private void getDiscoverList(){

        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null) {
                    //Log.d("DiscoverFragment", ((ArrayList<Bookmark>) object).toString());
                    ArrayList<Bookmark> raw_bookmark = ((ArrayList<Bookmark>) object);
                    OnTaskCompleted webExtractTaskComplete = new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(Object object) {

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
                    };
                    new WebExtractor(webExtractTaskComplete, raw_bookmark).execute();
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
                new DiscoverRequestTask(onTaskCompleted,user_id, token,date).execute();
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