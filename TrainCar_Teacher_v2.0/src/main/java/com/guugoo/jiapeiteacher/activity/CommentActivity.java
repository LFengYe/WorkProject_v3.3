package com.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.adapter.CommentAdapter;
import com.guugoo.jiapeiteacher.base.BaseActivity;
import com.guugoo.jiapeiteacher.base.Constants;
import com.guugoo.jiapeiteacher.bean.Forum;
import com.guugoo.jiapeiteacher.bean.ForumCommentInfo;
import com.guugoo.jiapeiteacher.bean.TotalData;
import com.guugoo.jiapeiteacher.base.BaseAsyncTask;
import com.guugoo.jiapeiteacher.util.EncryptUtils;
import com.guugoo.jiapeiteacher.util.HttpUtil;
import com.guugoo.jiapeiteacher.view.CircleImageView;
import java.util.ArrayList;

/**
 * Created by gpw on 2016/8/7.
 * --加油
 */
public class CommentActivity extends BaseActivity {
    private CommentAdapter mCommentAdapter;
    private ArrayList<ForumCommentInfo.ZambiaInfo> mZambiaInfos;
    private ArrayList<ForumCommentInfo.CommentInfo> mCommentInfos;


    private ListView lv_comment;
    private TextView tv_zambia;
    private EditText et_comment;

    TextView tv_nickname;
    TextView tv_createTime;
    TextView tv_content;
    TextView tv_commentNum;
    TextView tv_praiseNum;
    LinearLayout ll_praise;
    LinearLayout ll_comment;
    ImageView iv_praise;
    CircleImageView civ_head;
    private Forum forum;
    private int teacherId;
    private int position;
    private String zambia;
    private String nickName;
    private TextView tv_send;
    private String token;
    private String therespondentId;
    private String Replyobject;
    private InputMethodManager imm;

    @Override
    protected int getLayout() {
        return R.layout.activity_comment_details;
    }

