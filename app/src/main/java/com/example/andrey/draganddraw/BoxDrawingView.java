package com.example.andrey.draganddraw;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;


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
                /*При каждом получении события ACTION_DOWN в поле mCurrentBox сохраняется новый
                объект Box с базовой точкой, соответствующей позиции события.*/

                // Сброс текущего состояния
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
                //Пользователь перемещает палец по экрану
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setmCurrent(current);

                    /*invalidate() в случае ACTION_MOVE. Он заставляет BoxDrawingView перерисовать себя, чтобы пользователь видел прямоугольник
                    в процессе перетаскивания. */
                    invalidate();
                }
                break;
                //Пользователь отводит палец от экрана
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
                //Родительское представление перехватило событие касания
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
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

        // Прямоугольники рисуются полупрозрачным красным цветом (ARGB)
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);
        // Фон закрашивается серовато-белым цветом
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }
    @Override
    protected void onDraw(Canvas canvas) {
// Заполнение фона
        int count = 0;
        canvas.drawPaint(mBackgroundPaint);
        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
            count++;
            Log.d(TAG, "Box number " + count);
        }
    }

// застрял здесь на странице 619
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.mBoxList = mBoxen;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mBoxen = ss.mBoxList;
    }

    private static class SavedState extends BaseSavedState {
        private List<Box> mBoxList ;

        public SavedState(Parcelable superState) {
            super(superState);
        }
    }
}
