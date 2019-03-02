package com.example.gps2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class MGSensorListener extends BroadcastReceiver implements SensorEventListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private double mX, mY, mZ;
    private long lasttimestamp = 0;
    Calendar mCalendar;
    Context context;
    //ArrayList<MyGPosition> list = new ArrayList<>();
    MyGPosition gPosition = new MyGPosition();

    public MGSensorListener(Context tempcontext) {
        context = tempcontext;
    }

    public MGSensorListener() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    protected void go() {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
        if (null == mSensorManager) {
            Log.e(TAG, "deveice not support SensorManager");
        }
        // 参数三，检测的精准度
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);// SENSOR_DELAY_GAME
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //gPosition = new MyGPosition();
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            mCalendar = Calendar.getInstance();
            long stamp = mCalendar.getTimeInMillis() / 1000l;// 1393844912
            gPosition.stamp = stamp;

            int second = mCalendar.get(Calendar.SECOND);// 53
            gPosition.second = second;

            double px = Math.abs(mX - x);
            double py = Math.abs(mY - y);
            double pz = Math.abs(mZ - z);
//            Log.e(TAG, "pX:" + px + "  pY:" + py + "  pZ:" + pz + "    stamp:"
//                    + stamp + "  second:" + second);
            double maxvalue = getMaxValue(px, py, pz);
            gPosition.maxvalue = maxvalue;

            if (maxvalue > 2 && (stamp - lasttimestamp) > 30) {
                lasttimestamp = stamp;
                gPosition.movingboolean = true;
//                Log.e(TAG, " sensor isMoveorchanged....");
//                textviewF.setText("检测手机在移动..");
            }

            mX = x;
            mY = y;
            mZ = z;
            gPosition.x = px;
            gPosition.y = py;
            gPosition.z = pz;
            // list.add(gPosition);

        }
    }

    /**
     * 获取一个最大值
     *
     * @param px
     * @param py
     * @param pz
     * @return
     */
    public double getMaxValue(double px, double py, double pz) {
        double max = 0;
        if (px > py && px > pz) {
            max = px;
        } else if (py > px && py > pz) {
            max = py;
        } else if (pz > px && pz > py) {
            max = pz;
        }

        return max;
    }

}
