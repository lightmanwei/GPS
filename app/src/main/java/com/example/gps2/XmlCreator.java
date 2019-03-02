package com.example.gps2;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class XmlCreator {
    ArrayList<MyPosition> list;
    Context context;

    public XmlCreator(ArrayList<MyPosition> myList, Context context) {
        this.list = myList;
        this.context = context;
    }

    public XmlCreator(Context context) {
        this.context = context;
    }


}
