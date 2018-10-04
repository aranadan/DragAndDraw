package com.example.andrey.draganddraw;


import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();


    /*MotionEvent — класс, описывающий событие
    касания, включая его позицию и действие*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //координаты X и Y упаковываются в объекте PointF
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
                //Пользователь прикоснулся к экрану
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";

                // Сброс текущего состояния
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
                //Пользователь перемещает палец по экрану
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setmCurrent(current);
                    invalidate();
                }
                break;
                //Пользователь отводит палец от экрана
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                break;
                //Родительское представление перехватило событие касания
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                break;
        }
        Log.d(TAG, action + " at x=" + current.x +
                ", y=" + current.y);
        return true;
    }

    // Используется при создании представления в коде
    public BoxDrawingView(Context context) {
        this(context,null);
    }

    // Используется при заполнении представления по разметке XML
    public BoxDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
