package com.muhanbit.sycrethealth;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.muhanbit.sycrethealth.presenter.MainFragPresenter;
import com.muhanbit.sycrethealth.presenter.MainFragPresenterImpl;
import com.muhanbit.sycrethealth.view.MainFragView;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by hwjoo on 2017-01-17.
 */

public class PedometerService extends Service implements SensorEventListener {
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;


    private static final int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;
    private TimeThread timeThread;

    public static int count;
    public static int totalMinute;
    public static int remainMinute;




    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);
            if(gabOfTime > 100){
                lastTime = currentTime;
                x = sensorEvent.values[SensorManager.DATA_X];
                y = sensorEvent.values[SensorManager.DATA_Y];
                z = sensorEvent.values[SensorManager.DATA_Z];

                speed = Math.abs(x+y+z - lastX - lastY- lastZ) / gabOfTime * 10000;
                if(speed > SHAKE_THRESHOLD){
                    count++;
                    MainFragPresenterImpl.getmMainFragView().setStep(count);

                    Log.d("TEST","Count :"+count);

                }
                lastX = sensorEvent.values[DATA_X];
                lastY = sensorEvent.values[DATA_Y];
                lastZ = sensorEvent.values[DATA_Z];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TEST", "service oncreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TEST", "service onStartcommand");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerormeterSensor, SensorManager.SENSOR_DELAY_GAME);

        totalMinute = intent.getIntExtra("total_minute",0);
        remainMinute = totalMinute;
        TimeHandler timeHandler = new TimeHandler();
        timeThread = new TimeThread(timeHandler);
        timeThread.start();


        return START_NOT_STICKY ;
    }


    @Override
    public void onDestroy() {
        Log.d("TEST", "service ondestroy");
        if(sensorManager !=null){
            sensorManager.unregisterListener(this);
        }
        if(timeThread != null){
            timeThread.setRun(false);
            timeThread.interrupt();
            timeThread = null;
        }
        super.onDestroy();
    }
    public class TimeHandler extends Handler{

        public TimeHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainFragPresenterImpl.getmMainFragView().setTime(totalMinute,remainMinute);
            /*
             * 시간완료
             */
            if(remainMinute == 0){
                Intent intent = new Intent(getBaseContext(), PedometerService.class);
                stopService(intent);
                MainFragPresenterImpl.getmMainFragView().showSaveDialog();
                /*
                 * 앱 종료시, view도 같이 죽기때문에, showSaveDialog()호출시 NullPointerException 발생
                 * 완료시 WakefulBroadcast로 받아서 activity로 전달필요.
                 * ex) nullCheck 후 전송
                 */

                Log.d("TEST", "handler 내부 time완료");
            }
        }
    }

    public class TimeThread extends Thread {
        boolean run;
        TimeHandler timeHandler;

        public TimeThread(TimeHandler timeHandler) {
            this.run = true;
            this.timeHandler = timeHandler;
        }

        public boolean isRun() {
            return run;
        }

        public void setRun(boolean run) {
            this.run = run;
        }

        @Override
        public void run() {
            /*
             * 1분간격으로 total minute 1씩 감소 ( 1분씩감소 )
             * handler에 message를 send하여 MainFragement의
             * CircleProgress에 access한다.
             */

            while(run){
                Log.d("TEST","thread running");
                if(remainMinute == 0){
                    break;
                }
                try {
                    Thread.sleep(6*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("TEST","thread interruptedException");
                    return;
                }

                remainMinute --;
                timeHandler.sendEmptyMessage(0);
            }
        }
    }


}
