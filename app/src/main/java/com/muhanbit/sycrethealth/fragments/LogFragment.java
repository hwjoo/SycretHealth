package com.muhanbit.sycrethealth.fragments;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.muhanbit.sycrethealth.Log;
import com.muhanbit.sycrethealth.R;
import com.muhanbit.sycrethealth.SharedPreferenceBase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hwjoo on 2017-01-13.
 */

public class LogFragment extends Fragment {
    @BindView(R.id.request_log_text)
    TextView requestLogText;
    @BindView(R.id.response_log_text)
    TextView responseLogText;

    public static LogFragment newInstance(){
        LogFragment logFragment = new LogFragment();

        return logFragment;
    }

    public LogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View logView = inflater.inflate(R.layout.log_fragment, container, false);
        ButterKnife.bind(this,logView);

        return logView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log log = Log.getInstance();
        ArrayList<String> requests = log.getRequestLogs();
        ArrayList<String> responses = log.getResponseLogs();
        String requestLogString;
        String responseLogString;
        StringBuilder sb = new StringBuilder();

        for(int i =0 ; i<requests.size(); i++){
            sb.append( (i+1)+". "+requests.get(i) + "\n");
        }
        requestLogString = sb.toString();
        sb = new StringBuilder();

        for(int i =0 ; i<responses.size(); i++){
            sb.append( (i+1)+". "+responses.get(i) + "\n");
        }
        responseLogString =sb.toString();

        requestLogText.setText(requestLogString);
        responseLogText.setText(responseLogString);
    }
    /*
     * ContainerActivity에서 viewpager의 OnPageChangeListener에서
     * LogFragment가 selected됬을경우 refreshLogText()실행.
     */
    public  void refreshLogText(){
        Log log = Log.getInstance();
        ArrayList<String> requests = log.getRequestLogs();
        ArrayList<String> responses = log.getResponseLogs();
        String requestLogString;
        String responseLogString;
        StringBuilder sb = new StringBuilder();

        for(int i =0 ; i<requests.size(); i++){
            sb.append( (i+1)+". "+requests.get(i) + "\n\n");
        }
        requestLogString = sb.toString();
        sb = new StringBuilder();

        for(int i =0 ; i<responses.size(); i++){
            sb.append( (i+1)+". "+responses.get(i) + "\n\n");
        }
        responseLogString =sb.toString();

        requestLogText.setText(requestLogString);
        responseLogText.setText(responseLogString);
    }
}
