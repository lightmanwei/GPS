package com.example.gps2;


import java.util.Iterator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Gps extends BroadcastReceiver {

    private LocationManager lm;
    private static final String TAG = "GpsActivity";
    public Context context;
    public MyPoint myPoint = new MyPoint();
    Location location;

    public Gps(Context context) {
        this.context = context;
    }

    public Gps() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {


    }

    public void go() {
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //判断GPS是否正常启动
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "请开启GPS导航...", Toast.LENGTH_SHORT).show();
            //返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
            return;
        }

        //为获取地理位置信息时设置查询条件
        // String bestProvider = lm.getBestProvider(getCriteria(), true);
        //如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            location = lm.getLastKnownLocation(bestProvider);

            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

//          if(location==null){
//                Toast.makeText(context,"fail",Toast.LENGTH_SHORT).show();
//            }else{
//            myPoint.x = location.getLatitude();
//            myPoint.y = location.getAltitude();}
            //updateView(location);

        }
        //监听状态
        lm.addGpsStatusListener(listener);
        //绑定监听，有4个参数
        //参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
        //参数2，位置信息更新周期，单位毫秒
        //参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
        //参数4，监听
        //备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

        // 1秒更新一次，或最小位移变化超过1米更新一次；
        //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);//gps_provider
        if (location != null) {
            myPoint.x = location.getLatitude();
            myPoint.y = location.getLongitude();
        }
    }

    GpsStatus.Listener listener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
        }
    };
    //位置监听
    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //updateView(location);
//            Log.i(TAG, "时间：" + location.getTime());
//            Log.i(TAG, "经度：" + location.getLongitude());
//            Log.i(TAG, "纬度：" + location.getLatitude());
//            Log.i(TAG, "海拔：" + location.getAltitude());
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
//            switch (status) {
//                //GPS状态为可见时
//                case LocationProvider.AVAILABLE:
//                    Log.i(TAG, "当前GPS状态为可见状态");
//                    break;
//                //GPS状态为服务区外时
//                case LocationProvider.OUT_OF_SERVICE:
//                    Log.i(TAG, "当前GPS状态为服务区外状态");
//                    break;
//                //GPS状态为暂停服务时
//                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    Log.i(TAG, "当前GPS状态为暂停服务状态");
//                    break;
//            }
        }

        public void onProviderEnabled(String provider) {
//            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                Location location = lm.getLastKnownLocation(provider);
//                myPoint.x = location.getLatitude();
//                myPoint.y = location.getAltitude();
            //updateView(location);
//            }
        }

        public void onProviderDisabled(String provider) {
            //updateView(null);
        }


    };

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }
}

