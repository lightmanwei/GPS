package com.example.gps2;


import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView positionView;
    private Button buttonSingle;
    private LocationManager locationManager;
    private String locationProvider;
    private ArrayList<MyPosition> myList = new ArrayList<>();
    public Location location;
    public MyPosition position;
    int i = 1;
    MGSensorListener mgSensorListener;
    Context context = this;
    Gps myGps;
    LocalBroadcastManager localBroadcastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        mgSensorListener = new MGSensorListener(context);
        mgSensorListener.go();

        myGps = new Gps(context);
        myGps.go();
        //加速度开始

        positionView = (TextView) findViewById(R.id.positionView);
        positionView.setMovementMethod(ScrollingMovementMethod.getInstance());


        //单次按钮
        buttonSingle = (Button) findViewById(R.id.button_1);
        buttonSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.GPS.MY_BROADCAST");
                localBroadcastManager.sendBroadcast(intent);
                position = new MyPosition(myGps.myPoint, mgSensorListener.gPosition, "network", i);
//                position.gpsStatus = "network";
//                position.point = myGps.myPoint;
//                position.myGPosition = mgSensorListener.gPosition;
//                position.index=i;

                myList.add(position);
                showPosition(position);
                buttonSingle.setText(i++ + "th point");
                //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                //startActivityForResult(intent,0);
                //String gpsStatus = "";
                //position = new MyPosition();//initialize position

                //选择gps方式
//                List<String> providers = locationManager.getProviders(true);
//                if (providers.contains(LocationManager.GPS_PROVIDER)) {
//                    locationProvider = LocationManager.GPS_PROVIDER;
//                    position.gpsStatus = "GPS_PROVIDER";
//                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                            || ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//                        location = locationManager.getLastKnownLocation(locationProvider);
//                    if (location == null) {// && providers.contains(LocationManager.NETWORK_PROVIDER)) {
//                        locationProvider = LocationManager.NETWORK_PROVIDER;
//                        position.gpsStatus = "NETWORK_PROVIDER";
//                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                                || ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
//                            location = locationManager.getLastKnownLocation(locationProvider);
//                        if (location == null) {
//                            Toast.makeText(getApplicationContext(), "没有可用的位置提供器,network可用但无值", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "没有可用的位置提供器,gps无", Toast.LENGTH_SHORT).show();
//                }


                //initialize position

//                if (location != null) {
//                    position.point.x = myGps.myPoint.x;
//                    position.point.y = myGps.myPoint.y;
//                    position.index = i;
//                    position.myGPosition = new MyGPosition(mgSensorListener.gPosition);
//                    myList.add(position);
//
//                    showPosition(position);
//                    buttonSingle.setText(i++ + "th point");
//                }
            }

        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.GPS.MY_BROADCAST");

        localBroadcastManager.registerReceiver(myGps, intentFilter);
        localBroadcastManager.registerReceiver(mgSensorListener, intentFilter);


        //列表按钮
        Button listButton = (Button) findViewById(R.id.button_2);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionView.append(positionToString(myList));
            }
        });


        //文件按钮

        Button fileButton = (Button) findViewById(R.id.button_3);
        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile(myList);
            }
        });
    }

    private void saveFile(ArrayList<MyPosition> list) {
//        String msg = positionToString(list);
//        FileOutputStream outputStream;

//        try {
//            File file = new File(Environment.getExternalStorageDirectory(), "saveList.txt");
//            FileOutputStream outStream = new FileOutputStream(file);
//            outStream.write(msg.getBytes());
//            outStream.flush();
//            outStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//        }

        DocumentCreator documentCreator = new DocumentCreator(context, myList);
        documentCreator.domCreateXml();

//documentCreator.isSdExit();

/*        XmlCreator xmlCreator = new XmlCreator(context);
        xmlCreator.savexml();*/

//        try {
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
//            Date date = new Date(System.currentTimeMillis());
//
//            outputStream = openFileOutput("/sdcard/Listdata" + simpleDateFormat.format(date) + "txt", Context.MODE_APPEND);
//            outputStream.write(msg.getBytes());
//            outputStream.flush();
//            outputStream.close();
//            Toast.makeText(MainActivity.this, "save success", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//        }
    }

    public void showPosition(MyPosition item) {
        String positionstr = "";
        if (item.myGPosition != null)
            positionstr = ("index" + item.index + "\n经度: " + item.point.y + "纬度：" + item.point.x + "\n重力加速度：" + "\nx: " + item.myGPosition.x + "\ny: " + item.myGPosition.y + "\nz: " + item.myGPosition.z + "\ngps方式" + item.gpsStatus + "\n");
        else
            positionstr = "no gposition\n";
        positionView.append(positionstr);
    }

    private String positionToString(ArrayList<MyPosition> positions) {
        String result = "list:\n";
        for (MyPosition item : positions) {
            result += ("index" + item.index + "\n经度: " + item.point.y + "纬度：" + item.point.x + "\n重力加速度：" + "\nx: " + item.myGPosition.x + "\ny: " + item.myGPosition.y + "\nz: " + item.myGPosition.z + "\n");
        }
        return result;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(myGps);
        localBroadcastManager.unregisterReceiver(mgSensorListener);
    }
}
