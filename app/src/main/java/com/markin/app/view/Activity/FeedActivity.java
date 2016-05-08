package com.markin.app.view.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.callback.RecommendViewActionListener;
import com.markin.app.common.AuthManager;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.bookmark.ArchiveMineTask;
import com.markin.app.connector.recommend.RecommendGetTask;
import com.markin.app.model.Bookmark;
import com.markin.app.model.User;
import com.markin.app.view.Adapter.ArchiveAdapter;
import com.markin.app.view.Fragment.RecommendFragment;
import com.markin.app.view.component.PagedHeadListView.utils.PageTransformerTypes;
import com.markin.app.view.component.RecommendView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by wonmook on 2016. 4. 24..
 */
public class FeedActivity extends AppCompatActivity {


    private static final String TAG = "FeedFragment";
    private RecommendView recommendView= null;

    private ProgressDialog pDialog          = null;
    private ArrayList<Bookmark> recommend   = null;
    private ArrayList<Bookmark> myBookmarks = null;

    private String category = null;
    private ImageButton backBtn =null;
    private TextView titleTv = null;
    private SlidingUpPanelLayout mLayout = null;
    private ListView list = null;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout_alter_1);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading....");


        category = getIntent().getExtras().getString(StaticValues.CATEGORY);
        titleTv = (TextView)findViewById(R.id.feed_title_tv);
        titleTv.setText(category);
        titleTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/FrutigerLTStd-Bold.otf"));

        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recommendView = (RecommendView)findViewById(R.id.recommend_pager);
        //recommendView.setHeaderPageTransformer(PageTransformerTypes.ZOOMOUT);
        list = (ListView)findViewById(R.id.list);

        loadBookmark();
        loadRecommend();

        mLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);

                if(recommend.size()==0){
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                } else {
                    if(newState== SlidingUpPanelLayout.PanelState.ANCHORED){
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                }

            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


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

                    if(recommend.size()==0){
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        Toast.makeText(getApplicationContext(), "No Recommendation!!!", Toast.LENGTH_SHORT).show();
                    }

                    for(int i = 0 ; i < recommend.size() ; ++i){
                        final Bookmark bookmark = recommend.get(i);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("bookmark", bookmark);
                        final android.support.v4.app.Fragment fragment = new RecommendFragment().setSwipeActionListener(new RecommendViewActionListener() {
                            @Override
                            public void discard(String bookmark_id) {
                                recommendView.removeFragmentFromHeader(bookmark_id);
                                recommendView.showNumberIndicator();
                                if(recommendView.getPageCount()==0){
                                    Toast.makeText(getApplicationContext(), "Load More Recommendation.", Toast.LENGTH_SHORT).show();
                                    loadRecommend();
                                }
                            }

                            @Override
                            public void addToBookmark() {

                            }

                            @Override
                            public void swiping() {
                                recommendView.hideNumberIndicator();
                            }

                            @Override
                            public void release() {
                                recommendView.showNumberIndicator();
                            }
                        });
                        fragment.setArguments(bundle);
                        recommendView.addFragmentToHeader(fragment);
                    }

                }
                recommendView.showNumberIndicator();
                pDialog.dismiss();
            }
        };

        try{
            User user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                long date = System.currentTimeMillis();
                pDialog.show();
                new RecommendGetTask(onTaskCompleted,user_id, token,date,category,0).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
        recommendView.hideNumberIndicator();

    }

    private void loadBookmark(){

        myBookmarks = new ArrayList<>();

        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    myBookmarks.clear();
                    myBookmarks.addAll((ArrayList<Bookmark>) object);
                    ArchiveAdapter archiveAdapter = new ArchiveAdapter(getApplicationContext(), myBookmarks);
                    list.setAdapter(archiveAdapter);
                }
                pDialog.dismiss();
            }
        };

        try{
            User user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                pDialog.show();
                new ArchiveMineTask(onTaskCompleted,user_id, token,category,0).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }



    }

    @Override
    public void onResume(){
        super.onResume();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case AddBookmarkActivity.ADDBOOKMARK_SUCCESS:
                String bookmarkId = data.getStringExtra("bookmarkId");
                recommendView.removeFragmentFromHeader(bookmarkId);
                recommendView.showNumberIndicator();
                Log.d(TAG, "ADD Bookmark Success");
                loadBookmark();
                break;
            case AddBookmarkActivity.ADDBOOKMARK_CANCEL:
                Log.d(TAG , "ADD Bookmark Cancel");
                recommendView.showNumberIndicator();
                break;
            default:
                break;
        }
    }

}
