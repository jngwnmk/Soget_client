package com.markin.app.view.Fragment;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.connector.recommend.RecommendDiscardTask;
import com.markin.app.connector.recommend.RecommendGetTask;
import com.markin.app.model.Bookmark;
import com.markin.app.model.User;
import com.markin.app.view.Activity.SettingActivity;
import com.markin.app.view.Adapter.DiscoverAdapter;
import com.markin.app.view.component.CardStackMoveListener;
import com.markin.app.view.component.MyCardStackView;
import com.markin.app.view.component.SwipeTouchListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-18.
 */
public class DiscoverFragment extends Fragment {
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
    private View emptyView = null;
    private ImageView progressImg = null;
    private ProgressBar progressBar = null;
    private LinearLayout loadMoreLayout = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.discover_layout,container, false);

        settingBtn = (ImageButton)rootView.findViewById(R.id.setting_btn);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //getDiscoverList();
            }
        });
        backgroundImg = (ImageView)rootView.findViewById(R.id.discover_background_img);
        cardNumTextView = (TextView)rootView.findViewById(R.id.discover_circle_current_idx);
        cardNumTextView.setText(String.valueOf(bookmarks.size()));
        totalCardNumTextView = (TextView)rootView.findViewById(R.id.discover_circle_total_num);
        discoverAdapter = new DiscoverAdapter(getActivity(),bookmarks);
        cardStackView = (MyCardStackView)rootView.findViewById(R.id.discover_stack_view);
        /*emptyView = inflater.inflate(R.layout.discover_reload_layout, null);
        progressImg = (ImageView) emptyView.findViewById(R.id.reload_img);
        progressBar = (ProgressBar) emptyView.findViewById(R.id.reload_progress);
        loadMoreLayout = (LinearLayout)emptyView.findViewById(R.id.reload_layout);
        loadMoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"load more",Toast.LENGTH_SHORT).show();
                bookmarks.clear();
                currentIndex = 0;
                getDiscoverList();
                emptyView.setVisibility(View.GONE);
            }
        });
        cardStackView.setEmptyView(emptyView);*/

        cardStackView.setOrientation(SwipeTouchListener.Orientation.Vertical);
        cardStackView.setCardStackMoveListener(new CardStackMoveListener() {

            @Override
            public void cardAdd() {
                //addBookmark(bookmarks.get(currentIndex));
                showAddDialog(bookmarks.get(currentIndex).getUrl(),bookmarks.get(currentIndex));
                treatCardAction();

            }

            @Override
            public void cardDelete() {
                trashBookmark(bookmarks.get(currentIndex).getId());
                treatCardAction();
                //Discard to trashcans
                //Toast.makeText(getActivity().getApplicationContext(), "Discard!!!", Toast.LENGTH_SHORT).show();
            }

            private void treatCardAction(){
                //SettingManager.setLastDiscoverDate(getActivity().getSharedPreferences(SettingManager.LASTDISCOVER,Context.MODE_PRIVATE),bookmarks.get(currentIndex).getDate());
                //bookmarks.remove(0);
                ++currentIndex;
                cardNumTextView.setText(String.valueOf(bookmarks.size()-currentIndex));

                if(bookmarks.size()!=currentIndex) {
                    {
                        if (bookmarks.get(currentIndex).getImg_url().equals("")) {
                            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.picture_no_image)
                                    .placeholder(R.drawable.picture_no_image).fit().centerCrop()
                                    .into(backgroundImg);
                        } else {
                            Picasso.with(getActivity().getApplicationContext()).load(bookmarks.get(currentIndex).getImg_url())
                                    .placeholder(R.drawable.picture_no_image).fit().centerCrop()
                                    .into(backgroundImg);
                        }
                    }
                } else {
                    Toast.makeText(getActivity(),"load more",Toast.LENGTH_SHORT).show();
                    bookmarks.clear();
                    currentIndex = 0;
                    getDiscoverList();
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
                if (object != null) {
                    //Add to My Archive
                    //Toast.makeText(getActivity().getApplicationContext(), "Added to my archive!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //addBookmarkDialog.show(fm,"add_bookmark_dialog");
    }

    private void trashBookmark(String bookmark_id){
        User user = AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        if(user!=null){
            String user_id = user.getUserId();
            String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            new RecommendDiscardTask(null,user_id, bookmark_id, token).execute();
        }
    }

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
                            if(bookmarks.get(currentIndex).getImg_url().equals("")){
                                Picasso.with(getActivity().getApplicationContext()).load(R.drawable.picture_no_image)
                                        .placeholder(R.drawable.picture_no_image).fit().centerCrop()
                                        .into(backgroundImg);
                            } else {
                                Picasso.with(getActivity().getApplicationContext()).load(bookmarks.get(currentIndex).getImg_url())
                                        .placeholder(R.drawable.picture_no_image).fit().centerCrop()
                                        .into(backgroundImg);
                            }

                        } else {
                            Toast.makeText(getActivity(), "No more discover!!",Toast.LENGTH_SHORT).show();
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
                new RecommendGetTask(onTaskCompleted,user_id, token,date,"",0).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("DiscoverFragment","onResume()");
    }
}
