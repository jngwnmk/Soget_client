package com.markin.app.view.component;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.markin.app.R;

/**
 * Created by wonmook on 2016. 7. 15..
 */
public class DottedProgressDialog extends Dialog{
    public DottedProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 지저분한(?) 다이얼 로그 제목을 날림
        setContentView(R.layout.dotted_progressbar); // 다이얼로그에 박을 레이아웃
    }
}
