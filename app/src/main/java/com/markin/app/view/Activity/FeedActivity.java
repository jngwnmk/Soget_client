package com.markin.app.view.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.markin.app.R;
import com.markin.app.callback.OnTaskCompleted;
import com.markin.app.callback.RecommendViewActionListener;
import com.markin.app.common.AuthManager;
import com.markin.app.common.SogetUtil;
import com.markin.app.common.StaticValues;
import com.markin.app.connector.bookmark.ArchiveMineTask;
import com.markin.app.connector.recommend.RecommendGetTask;
import com.markin.app.model.Bookmark;
import com.markin.app.model.Comment;
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
public class FeedActivity extends AppCompatActivity{


    private static final String TAG = "FeedFragment";

    //Dialog
    private ProgressDialog pDialog          = null;

    //Recommend Horizontal List
    private RecommendView recommendView= null;
    private ArrayList<Bookmark> recommend   = null;
    private int recommendPage;

    //Titlebar
    private String category = null;
    private ImageButton backBtn =null;
    private TextView titleTv = null;

    //ArchiveList
    private ArrayList<Bookmark> myBookmarks = null;
    private SlidingUpPanelLayout mLayout = null;
    private ListView list = null;
    private boolean mLockListView;
    private boolean isLastPage;
    private LayoutInflater mInflater;
    private int bookmarkPage;
    private ArchiveAdapter archiveAdapter;

