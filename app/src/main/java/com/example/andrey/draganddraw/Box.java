package com.example.andrey.draganddraw;

import android.graphics.PointF;

//класс с именем Box для хранения данных, определяющих прямоугольник.
public class Box {
    private PointF mCurrent;
    private PointF mOrigin;

    public Box(PointF origin) {
        this.mOrigin = origin;
        this.mCurrent = origin;
    }

    public PointF getmCurrent() {
        return mCurrent;
    }

    public PointF getmOrigin() {
        return mOrigin;
    }

    public void setmCurrent(PointF mCurrent) {
        this.mCurrent = mCurrent;
    }
}
