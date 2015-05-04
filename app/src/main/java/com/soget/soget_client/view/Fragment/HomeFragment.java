package com.soget.soget_client.view.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soget.soget_client.R;
import com.soget.soget_client.callback.OnTaskCompleted;
import com.soget.soget_client.common.AuthManager;
import com.soget.soget_client.connector.MyArchiveRequestTask;
import com.soget.soget_client.connector.WebExtractor;
import com.soget.soget_client.model.Bookmark;
import com.soget.soget_client.view.Adapter.DiscoverAdapter;
import com.soget.soget_client.view.component.MyCardStackView;
import com.soget.soget_client.view.component.SwipeTouchListener;

import java.util.ArrayList;

/**
 * Created by wonmook on 2015-03-18.
 */
public class HomeFragment extends Fragment{
    private MyCardStackView cardStackView = null;
    private ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
    private DiscoverAdapter discoverAdapter = null;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.home_layout,container, false);

        discoverAdapter = new DiscoverAdapter(inflater.getContext(),bookmarks);

        cardStackView = (MyCardStackView)rootView.findViewById(R.id.discover_stack_view);
        cardStackView.setOrientation(SwipeTouchListener.Orientation.Vertical);
        cardStackView.setAdapter(discoverAdapter);

        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Loading....");
        pDialog.show();

        return rootView;
    }

    private void updateDiscoverList(){
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                discoverAdapter = new DiscoverAdapter(getActivity().getBaseContext(),bookmarks);
                cardStackView.setAdapter(discoverAdapter);
                discoverAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getDiscoverList(){
        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {

                Log.d("HomeFragment", ((ArrayList<Bookmark>) object).toString());
                ArrayList<Bookmark> raw_bookmark = ((ArrayList<Bookmark>) object);
                OnTaskCompleted webExtractTaskComplete = new OnTaskCompleted(){
                    @Override
                    public void onTaskCompleted(Object object) {
                        bookmarks.clear();
                        bookmarks.addAll((ArrayList<Bookmark>) object);
                        updateDiscoverList();
                        pDialog.dismiss();
                    }
                };
                new WebExtractor(webExtractTaskComplete,raw_bookmark).execute();
            }
        };
        String user_id = (AuthManager.getAuthManager().getLoginInfo(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE))).getUserId();
        String token = AuthManager.getAuthManager().getToken(getActivity().getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
        new MyArchiveRequestTask(onTaskCompleted,user_id, token).execute();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("HomeFragment","onResume()");
        getDiscoverList();
    }
}
