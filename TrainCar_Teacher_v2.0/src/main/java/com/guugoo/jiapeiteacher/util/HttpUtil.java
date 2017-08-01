package com.guugoo.jiapeiteacher.util;



import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gpw on 2016/8/1.
 * --加油
 */
public class HttpUtil {
    private static String localhost = "http://101.201.74.192:8076/";
    public static String url_login = localhost + "T_Basic/TeacherLogin";
    public static String url_bookings= localhost + "T_Work/TeacherBookings";
    public static String url_scheduling= localhost + "T_Work/TeacherScheduling";
    public static String url_batchCancel= localhost + "T_Work/BatchCancelClass";
    public static String url_cancelClassByTime= localhost + "T_Work/CancelClassByTime";
    public static String url_classStudentDetails= localhost + "T_Work/ClassStudentDetails";
    public static String url_scanCodeStart= localhost + "T_Work/ScanCodeStart";
    public static String url_classEnd= localhost + "T_Work/ClassEnd";
    public static String url_teacherComment= localhost + "T_Work/TeacherComment";
    public static String url_checkCalssStart= localhost + "T_Work/CheckCalssStart";
    public static String url_isCancelBookings= localhost + "T_Work/IsCancelBookings";
    public static String url_getVerificationCode= localhost + "S_Basic/StudentGetVerificationCode";
    public static String url_personalInfo = localhost + "T_UserInfo/TeacherPersonalInformationit";
    public static String url_updatePass = localhost + "T_UserInfo/TeacherUpdatePass";
    public static String url_nickname = localhost + "T_UserInfo/TeacherNickname";
    public static String url_headPortrait = localhost + "T_UserInfo/TeacherHeadPortrait";
    public static String url_notice = localhost + "S_MicroBlog/StudentNotice";
    public static String url_noticeDetails = localhost + "S_MicroBlog/StudentNoticeDetails";
    public static String url_retrievePassword = localhost + "T_Basic/TeacherRetrievePassword";
    public static String url_forum = localhost + "S_MicroBlog/StudentForum";
    public static String url_teacherMessage = localhost + "T_UserInfo/TeacherMessage";
    public static String url_forumComment = localhost + "S_MicroBlog/StudentForumComment";
    public static String url_clickALike = localhost + "S_MicroBlog/StudentClickALike";
    public static String url_publish = localhost + "S_MicroBlog/StudentPublish";
    public static String url_replies = localhost + "S_MicroBlog/StudentReplies";
    public static String url_information = localhost + "T_UserInfo/TeacherInformation";
    public static String url_statute = localhost + "T_Basic/TeacherStatute";
    public static String url_statuteDetails = localhost + "T_Basic/TeacherStatuteDetails";
    public static String url_myScore = localhost + "T_UserInfo/TeacherMyScore";
    public static String url_evaluate= localhost + "T_UserInfo/TeacherEvaluate";
    public static String url_recommend= localhost + "T_UserInfo/TeacherRecommend";
    public static String url_gpsStorage= localhost + "Gps/GpsStorage";
    public static String url_exitLogin= localhost + "S_Basic/SignOut";
    public static String url_CoachLogin = localhost + "T_Work/CoachLogin";
    public static String url_GetCardCheck = localhost + "T_Work/GetCardCheck";
    public static String url_CoachLogOut = localhost + "T_Work/CoachLogOut";
    public static String url_StudentLogOut = localhost + "T_Work/StudentLogOut";
    public static String url_StudentsGetBlanch = localhost + "Students/GetBlanch";


    public static String httpPost(String strURL, JsonObject strParam,String token) {
        Log.i("当前请求", strURL);
        String resultData = "";
        try {
            URL url = new URL(strURL);
            // 使用HttpURLConnection打开连接
            HttpURLConnection urlConn = null;
            DataOutputStream out = null;
            try {
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                // 设置以POST方式
                urlConn.setRequestMethod("POST");
                // 设置连接超时
                urlConn.setConnectTimeout(5000);
                urlConn.setReadTimeout(5000);
                // Post 请求不能使用缓存
                urlConn.setUseCaches(false);
                urlConn.setInstanceFollowRedirects(true);
                // 设置指定的请求头字段的值

                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.setRequestProperty("Authorization","Basic "+ Base64.encodeToString(token.getBytes(),Base64.DEFAULT));

                urlConn.connect();
                out = new DataOutputStream(urlConn.getOutputStream());
                // 编码设置
                // URLEncoder.encode(strParam, "UTF_8");
                // 将要上传的内容写入流中
                out.write(strParam.toString().getBytes("UTF-8"));
                out.flush();
                out.close();
                // 获取数据
                int code = urlConn.getResponseCode();
                System.out.println("code:" + code);
                if (code == 200) {
                    InputStream in = urlConn.getInputStream();
                    resultData = readStream(in).replace("\"","");
                    resultData= EncryptUtils.decryptDES(resultData);
                } else if (code == 401) {
                    resultData= "unAuthorization";
                } else {
                    resultData = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return resultData;
    }


    public static String httpPost1(String strURL, JsonObject strParam) {
        String resultData = "";
        try {
            URL url = new URL(strURL);
            // 使用HttpURLConnection打开连接
            HttpURLConnection urlConn = null;
            DataOutputStream out = null;
            try {
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                // 设置以POST方式
                urlConn.setRequestMethod("POST");
                // 设置连接超时
                urlConn.setConnectTimeout(5000);
                urlConn.setReadTimeout(5000);
                // Post 请求不能使用缓存
                urlConn.setUseCaches(false);
                urlConn.setInstanceFollowRedirects(true);
                // 设置指定的请求头字段的值

                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Accept", "application/json");
               // urlConn.setRequestProperty("Authorization","Basic"+ Base64.encodeToString(token.getBytes(),Base64.DEFAULT));

                urlConn.connect();
                out = new DataOutputStream(urlConn.getOutputStream());
                // 编码设置
                // URLEncoder.encode(strParam, "UTF_8");
                // 将要上传的内容写入流中
                out.write(strParam.toString().getBytes("UTF-8"));
                out.flush();
                out.close();
                // 获取数据
                int code = urlConn.getResponseCode();
                if (code == 200) {
                    InputStream in = urlConn.getInputStream();
                    resultData = readStream(in).replace("\"","");
                    resultData= EncryptUtils.decryptDES(resultData);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    public static String readStream(InputStream in) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = -1;
        byte[] bytes = new byte[1024];
        while ((len = in.read(bytes)) != -1) {
            baos.write(bytes, 0, len);
        }
        in.close();
        return new String(baos.toByteArray());

    }

}