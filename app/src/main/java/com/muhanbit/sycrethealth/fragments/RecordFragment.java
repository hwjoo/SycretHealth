package com.muhanbit.sycrethealth.fragments;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muhanbit.sycrethealth.R;
import com.muhanbit.sycrethealth.Record;
import com.muhanbit.sycrethealth.RecordAdapter;
import com.muhanbit.sycrethealth.model.RecordFragModel;
import com.muhanbit.sycrethealth.model.RecordFragModelImpl;
import com.muhanbit.sycrethealth.presenter.RecordFragPresenter;
import com.muhanbit.sycrethealth.presenter.RecordFragPresenterImpl;
import com.muhanbit.sycrethealth.view.RecordFragView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hwjoo on 2017-01-13.
 */
/*
 *  구조      RecordFragView                < - >  RecordFragPresenter  < - > RecordFragModel
 *            AdapterContract.RecordView    < - > RecordFragPresenter
 *            AdapterContract.RecordModel   < - > RecordFragPresenter
 *
 *            RecordFragModel에서 DB or server Data를 받아 update.
 *            AdapterContract.RecordModel은 RecordFragPresenter를 통해
 *
 */

public class RecordFragment extends Fragment implements RecordFragView {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    RecordAdapter recordAdapter;

    RecordFragPresenter mRecordPresenter;


    public static RecordFragment newInstance(){
        RecordFragment recordFragment = new RecordFragment();

        return recordFragment;
    }

    public RecordFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_fragment, container, false);
        ButterKnife.bind(this,view);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recordAdapter = new RecordAdapter(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10),true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recordAdapter);
        ArrayList testList = new ArrayList();
        for (int i =0; i<10; i++){
            Record record = new Record(String.valueOf(i),"10:00","12:00","2017-01-18");
            testList.add(record);
        }
        recordAdapter.setRecords(testList);
        recordAdapter.notifyDataSetChanged();

        RecordFragModel recordFragModel = new RecordFragModelImpl();
        mRecordPresenter = new RecordFragPresenterImpl(this, recordFragModel); // presenter에 view, model;
        mRecordPresenter.setRecordAdapterView(recordAdapter);
        mRecordPresenter.setRecordAdapterModel(recordAdapter);


        return view;
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(getContext(),toast,Toast.LENGTH_SHORT).show();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;

        private boolean includeEdge;
        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge){
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if(includeEdge){
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) *spacing / spanCount;
                if(position <spanCount){
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            }else{
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if(position >= spanCount){
                    outRect.top = spacing;
                }
            }
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
    private int dpToPx(int dp){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics() ));
    }
}
