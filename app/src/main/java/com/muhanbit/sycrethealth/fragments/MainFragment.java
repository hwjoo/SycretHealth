package com.muhanbit.sycrethealth.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.muhanbit.sycrethealth.ContainerActivity;
import com.muhanbit.sycrethealth.PedometerService;
import com.muhanbit.sycrethealth.R;
import com.muhanbit.sycrethealth.model.MainFragModel;
import com.muhanbit.sycrethealth.model.MainFragModelImpl;
import com.muhanbit.sycrethealth.presenter.MainFragPresenter;
import com.muhanbit.sycrethealth.presenter.MainFragPresenterImpl;
import com.muhanbit.sycrethealth.view.MainFragView;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by hwjoo on 2017-01-13.
 */

public class MainFragment extends Fragment implements MainFragView,
        TimePickerDialog.OnTimeSetListener,PopupMenu.OnMenuItemClickListener{
    private static final int TIME_PICKER_HOUR = 24;
    private static final int TIME_PICKER_MIN = 0;
    private static int refInt; // setTime에서 animation을 위한 reference value

    @BindView(R.id.circle_progress)
    CircleProgressView mCircleProgressView;
    @BindView(R.id.main_overflow)
    ImageView mainOverflow;
    @BindView(R.id.action_btn)
    Button actionBtn;
    @BindView(R.id.step_text)
    TextView stepText;


    private MainFragPresenter mMainFragPresenter;
    private ProgressDialog progressDialog;

    private ContainerActivity mContainer;
    private View mContainerView; // snack bar를 사용하기 위한 View



    public static MainFragment newInstance(){
        /*
         * fragment 초기화, 생성자대신 newInstance의 Bundle 사용
         * 아래와 같이 setArguments로 data setting
         * 이후 onCreteView에서 getArguments로 bundle get 가능
         */
        MainFragment mainFragment = new MainFragment();
//        Bundle data  = new Bundle();
//        oneFragment.setArguments(data);
        return mainFragment;
    }
    public MainFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContainer = (ContainerActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        MainFragModel mainFragModel = new MainFragModelImpl(getContext());
        mMainFragPresenter = new MainFragPresenterImpl(this, mainFragModel);
        ButterKnife.bind(this, view);
        mMainFragPresenter.initCircleProgress(mCircleProgressView);

        mContainerView = container;


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mMainFragPresenter.isServiceRunningCheck("com.muhanbit.sycrethealth.PedometerService")){
            this.setStep(PedometerService.count);
            this.setTime(PedometerService.totalMinute, PedometerService.remainMinute);
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TEST","Fragment onDestroy");
    }

    @OnClick(R.id.circle_progress)
    void onClickCircleProgress(){
        mMainFragPresenter.clickProgress();
    }
    @OnClick(R.id.main_overflow)
    void onClickMainOverflow(){
        mMainFragPresenter.clickOverflow();
    }
    @OnClick(R.id.action_btn)
    void onClickActionBtn(){
        mMainFragPresenter.clickActionBtn();
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
    public void showTimePicker() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this,TIME_PICKER_HOUR,TIME_PICKER_MIN,true);
        /*
         * Android M에서 getColor deprecated. 따라서 아래와 같이 M기준으로 devide
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary,getActivity().getTheme()));
        }else{
            timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        }
        timePickerDialog.show(getActivity().getFragmentManager(),"time_picker");
    }

    @Override
    public void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(),mainOverflow, Gravity.RIGHT);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }
    /*
     * setStep, setTime은 service에서 계산된
     * step과 남은시간을 view에 전달하여 set하기 위한 method
     */
    @Override
    public void setStep(int step) {
        stepText.setText(step +" Step");
    }
    @Override
    public void setTime(int totalMinute, int remainTime) {
        if(remainTime == (totalMinute-1)){
            refInt=totalMinute;
        }else{
            int cal = totalMinute-remainTime;
            refInt = totalMinute-(cal-1);
        }
        mCircleProgressView.setMaxValue(totalMinute);
        mCircleProgressView.setValueAnimated(refInt,remainTime,1000);
        int hour = remainTime/60;
        int minute = remainTime%60;
        mCircleProgressView.setText(hour+"시간 "+minute+"분");
    }
    /*
     * getTotalMinute은 Presenter에서 service를 실행시킬때,
     * 인텐트에 setting된 시간을 전달.
     */
    @Override
    public int getCurrentMinute() {
        return (int) mCircleProgressView.getCurrentValue();
    }

    @Override
    public void changeBtnText(String chText) {
        actionBtn.setText(chText);
    }

    @Override
    public void showSaveDialog(final String startTime, final String endTime, final String date) {
        this.changeBtnText("START");
        /*
         * PedometerService가 종료되고, onDestroy에서
         * start time, end time을 전달한다.(service 시작 시간, service 끝나는시간)
         * date는 presenter에서 db에 저장할때 그 시점의 date를 저장
         * step은 textview에 getText를 통해서 get
         *  presenter에 start time, end time, step 갯수 전달.
         *  presenter에서 model로 start time, endtime, step, date전달하여 SQLite에 저장
         */
        final String stepCount = stepText.getText().toString();

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog, null);
        DialogHolder dialogHolder = new DialogHolder(dialogView);
        dialogHolder.onBind(stepCount,startTime+" - "+endTime,date);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Step save").setIcon(R.mipmap.ic_launcher).setView(dialogView)
                .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                       mMainFragPresenter.insertStepInfo(stepCount,
                             startTime, endTime, date);

                        mMainFragPresenter.initCircleProgress(mCircleProgressView);
                        mMainFragPresenter.initStep(stepText);
                    }
                })
                .setNegativeButton("초기화", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMainFragPresenter.initCircleProgress(mCircleProgressView);
                        mMainFragPresenter.initStep(stepText);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


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
    public ContainerActivity getContainer() {
        return mContainer;
    }

    /*
     * onTimeSet은 timepicker의 listener
     */
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mMainFragPresenter.setTimeForProgress(mCircleProgressView, hourOfDay, minute);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_set_time :
                mMainFragPresenter.clickSetMenu();
                break;
            case R.id.action_reset_time :
                mMainFragPresenter.clickResetMenu(mCircleProgressView);
                break;
        }
        return false;
    }
    /*
     * CustomDialog Holder
     */
    public class DialogHolder{
        @BindView(R.id.dialog_step)
        TextView dialogStep;
        @BindView(R.id.dialog_time)
        TextView dialogTime;
        @BindView(R.id.dialog_date)
        TextView dialogDate;
        public DialogHolder(View view){
            ButterKnife.bind(this,view);
        }
        public void onBind(String step, String time, String date){
            dialogStep.setText(step);
            dialogTime.setText(time);
            dialogDate.setText(date);
        }
    }
}
