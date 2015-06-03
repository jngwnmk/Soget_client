package com.soget.soget_client.view.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.soget.soget_client.R;

/**
 * Created by wonmook on 15. 5. 31..
 */
public class AppleGothicNeoSemiBoldTextView extends TextView{
    private static final String TextViewTag = "TextViewTag";
    public AppleGothicNeoSemiBoldTextView(Context context) {
        super(context);
    }

    public AppleGothicNeoSemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context,attrs);
    }

    public AppleGothicNeoSemiBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.AppleGothicNeoSemiBoldTextView);
        String customFont = a.getString(R.styleable.AppleGothicNeoSemiBoldTextView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e(TextViewTag, "Could not get typeface: " + e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }

}
