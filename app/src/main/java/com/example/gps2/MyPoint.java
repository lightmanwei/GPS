package com.example.gps2;

public class MyPoint {
    double x = 0, y = 0;

    public MyPoint() {
    }

    public MyPoint(MyPoint myPoint) {
        this.x = myPoint.x;
        this.y = myPoint.y;
    }
}
