package com.example.gps2;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class DocumentCreator {
    private Context myContext;
    private ArrayList<MyPosition> myList;
    DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日hh时mm分ss秒");
    Date date = new Date(System.currentTimeMillis());
    String myPath_1 = "/storage/emulated/0/360/myxml/LocListDom2.xml";
    String myPath_2 = Environment.getExternalStorageDirectory() + "/myXml" + dateFormat.format(date) + ".xml";

    public DocumentCreator(Context context, ArrayList<MyPosition> list) {
        myContext = context;
        myList = list;
    }

    public void isSdExit() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            Toast.makeText(myContext, "sd OK", Toast.LENGTH_LONG).show();
    }

    public DocumentBuilder getDocumentBuilder() {
        // 获得DocumentBuilderFactory对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return db;
    }

    private void createFolder() {
        //新建一个File，传入文件夹目录
        File file = new File("/storage/emulated/0/360/myxml");
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            Log.e("?", "?");
            //通过file的mkdirs()方法创建目录中包含却不存在的文件夹
            file.mkdirs();
        }
    }

    public void domCreateXml() {
        //创建DocumentBuilder对象
        DocumentBuilder db = this.getDocumentBuilder();
        //生成一个Dom树
        Document document = db.newDocument();
        //去掉standalone="no"声明,说明只是一个简单的xml,没有特殊DTD(document type definition文档类型定义)规范
        document.setXmlStandalone(true);
        //创建Location根节点
        Element rootElement = document.createElement("Location");
        //创建CountryRegion节点

        for (MyPosition item : myList) {
            Element country = document.createElement("index");
            country.setAttribute("Index", String.valueOf(item.index));
//        country.setAttribute("Code", "1");
            //创建State节点
            Element state = document.createElement("position");
            state.setAttribute("y", String.valueOf(item.point.y));
            state.setAttribute("x", String.valueOf(item.point.x));
            state.setAttribute("gx", String.valueOf(item.myGPosition.x));
            state.setAttribute("gy", String.valueOf(item.myGPosition.y));
            state.setAttribute("gz", String.valueOf(item.myGPosition.z));
            state.setAttribute("gps_status", item.gpsStatus);
            //创建city节点
//        Element city = document.createElement("City");
//        city.setAttribute("Name", "成都");
//        city.setAttribute("Code", "cd");
// 将city是state下的子节点，将city加入到state中
            //  state.appendChild(city);
            //将state是country下的子节点，将state加入到country中
            country.appendChild(state);
            //将country是Location下的子节点，将state加入到country中
            rootElement.appendChild(country);

            //将包含了子节点的rootElement添加到document中

        }
        document.appendChild(rootElement);

        //实例化工厂类，工厂类不能使用new关键字实例化创建对象
        TransformerFactory transFactory = TransformerFactory.newInstance();
        try {
            //创建transformer对象
            Transformer transformer = transFactory.newTransformer();
            //设置换行
            transformer.setOutputProperty(OutputKeys.INDENT, "Yes");
            //构造转换,参数都是抽象类，要用的却是更具体的一些类，这些的类的命名有一些规律的。
            transformer.transform(new DOMSource(document), new StreamResult(myPath_2));
            Toast.makeText(myContext, "ok", Toast.LENGTH_LONG).show();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            Toast.makeText(myContext, "ok1", Toast.LENGTH_LONG).show();
        } catch (TransformerException e) {
            e.printStackTrace();
            Toast.makeText(myContext, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
