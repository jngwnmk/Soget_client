package com.markin.app.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by saadati on 10/4/14.
 */
public class MyCardStackView extends AbstractCardsStackView {
    public static final String TAG = "MyCardsStackView";
    private CardStackMoveListener cardStackMoveListener = null;
    public MyCardStackView(Context context) {
        super(context);
    }

    public MyCardStackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCardStackView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CardStackMoveListener getCardStackMoveListener() {
        return cardStackMoveListener;
    }

    public void setCardStackMoveListener(CardStackMoveListener cardStackMoveListener) {
        this.cardStackMoveListener = cardStackMoveListener;
    }

    @Override
    public void onStackGettingEmpty() {
        Log.d(TAG, "Stack getting empty");
    }

    @Override
    public void onSwipedLeft() {
        Log.d(TAG, "Swiped Left");
        super.onSwipedLeft();

    }

    @Override
    public void onSwipedRight() {
        Log.d(TAG, "Swiped Right");
        super.onSwipedRight();

    }

    @Override
    public void onSwipedUp() {
        Log.d(TAG, "Swiped Up");
        super.onSwipedUp();
        cardStackMoveListener.cardDelete();
    }

    @Override
    public void onSwipedDown() {
        Log.d(TAG, "Swiped Down");
        super.onSwipedDown();
        cardStackMoveListener.cardAdd();
    }
}