    @Override
    protected void initView() {
        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);

        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_createTime = (TextView) findViewById(R.id.tv_createTime);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_commentNum = (TextView) findViewById(R.id.tv_commentNum);
        tv_praiseNum = (TextView) findViewById(R.id.tv_praiseNum);
        ll_praise = (LinearLayout) findViewById(R.id.ll_praise);
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
        iv_praise = (ImageView) findViewById(R.id.iv_praise);
        civ_head = (CircleImageView) findViewById(R.id.civ_head);
        et_comment = (EditText) findViewById(R.id.et_comment);
        tv_send = (TextView) findViewById(R.id.tv_send);
        lv_comment = (ListView) findViewById(R.id.lv_comment);
        tv_zambia = (TextView) findViewById(R.id.tv_zambia);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        et_comment.clearFocus();
        imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);

        tv_nickname.setText(forum.getNickname());
        tv_createTime.setText(forum.getCreateTime() + "");
        tv_content.setText(forum.getContent());
        tv_commentNum.setText(forum.getCommentNumber() + "");
        tv_praiseNum.setText(forum.getZambiaNumber() + "");
        Glide.with(this)
                .load(forum.getStudentHeadPortrait())
                .crossFade()
                .into(civ_head);

        if (!forum.getIsZambia()) {
            iv_praise.setImageResource(R.mipmap.praise);
        } else {
            iv_praise.setImageResource(R.mipmap.praise_press);
        }


        tv_center.setText("评论详情");


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("DeliverId", forum.getDeliverId());
        jsonObject.addProperty("UserId", teacherId);
        jsonObject.addProperty("Type", 2);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        new GetForumCommentAsyncTask(CommentActivity.this,HttpUtil.url_forumComment,token).execute(jsonObject);


        iv_back.setOnClickListener(this);
        ll_comment.setOnClickListener(this);
        ll_praise.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        tv_center.setOnClickListener(this);
        et_comment.setOnClickListener(this);


    }

    @Override
    protected void initData() {

        forum = getIntent().getParcelableExtra("forum");
        position= getIntent().getIntExtra("position", 0);
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        teacherId = prefs.getInt("Id",0);
        nickName = prefs.getString("NicKname","");
        token = prefs.getString("token","");
        mZambiaInfos = new ArrayList<>();
        mCommentInfos = new ArrayList<>();
        zambia = "";
        imm = (InputMethodManager)CommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            getIntent().putExtra("forum", forum);
            getIntent().putExtra("position", position);
            setResult(RESULT_OK, getIntent());
            finish();
        } else if (v.getId() == R.id.ll_praise) {
            if (!forum.getIsZambia()) {
                iv_praise.setImageResource(R.mipmap.praise_press);
                forum.setIsZambia(true);
                int num = forum.getZambiaNumber()+1;
                forum.setZambiaNumber(num);
                tv_praiseNum.setText(String.valueOf(num));
                if (zambia.isEmpty()){
                    tv_zambia.setText(nickName+" 已赞.");
                }
                else {
                    StringBuilder sb = new StringBuilder(zambia);
                    int before = zambia.indexOf("已赞.");
                    sb.replace(before, before + 3, nickName+" 已赞.");
                    tv_zambia.setText(sb.toString());
                }

            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("StudentId", teacherId);
            jsonObject.addProperty("Type", 2);
            jsonObject.addProperty("DeliverId", forum.getDeliverId());
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new ClickALikeAsyncTask(CommentActivity.this,HttpUtil.url_clickALike,token).execute(jsonObject);


        }else if (v.getId() == R.id.tv_send) {
            String comment_content = et_comment.getText().toString();
            if (comment_content.isEmpty()||comment_content.equals(" ")){
                Toast.makeText(CommentActivity.this, "评论的信息不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("DeliverId", forum.getDeliverId());
            jsonObject.addProperty("StudentId", teacherId);
            jsonObject.addProperty("Type", 2);
            jsonObject.addProperty("TherespondentId", therespondentId);
            jsonObject.addProperty("Comment", et_comment.getText().toString());
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new RepliesAsyncTask(CommentActivity.this,HttpUtil.url_replies,token).execute(jsonObject);
        }else if (v.getId() == R.id.et_comment) {

            therespondentId = forum.getStudentId();
            Replyobject = forum.getNickname();
            et_comment.setFocusable(true);
            et_comment.setHint("评论");
        }



    }


    @Override
    public void onBackPressed() {
        getIntent().putExtra("forum", forum);
        getIntent().putExtra("position", position);
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }

    class GetForumCommentAsyncTask extends  BaseAsyncTask {


        public GetForumCommentAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(CommentActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                ForumCommentInfo forumComment = gson.fromJson(totalData.getData(), ForumCommentInfo.class);
                System.out.println(forumComment.toString());
                mZambiaInfos = forumComment.getZambia();
                mCommentInfos = forumComment.getComment();
                System.out.println(mZambiaInfos.size());
                System.out.println(mCommentInfos.size());

                mCommentAdapter = new CommentAdapter(mCommentInfos, CommentActivity.this,nickName);
                lv_comment.setAdapter(mCommentAdapter);
                lv_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ForumCommentInfo.CommentInfo commentInfo = mCommentInfos.get(position);
                        therespondentId = commentInfo.getTheReviewersId();
                        Replyobject  = commentInfo.getTheReviewers();
                        //view.setBackgroundColor(ContextCompat.getColor(CommentActivity.this, R.color.color_line));
                        et_comment.setHint("回复"+Replyobject);
                        et_comment.setFocusable(true);

                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                });
                if (mZambiaInfos.size()>0) {
                    for (int j = 0; j < mZambiaInfos.size(); j++) {
                        if (j == mZambiaInfos.size() - 1) {
                            zambia = zambia + mZambiaInfos.get(j).getZambiaName() + "  已赞.";
                        }else
                        {
                            zambia = zambia + mZambiaInfos.get(j).getZambiaName() + ", ";
                        }
                    }
                }

                tv_zambia.setText(zambia);

            } else {
                Toast.makeText(CommentActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class ClickALikeAsyncTask extends BaseAsyncTask {


        public ClickALikeAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }



        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(CommentActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Toast.makeText(CommentActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CommentActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    class RepliesAsyncTask extends BaseAsyncTask {


        public RepliesAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(CommentActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                ForumCommentInfo.CommentInfo commentInfo = new ForumCommentInfo.CommentInfo();
                commentInfo.setContent(et_comment.getText().toString());
                commentInfo.setReplyobject(Replyobject);
                commentInfo.setReplyobjectId(therespondentId);
                commentInfo.setTheReviewers(nickName);
                commentInfo.setTheReviewersId(String.valueOf(teacherId));
                mCommentInfos.add(commentInfo);
                mCommentAdapter.notifyDataSetChanged();
                int num = forum.getCommentNumber()+1;
                forum.setCommentNumber(num);
                et_comment.setText("");
                tv_commentNum.setText(String.valueOf(num));
                Toast.makeText(CommentActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CommentActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }



}
