package com.gpw.app.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 * ---个人专属
 */

public class CommAdTimeInfo {

    /**
     * GetTime : 2016-11-16 17:27:33
     * List : [{"AddressId":9,"Receipter":"1","ReceiptTel":"1","ReceiptAddress":"金茂深圳jw万豪酒店 (广东省深圳市福田区深南大道6005号)","Lat":22.542512,"Lng":114.038717},{"AddressId":8,"Receipter":"123","ReceiptTel":"123456786","ReceiptAddress":"coco park (广东省深圳市龙岗区龙岗大道辅路)","Lat":22.697198,"Lng":114.234969},{"AddressId":7,"Receipter":"123","ReceiptTel":"1234567","ReceiptAddress":"深圳市飞比电子科技有限公司 (广东省深圳市龙岗区甘李二路)","Lat":22.64846,"Lng":114.133964},{"AddressId":6,"Receipter":"123","ReceiptTel":"123456","ReceiptAddress":"阿叔牛腩档 (广东省深圳市福田区园岭六街10)","Lat":22.560253,"Lng":114.104392},{"AddressId":5,"Receipter":"122","ReceiptTel":"12353678","ReceiptAddress":"华里酒店(h-life) (广东省深圳市南山区锦绣北街2号)","Lat":22.54303,"Lng":114.002569},{"AddressId":4,"Receipter":"123","ReceiptTel":"123156489","ReceiptAddress":"沃尔玛(龙华店) (广东省深圳市宝安区民塘路)","Lat":22.634693,"Lng":114.032536}]
     */

    private String GetTime;
    private java.util.List<CommonAdInfo> List;
    public String getGetTime() {
        return GetTime;
    }
    public void setGetTime(String GetTime) {
        this.GetTime = GetTime;
    }
    public List<CommonAdInfo> getList() {
        return List;
    }

    public void setList(List<CommonAdInfo> List) {
        this.List = List;
    }

}
