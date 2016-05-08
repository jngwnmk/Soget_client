package com.markin.app.callback;

import android.support.v4.app.Fragment;

/**
 * Created by wonmook on 2016. 4. 24..
 */
public interface RecommendViewActionListener {
    void discard(String bookmark_id);
    void addToBookmark();
    void swiping();
    void release();
}
