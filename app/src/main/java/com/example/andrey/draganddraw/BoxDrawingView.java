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
        boolean isQuad = false;


        //изначально был метод event.getAction()
        switch (event.getActionMasked()) {

            //первое касание к экрану
            case MotionEvent.ACTION_DOWN:

                /*При каждом получении события ACTION_DOWN в поле mCurrentBox сохраняется новый объект Box с базовой точкой, соответствующей позиции события.*/

                action = "ACTION_DOWN";

                //если существуют уже квадраты на холсте
                if (mBoxen.size() > 0) {
                    Log.d(TAG, "проверка на попадание в квадрат");
                    //если каcанием попал в существующий квадрат
                    for (Box box : mBoxen) {
                        if (event.getX() >= box.getOrigin().x && event.getX() <= box.getCurrent().x && event.getY() >= box.getOrigin().y && event.getY() <= box.getCurrent().y) {
                            Log.d(TAG, "попал в квадрат " + event.getX() + " " + event.getY());
                            mCurrentBox = box;
                            isQuad = true;
                            break;
                        }
                    }
                }

                if (!isQuad) {
                    // Сброс текущего состояния
                    Log.d(TAG, "если не попал в квадрат");
                    mCurrentBox = new Box(current);
                    Log.d(TAG, "создал новый квадрат");
                    mBoxen.add(mCurrentBox);
                    Log.d(TAG, "добавил квадрат в список");
                }
                break;

                //последующие касания
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getActionIndex() == 1){
                    Log.d(TAG, "начальная позиция второго пальца" + event.getX(1) + " " + event.getY(1));
                }
                break;

                //срабатывает при отпускании каждого пальца кроме первого
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getActionIndex() == 1){
                    Log.d(TAG, "конечная позиция второго пальца" + event.getX(1) + " " + event.getY(1));
                }
                break;

                //Пользователь перемещает палец по экрану
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";

                //отслеживаю движение второго пальца
                if (event.getPointerCount() == 2){
                Log.d(TAG, "позиция второго пальца" + event.getX(1) + " " + event.getY(1));
                }

                if (mCurrentBox != null) {
                    mCurrentBox.setmCurrent(current);
                    invalidate();  /*invalidate()  заставляет BoxDrawingView перерисовать себя, чтобы пользователь видел прямоугольник в процессе перетаскивания. */
                }


                break;
                //Пользователь отводит первый палец от экрана
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";



                mCurrentBox = null;
                Log.d(TAG, "обнулил текущий квадрат");

                break;
                //Родительское представление перехватило событие касания
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;


        }
        //Log.d(TAG, action + " at x=" + current.x + ", y=" + current.y);
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
        //вращение холста
        //canvas.rotate(10);
        for (Box box : mBoxen) {
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
            count++;
            //Log.d(TAG, "Box number " + count);
        }
    }

    // если не присвоить представлению ИД, методы onSaveInstanceState и onRestoreInstanceState не работают
    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(TAG, "onSaveInstanceState");
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.setmBoxList(mBoxen);
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.d(TAG, "onRestoreInstanceState");
        //сохраняю состояние представления
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mBoxen = ss.getmBoxList();
    }

    private static class SavedState extends BaseSavedState {
        private List<Box> mBoxList;

        public SavedState(Parcelable superState) {
            super(superState);
        }
        public List<Box> getmBoxList() {
            return mBoxList;
        }
        public void setmBoxList(List<Box> mBoxList) {
            this.mBoxList = mBoxList;
        }

    }
}
