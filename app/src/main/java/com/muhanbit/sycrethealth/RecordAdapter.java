package com.muhanbit.sycrethealth;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.muhanbit.sycrethealth.contract.AdapterContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hwjoo on 2017-01-18.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> implements
        AdapterContract.RecordView, AdapterContract.RecordModel{

    private Context mContext;
    private List<Record> mRecords;
    private OnDeleteClickListener mOnDeleteClickListener;


    /*
     * Adapter 내부에 view holder 정의.
     */
    public class RecordViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.step_record)
        TextView stepRecord;
        @BindView(R.id.time_record)
        TextView timeRecord;
        @BindView(R.id.date_record)
        TextView dateRecord;
        @BindView(R.id.deleteImg)
        ImageView deleteImg;

        public RecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void onBind(Record record, int position){
            stepRecord.setText(record.getStep());
            timeRecord.setText(record.getStartTime() + " - "+record.getEndTime());
            dateRecord.setText(record.getDate());
        }
    }

    public RecordAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_card, parent, false);
        return new RecordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecordViewHolder holder, final int position) {
        if(holder == null) return;
        holder.onBind(mRecords.get(position), position);
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnDeleteClickListener !=null){
                    mOnDeleteClickListener.onDeleteClick(position);

                    ValueAnimator animator = new ValueAnimator().ofFloat(0,360);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            float value = (float) valueAnimator.getAnimatedValue();
                            holder.deleteImg.setRotation(value);
                        }
                    });
                    animator.setInterpolator(new LinearInterpolator());
                    animator.setDuration(500);
                    animator.start();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecords !=null ? mRecords.size() : 0;
    }

     /*
      * 아래부터 AdapterContract.RecordModel의 Override method.
      */

    @Override
    public void setRecords(ArrayList<Record> records){
        this.mRecords = records;
    }

    @Override
    public void addRecordItem(Record record) {
        if(this.mRecords != null){
            this.mRecords.add(record);
        }

    }

    @Override
    public void removeRecordItem(int position) {
        if(this.mRecords !=null && this.mRecords.size() !=0){
            this.mRecords.remove(position);
        }
    }

    @Override
    public void clearItem() {
        if(mRecords !=null){
            mRecords.clear();
        }
    }

    @Override
    public Record getItem(int position) {
        return mRecords.get(position);
    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public void setDeleteClickListener(OnDeleteClickListener listener) {
        /*
         * RecordFragPresenterImpl에서 OnDeleteClickLister를 implement하였기 때문에 다형성에 의해
         * 파라미터 listenr는 RecordPresenterImpl이되고, OnDeleteClcikListener의 onDeleteClick이
         * RecordPresenterImpl에 override되어있다. 따라서, holder의 deleteImage에 setOnclickListener를
         * 연결하여 onclick시, mOnDeleteClickListener.onDeleteClick으로 preseter에서 처리하도록
         * 넘겨줄수 있다.
         */
        this.mOnDeleteClickListener = listener;
    }


}
