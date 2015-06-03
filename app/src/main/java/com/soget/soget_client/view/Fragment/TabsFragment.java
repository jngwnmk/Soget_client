package com.soget.soget_client.view.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;

import com.soget.soget_client.R;

/**
 * Created by wonmook on 2015-03-18.
 */
public class TabsFragment extends Fragment implements TabHost.OnTabChangeListener{
    private static final String TAG = "FragmentTabs";
    public static final String TAB_HOME = "home";
    public static final String TAB_ARCHIVE = "archive";
    public static final String TAB_FRIEND = "friend";

    private DiscoverFragment discoverFragment = null;
    private ArchiveFragment archiveFragment = null;
    private FriendsFragment friendsFragment = null;

    private View mRoot;
    private TabHost mTabHost;
    private int mCurrentTab=0;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.tabs_fragment, null);
        mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
        setupTabs();
        return mRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        mTabHost.setOnTabChangedListener(this);
        mTabHost.setCurrentTab(mCurrentTab);
        // manually start loading stuff in the first tab
        //updateTab(TAB_HOME, R.id.tab_1);
    }

    private void setupTabs() {
        mTabHost.setup(); // you must call this before adding your tabs!
        mTabHost.addTab(newTab(TAB_HOME, R.drawable.discover_1, R.id.tab_1));
        mTabHost.addTab(newTab(TAB_ARCHIVE, R.drawable.archive_0, R.id.tab_2));
        mTabHost.addTab(newTab(TAB_FRIEND, R.drawable.friends_0, R.id.tab_3));
    }

    private TabHost.TabSpec newTab(String tag, int tabImgSrc, int tabContentId) {
        Log.d(TAG, "buildTab(): tag=" + tag);

        View indicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab_view,(ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
        ImageView tab_img = (ImageView) indicator.findViewById(R.id.tabs_image);
        tab_img.setImageResource(tabImgSrc);
//        TextView tab_title = (TextView) indicator.findViewById(R.id.tabs_text);
//        tab_title.setText(labelId+"");

        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
        tabSpec.setIndicator(indicator);
        tabSpec.setContent(tabContentId);
        return tabSpec;
    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d(TAG, "onTabChanged(): tabId=" + tabId);
        if (TAB_HOME.equals(tabId)) {
            updateTab(tabId, R.id.tab_1);
            mCurrentTab = 0;
            updateTabView(R.drawable.discover_1,0);
            updateTabView(R.drawable.archive_0,1);
            updateTabView(R.drawable.friends_0,2);

            return;
        }
        if (TAB_ARCHIVE.equals(tabId)) {
            updateTab(tabId, R.id.tab_2);
            mCurrentTab = 1;
            updateTabView(R.drawable.discover_0,0);
            updateTabView(R.drawable.archive_1,1);
            updateTabView(R.drawable.friends_0,2);
            return;
        }

        if (TAB_FRIEND.equals(tabId)) {
            updateTab(tabId, R.id.tab_3);
            updateTabView(R.drawable.discover_0,0);
            updateTabView(R.drawable.archive_0,1);
            updateTabView(R.drawable.friends_1,2);
            mCurrentTab = 2;
            return;
        }
    }

    private void updateTabView(int tabImgSrc,int index){
        ImageView iv = (ImageView) mTabHost.getTabWidget().getChildAt(index).findViewById(R.id.tabs_image);
        iv.setImageDrawable(getResources().getDrawable(tabImgSrc));

    }

    private void updateTab(String tabId, int placeholder) {
        FragmentManager fm = getFragmentManager();
        if(TAB_HOME.equals(tabId)) {
            if (fm.findFragmentByTag(tabId) == null) {
                if(discoverFragment ==null){
                    discoverFragment = new DiscoverFragment();

                }
                fm.beginTransaction().replace(placeholder, discoverFragment, tabId).commit();

            }
        }

        if(TAB_ARCHIVE.equals(tabId)) {
            if (fm.findFragmentByTag(tabId) == null) {
                if(archiveFragment==null){
                    archiveFragment = new ArchiveFragment();
                }
                fm.beginTransaction().replace(placeholder, archiveFragment, tabId).commit();

            }

        }

        if(TAB_FRIEND.equals(tabId)) {
            if (fm.findFragmentByTag(tabId) == null) {
                if(friendsFragment==null){
                    friendsFragment = new FriendsFragment();
                }
                fm.beginTransaction().replace(placeholder, friendsFragment, tabId).commit();
            }

        }
        System.out.println("updateTab():TabsFragment");
    }


}
