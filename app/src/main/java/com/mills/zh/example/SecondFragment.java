package com.mills.zh.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangmd on 2019/1/2.
 */

public class SecondFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Factory factory = inflater.getFactory();
        Log.d("SecondFragment", "onCreateView LayoutInflater factory:"+factory);

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        return view;
    }
}
