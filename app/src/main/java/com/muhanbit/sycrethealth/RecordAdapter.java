package com.muhanbit.sycrethealth;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void setRecords(ArrayList<Record> records){
        this.mRecords = records;
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_card, parent, false);
        return new RecordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, final int position) {
        if(holder == null) return;
        holder.onBind(mRecords.get(position), position);
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnDeleteClickListener !=null){
                    mOnDeleteClickListener.onDeleteClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecords !=null ? mRecords.size() : 0;
    }

    @Override
    public void addRecordItem(ArrayList<Record> records) {
        this.mRecords = records;
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
        this.mOnDeleteClickListener = listener;
    }


}
