package com.guugoo.jiapeistudent.MinorActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.guugoo.jiapeistudent.Adapter.ForumCommentAdapter;
import com.guugoo.jiapeistudent.App.Constant;
import com.guugoo.jiapeistudent.Data.Forum;
import com.guugoo.jiapeistudent.Data.ForumComment;
import com.guugoo.jiapeistudent.Data.ReturnData;
import com.guugoo.jiapeistudent.MainActivity.BaseActivity;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.DES;
import com.guugoo.jiapeistudent.Tools.MyHandler;
import com.guugoo.jiapeistudent.Tools.MyThread;
import com.guugoo.jiapeistudent.Tools.MyToast;
import com.guugoo.jiapeistudent.Tools.Utils;
import com.guugoo.jiapeistudent.Views.TitleView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ForumCommentActivity extends BaseActivity {
    private static final String TAG ="ForumCommentActivity";
    private Forum forum;
    private int position;
    private int StudentId;
    private ListView listView;
    private List<ForumComment> listData ;
    private ForumCommentAdapter adapter;
    private TextView praise;
    private TextView[] textviews;
    private ImageView hand;
    private EditText content;
    private int number;
    private SharedPreferences sp;

    protected Handler handler1 = new MyHandler(ForumCommentActivity.this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data= JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    MyToast.makeText(ForumCommentActivity.this,data.getMessage());
                    number=number+1;
                    textviews[4].setText(String.valueOf(number));
                    ForumComment forumComment= new ForumComment(sp.getString("Nickname",""),content.getText().toString());
                    listData.add(forumComment);
                    adapter.notifyDataSetChanged();
                    Intent intent =new Intent();
                    intent.putExtra("position",position);
                    intent.putExtra("number",number);
                    setResult(RESULT_OK,intent);
                    content.setText("");
                }else {
                    MyToast.makeText(ForumCommentActivity.this,data.getMessage());
                }
            }
        }
    };


    @Override
    protected void processingData(ReturnData data) {
        Log.d(TAG, "processingData: "+data.getData());
        JSONObject comment = JSONObject.parseObject(data.getData());
        List<String> Zambia = JSONObject.parseArray(comment.getString("Zambia"),String.class);
        String string="";
        for (String s:Zambia ){
            JSONObject json = JSONObject.parseObject(s);
            string = string+json.getString("ZambiaName")+","+"\t";
        }
        praise.setText(string+"\t"+"已赞。");
        List<ForumComment> forumComments = JSONObject.parseArray(comment.getString("Comment"),ForumComment.class);
        listData.addAll(forumComments);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forum_comment);
    }

    @Override
    protected void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.forum_comment_title);
        titleView.setMiddleTextVisible(true);
        titleView.setBackImageVisible(true);
        titleView.setRightTextVisible(true);
        titleView.setMiddleText(R.string.forum_comment_hint);
        titleView.setRightText(R.string.post_message_send);
        titleView.setRightTextListenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replies();
            }
        });
        titleView.setBackImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void findView() {
        Intent intent = getIntent();
        forum = intent.getParcelableExtra("forum");
        position = intent.getIntExtra("position",0);
        StudentId = intent.getIntExtra("StudentId",0);
        number =intent.getIntExtra("Number",0);
        Log.d(TAG, "findView: "+number);
        listView = (ListView) findViewById(R.id.forum_comment_list);
        listData = new ArrayList<>();
        adapter = new ForumCommentAdapter(ForumCommentActivity.this,R.layout.adapter_forum_comment,listData);
        listView.setAdapter(adapter);
        praise = (TextView) findViewById(R.id.Zambia);
        textviews = new TextView[5];
        textviews[0] = (TextView) findViewById(R.id.forum_comment_text1);
        textviews[1] = (TextView) findViewById(R.id.forum_comment_text2);
        textviews[2] = (TextView) findViewById(R.id.forum_comment_text3);
        textviews[3] = (TextView) findViewById(R.id.forum_comment_text4);
        textviews[4] = (TextView) findViewById(R.id.forum_comment_text5);
        hand = (ImageView) findViewById(R.id.forum_comment_head);
        content = (EditText) findViewById(R.id.comment_edit);
        sp = getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Override
    protected void init() {
        getForumComment();
        textviews[0].setText(forum.getNickname());
        textviews[1].setText(forum.getCreateTime());
        textviews[2].setText(forum.getContent());
        textviews[3].setText(String.valueOf(forum.getZambiaNumber()));
        textviews[4].setText(String.valueOf(forum.getCommentNumber()));
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(ForumCommentActivity.this));
        imageLoader.getInstance().displayImage(forum.getStudentHeadPortrait(),hand);
    }


    private void getForumComment(){
        if(Utils.isNetworkAvailable(ForumCommentActivity.this)){
            JSONObject json= new JSONObject(true);
            json.put("UserId",StudentId);
            json.put("Type",0);
            json.put("DeliverId",forum.getDeliverId());
            new MyThread(Constant.URL_ForumComment, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(ForumCommentActivity.this,R.string.Toast_internet);
        }
    }

    private void replies(){
        if(Utils.isNetworkAvailable(ForumCommentActivity.this)){
            JSONObject json= new JSONObject(true);
            json.put("StudentId",StudentId);
            json.put("Type",1);
            json.put("DeliverId",forum.getDeliverId());
            json.put("TherespondentId",forum.getStudentId());
            json.put("Comment",content.getText());
            Log.d(TAG, "replies: "+json.toString());
            new MyThread(Constant.URL_Replies, handler1, DES.encryptDES(json.toString())).start();

        }else {
            MyToast.makeText(ForumCommentActivity.this,R.string.Toast_internet);
        }
    }
}
