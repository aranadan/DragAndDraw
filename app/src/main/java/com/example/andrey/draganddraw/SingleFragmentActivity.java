package com.example.andrey.draganddraw;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public abstract class SingleFragmentActivity extends AppCompatActivity {

   protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SingleFragmentActivity", "onCreate Activity");
        setContentView(getLayoutResId());
        //создаю представление активности
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
    }
    //метод возвращает идентификатор макета
    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

}
