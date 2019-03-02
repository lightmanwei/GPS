package com.example.gps2;

public class MyPosition {
    MyGPosition myGPosition;
    int index = 0;
    String gpsStatus = "?";
    MyPoint point;

    public MyPosition() {
        this.myGPosition = new MyGPosition();
        this.point = new MyPoint();
    }

    public MyPosition(MyPoint myPoint, MyGPosition myGPosition, String gpsStatus, int index) {
        this.myGPosition = new MyGPosition(myGPosition);
        this.point = new MyPoint(myPoint);
        this.gpsStatus = gpsStatus;
        this.index = index;
    }
}
