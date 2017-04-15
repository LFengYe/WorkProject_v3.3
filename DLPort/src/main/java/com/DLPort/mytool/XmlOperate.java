package com.DLPort.mytool;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by fuyzh on 16/5/31.
 */
public class XmlOperate {

    /**
     * 生成xml格式字符串
     * @param data
     * @param rootTag
     * @return
     */
    public String createXmlStr(HashMap<String, String> data, String rootTag) {
        try {
            StringWriter writer = new StringWriter();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlSerializer serializer = factory.newSerializer();
            serializer.setOutput(writer);

            serializer.startDocument(null, true);
            serializer.startTag(null, rootTag);
            Iterator iterator = data.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
                serializer.startTag(null, entry.getKey());
                serializer.text(entry.getValue());
                serializer.endTag(null, entry.getKey());
            }
            serializer.endTag(null, rootTag);

            serializer.endDocument();
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    public static void main(String[] args) {
        XmlOperate operate = new XmlOperate();
        System.out.println(operate.createXmlStr(null, "test"));
    }
    */
}
