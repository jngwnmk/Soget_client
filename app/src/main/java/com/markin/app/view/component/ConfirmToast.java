package com.markin.app.view.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.markin.app.R;

/**
 * Created by wonmook on 15. 6. 7..
 */
public class ConfirmToast extends Toast {
    Context mContext;
    public ConfirmToast(Context context) {
        super(context);
        mContext = context;
    }

    public void showToast(String body, int duration){
        LayoutInflater inflater;
        View v;
        if(false){
            Activity act = (Activity)mContext;
            inflater = act.getLayoutInflater();
            v = inflater.inflate(R.layout.confirm_layout, null);
        }else{  // same
            inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.confirm_layout, null);
        }
        TextView text = (TextView) v.findViewById(R.id.confirm_message_tv);
        text.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/AppleSDGothicNeo-SemiBold.otf"));

        text.setText(body);

        show(this,v,duration);
    }

    private void show(Toast toast, View v, int duration){
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(v);
        toast.show();
    }
}


