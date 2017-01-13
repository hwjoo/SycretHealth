package com.muhanbit.sycrethealth.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hwjoo on 2017-01-13.
 */

public class OneFragment extends Fragment {
    public static OneFragment newInstance(){
        /*
         * fragment 초기화, 생성자대신 newInstance의 Bundle 사용
         * 아래와 같이 setArguments로 data setting
         * 이후 onCreteView에서 getArguments로 bundle get 가능
         */
        OneFragment oneFragment = new OneFragment();
//        Bundle data  = new Bundle();
//        oneFragment.setArguments(data);
        return oneFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }
}
