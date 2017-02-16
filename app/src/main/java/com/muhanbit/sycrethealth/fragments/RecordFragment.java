package com.muhanbit.sycrethealth.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muhanbit.sycrethealth.ContainerActivity;
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

public class RecordFragment extends Fragment implements RecordFragView, ContainerActivity.OnInsertListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ProgressDialog progressDialog;
    private RecordAdapter recordAdapter;
    private RecordFragPresenter mRecordPresenter;

    private ContainerActivity mContainer;
    private View mContainerView;

    public static RecordFragment newInstance(){
        RecordFragment recordFragment = new RecordFragment();

        return recordFragment;
    }

    public RecordFragment() {
    }

    /*
     * Fragment LifeCycle
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_fragment, container, false);
        ButterKnife.bind(this,view);
        mContainerView = container;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recordAdapter = new RecordAdapter(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10),true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recordAdapter);


        RecordFragModel recordFragModel = new RecordFragModelImpl(getContext());
        mRecordPresenter = new RecordFragPresenterImpl(this, recordFragModel); // presenter에 view, model;
        mRecordPresenter.setRecordAdapterView(recordAdapter);
        mRecordPresenter.setRecordAdapterModel(recordAdapter);

        mRecordPresenter.initializeRecord();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContainer = (ContainerActivity) getActivity();
        mContainer.setOnInsertListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
             *  RecordFragView overrid method
             */
    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mContainerView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void progressOnOff(boolean on) {

        if(on){
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려주세요");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    @Override
    public void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Step Delete").setIcon(R.mipmap.ic_launcher).setMessage("정말로 삭제하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mRecordPresenter.deleteRecord(position);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /*
     * ContainerActivity의 OnInsertListenr method
     */
    @Override
    public void onInserted() {
        mRecordPresenter.insertedRecordAtMain();
    }


    /*
     * instance method
     */
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
