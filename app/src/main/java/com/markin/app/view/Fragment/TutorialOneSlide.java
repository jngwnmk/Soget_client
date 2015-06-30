package com.markin.app.view.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.markin.app.R;
import com.markin.app.callback.PageMove;

/**
 * Created by wonmook on 15. 6. 12..
 */
public class TutorialOneSlide extends Fragment {
    private Button nextBtn = null;
    private PageMove pageMove = null;

    public PageMove getPageMove() {
        return pageMove;
    }

    public void setPageMove(PageMove pageMove) {
        this.pageMove = pageMove;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tutorial_one_layout, container, false);
        nextBtn = (Button)v.findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageMove.next();
            }
        });
        return v;
    }
}
