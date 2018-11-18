package com.example.andrey.draganddraw;

import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.Serializable;

//класс с именем Box для хранения данных, определяющих прямоугольник.
public class Box implements Serializable {
    private PointF mCurrent;
    private PointF mOrigin;

    public Box(PointF origin) {
        this.mOrigin = origin;
        this.mCurrent = origin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public void setmCurrent(PointF mCurrent) {
        this.mCurrent = mCurrent;
    }
}
