package com.markin.app.view.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.common.AuthManager;
import com.markin.app.connector.recommend.RecommendGetTask;
import com.markin.app.model.Bookmark;
import com.markin.app.model.User;
import com.markin.app.view.Adapter.ArchiveAdapter;
import com.markin.app.view.component.PagedHeadListView.PagedHeadListView;
import com.markin.app.view.component.PagedHeadListView.utils.PageTransformerTypes;
import com.markin.app.view.component.RecommendView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wonmook on 2016. 4. 13..
 */
public class FeedFragment extends Fragment {
    public static final String TAG = "FeedFragment";
    private RecommendView recommendView= null;

    private ProgressDialog pDialog          = null;
    private ArrayList<Bookmark> recommend   = null;
    private ArrayList<Bookmark> myBookmarks = null;

    private SlidingUpPanelLayout mLayout = null;
    private ListView list = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getTheme().applyStyle(R.style.AppTheme, true);
        View rootView = inflater.inflate(R.layout.feed_layout_alter_1, container, false);
        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Loading....");

        recommendView = (RecommendView) rootView.findViewById(R.id.recommend_pager);
        recommendView.setHeaderPageTransformer(PageTransformerTypes.ZOOMOUT);
        list = (ListView)rootView.findViewById(R.id.list);

        loadRecommend();
        loadBookmark();

        mLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState== SlidingUpPanelLayout.PanelState.ANCHORED){
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

                }
                //Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);



        return rootView;
    }

    private void loadRecommend(){

        recommend = new ArrayList<>();

        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    recommend.clear();
                    recommend.addAll((ArrayList<Bookmark>) object);

                    for(Bookmark bookmark : recommend){
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("bookmark", bookmark);
                        android.support.v4.app.Fragment fragment = new RecommendFragment();
                        fragment.setArguments(bundle);
                        recommendView.addFragmentToHeader(fragment);
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
                long date = System.currentTimeMillis();
                pDialog.show();
                new RecommendGetTask(onTaskCompleted,user_id, token,date,"",0).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

    }

    private void loadBookmark(){
        ArrayList<Bookmark> mockItemList = new ArrayList<Bookmark>();

        for (int i = 0; i < 10; i++)
            mockItemList.add(new Bookmark());

        ArchiveAdapter mockListAdapter = new ArchiveAdapter(getActivity(), mockItemList);
        list.setAdapter(mockListAdapter);

    }
}