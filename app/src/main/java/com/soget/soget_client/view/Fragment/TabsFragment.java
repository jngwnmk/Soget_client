package com.soget.soget_client.view.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.soget.soget_client.R;

/**
 * Created by wonmook on 2015-03-18.
 */
public class TabsFragment extends Fragment implements TabHost.OnTabChangeListener{
    private static final String TAG = "FragmentTabs";
    public static final String TAB_HOME = "home";
    public static final String TAB_ARCHIVE = "archive";
    public static final String TAB_FRIEND = "friend";

    private View mRoot;
    private TabHost mTabHost;
    private int mCurrentTab;

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
        updateTab(TAB_HOME, R.id.tab_1);
    }

    private void setupTabs() {
        mTabHost.setup(); // you must call this before adding your tabs!
        mTabHost.addTab(newTab(TAB_HOME, 1, R.id.tab_1));
        mTabHost.addTab(newTab(TAB_ARCHIVE, 2, R.id.tab_2));
        mTabHost.addTab(newTab(TAB_FRIEND, 3, R.id.tab_3));
    }

    private TabHost.TabSpec newTab(String tag, int labelId, int tabContentId) {
        Log.d(TAG, "buildTab(): tag=" + tag);

        View indicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab_view,(ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
        TextView tab_title = (TextView) indicator.findViewById(R.id.tabs_text);
        tab_title.setText(labelId+"");

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
            return;
        }
        if (TAB_ARCHIVE.equals(tabId)) {
            updateTab(tabId, R.id.tab_2);
            mCurrentTab = 1;
            return;
        }

        if (TAB_FRIEND.equals(tabId)) {
            updateTab(tabId, R.id.tab_3);
            mCurrentTab = 2;
            return;
        }
    }

    private void updateTab(String tabId, int placeholder) {
        FragmentManager fm = getFragmentManager();
        if(TAB_HOME.equals(tabId)) {
            if (fm.findFragmentByTag(tabId) == null) {
                fm.beginTransaction().replace(placeholder, new HomeFragment(), tabId).commit();
            }
        }

        if(TAB_ARCHIVE.equals(tabId)) {
            if (fm.findFragmentByTag(tabId) == null) {
                fm.beginTransaction().replace(placeholder, new ArchiveFragment(), tabId).commit();
            }
        }

        if(TAB_FRIEND.equals(tabId)) {
            if (fm.findFragmentByTag(tabId) == null) {
                fm.beginTransaction().replace(placeholder, new FriendsFragment(), tabId).commit();
            }
        }
    }

}
