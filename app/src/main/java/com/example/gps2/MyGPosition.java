package com.example.gps2;

public class MyGPosition {
    double x = 0, y = 0, z = 0, maxvalue;
    long stamp, second;
    boolean movingboolean = false;

    public MyGPosition() {

    }

    public MyGPosition(MyGPosition myGPosition) {//,int mmaxvalue,long mstamp,long msecond,boolean mmoving){
        x = myGPosition.x;
        y = myGPosition.y;
        z = myGPosition.z;
    }

}