    //Blank Archive View
    private LinearLayout blankArchiveMsgLayout = null;
    private TextView  emptyArchiveMsg1 = null;
    private TextView  emptyArchiveMsg2 = null;
    private ImageView emptyArchiveImg  = null;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_layout_alter_1);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading....");

        setupTitleBar();
        setupRecommendList();
        setupBookmarkList();
        setupBlankBookmarkList();

        loadBookmark(bookmarkPage++);
        loadRecommend();

    }

    private void setupTitleBar(){
        category = getIntent().getExtras().getString(StaticValues.CATEGORY);
        titleTv = (TextView)findViewById(R.id.feed_title_tv);
        titleTv.setText(category);
        titleTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/FrutigerLTStd-Bold.otf"));

        backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
    }

    private void setupRecommendList(){
        recommendView = (RecommendView)findViewById(R.id.recommend_pager);
        //recommendView.setHeaderPageTransformer(PageTransformerTypes.ZOOMOUT);

        recommend = new ArrayList<>();


    }

    private void setupBlankBookmarkList(){
        blankArchiveMsgLayout = (LinearLayout)findViewById(R.id.blank_archive_msg_layout);
        emptyArchiveMsg1 = (TextView)findViewById(R.id.empty_archive_msg_1);
        emptyArchiveMsg2 = (TextView)findViewById(R.id.empty_archive_msg_2);
        emptyArchiveImg  = (ImageView)findViewById(R.id.blank_archive_shape_img);
        emptyArchiveMsg1.setTextColor(getResources().getColor(R.color.charcol_text_color_66));
        emptyArchiveMsg2.setTextColor(getResources().getColor(R.color.charcol_text_color_33));
        emptyArchiveMsg1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));
        emptyArchiveMsg2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AppleSDGothicNeo-Medium.otf"));

        blankArchiveMsgLayout.setVisibility(View.GONE);
        emptyArchiveImg.setVisibility(View.GONE);


        mLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);

                if (recommend.size() == 0) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                } else {
                    if (newState == SlidingUpPanelLayout.PanelState.ANCHORED) {
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
    private void setupBookmarkList(){
        list = (ListView)findViewById(R.id.list);
        myBookmarks = new ArrayList<>();

        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list.addFooterView(mInflater.inflate(R.layout.loading_footer, null));
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
                // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
                int count = totalItemCount - visibleItemCount;
                Log.i(TAG, "onScroll(Count):" + count);
                if (firstVisibleItem >= count && totalItemCount != 0
                        && mLockListView == false && !isLastPage) {
                    Log.i(TAG, "Loading next items");
                    loadBookmark(bookmarkPage++);
                }
            }
        });
        archiveAdapter = new ArchiveAdapter(FeedActivity.this, myBookmarks);
        list.setAdapter(archiveAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                User user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));

                boolean didComment = false;
                boolean didLike = false;
                boolean didSocket = false;

                if (user != null) {
                    String user_id = user.getUserId();
                    Comment checkCommend = new Comment();
                    checkCommend.setUserId(user_id);
                    if (myBookmarks.get(position).getComments().contains(checkCommend)) {
                        didComment = true;
                    } else {
                        didComment = false;
                    }

                    if (myBookmarks.get(position).getLike().contains(user_id)) {
                        didLike = true;
                    } else {
                        didLike = false;
                    }

                    if (myBookmarks.get(position).getFollowers().contains(user_id)) {
                        didSocket = true;
                    } else {
                        didSocket = false;
                    }
                }
                String url = myBookmarks.get(position).getUrl();
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                Bundle extras = new Bundle();
                extras.putString(WebViewActivity.WEBVIEWURL, url);
                extras.putString(StaticValues.BOOKMARKID, myBookmarks.get(position).getId());
                extras.putBoolean(StaticValues.ISBOOKMARKLIKE, didLike);
                extras.putBoolean(StaticValues.ISBOOKMARKSOCKET, didSocket);
                extras.putBoolean(StaticValues.ISBOOKMARKCOMMENT, didComment);
                extras.putBoolean(StaticValues.ISMYBOOKMARK, true);

                intent.putExtras(extras);
                startActivity(intent);
                showActivityAnmination();
            }
        });

    }

    private void loadRecommend(){


        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){

                    //recommend.clear();
                    recommend.addAll((ArrayList<Bookmark>) object);

                    if(recommend.size()==0){
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        Toast.makeText(getApplicationContext(), "No Recommendation!!!", Toast.LENGTH_SHORT).show();
                    }

                    for(int i = 0 ; i < recommend.size() ; ++i){
                        final Bookmark bookmark = recommend.get(i);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("bookmark", bookmark);
                        android.support.v4.app.Fragment fragment = new RecommendFragment().setSwipeActionListener(new RecommendViewActionListener() {
                            @Override
                            public void discard(String bookmark_id) {
                                recommendView.removeFragmentFromHeader(bookmark_id);
                                recommendView.showNumberIndicator();
                                if(recommendView.getPageCount()==0){
                                    Toast.makeText(getApplicationContext(), "Load More Recommendation.", Toast.LENGTH_SHORT).show();
                                    recommend.clear();
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

    private void loadBookmark(int page){

        OnTaskCompleted onTaskCompleted;
        onTaskCompleted = new OnTaskCompleted(){
            @Override
            public void onTaskCompleted(Object object) {
                if(object!=null){
                    //myBookmarks.clear();
                    ArrayList<Bookmark> loadedBookmark = (ArrayList<Bookmark>) object;
                    if(loadedBookmark.size()==0){
                        isLastPage = true;
                    } else {
                        isLastPage = false;
                    }
                    myBookmarks.addAll(loadedBookmark);
                    updateBookmarkListView();
                }
                mLockListView = false;
                pDialog.dismiss();
            }
        };

        try{
            User user = AuthManager.getAuthManager().getLoginInfo(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
            if(user!=null){
                String user_id = user.getUserId();
                String token = AuthManager.getAuthManager().getToken(getSharedPreferences(AuthManager.LOGIN_PREF, Context.MODE_PRIVATE));
                pDialog.show();
                mLockListView = true;
                new ArchiveMineTask(onTaskCompleted,user_id, token,category,page).execute();
            }

        } catch (NullPointerException ex){
            ex.printStackTrace();
        }

    }

    private void updateBookmarkListView(){
        if(myBookmarks.size()==0){
            blankArchiveMsgLayout.setVisibility(View.VISIBLE);
            emptyArchiveImg.setVisibility(View.VISIBLE);
        } else {
            blankArchiveMsgLayout.setVisibility(View.GONE);
            emptyArchiveImg.setVisibility(View.GONE);
        }
        archiveAdapter.notifyDataSetChanged();
    }

    private void updateRecommendListView(){

    }

    private void showActivityAnmination(){
        //SogetUtil.showNewActivityAnim(this);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case AddBookmarkActivity.ADDBOOKMARK_SUCCESS:
                String bookmarkId = data.getStringExtra("bookmarkId");
                recommendView.removeFragmentFromHeader(bookmarkId);
                recommendView.showNumberIndicator();
                Log.d(TAG, "ADD Bookmark Success");
                bookmarkPage = 0;
                myBookmarks.clear();
                loadBookmark(bookmarkPage);
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
