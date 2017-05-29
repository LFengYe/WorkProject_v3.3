package cn.guugoo.jiapeiteacher.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.adapter.ForumAdapter;
import cn.guugoo.jiapeiteacher.adapter.NoticeAdapter;
import cn.guugoo.jiapeiteacher.base.BaseActivity;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.bean.Forum;
import cn.guugoo.jiapeiteacher.bean.Notice;
import cn.guugoo.jiapeiteacher.bean.TotalData;
import cn.guugoo.jiapeiteacher.base.BaseAsyncTask;
import cn.guugoo.jiapeiteacher.util.DateUtils;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;
import cn.guugoo.jiapeiteacher.util.NetUtil;
import cn.guugoo.jiapeiteacher.view.XRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Created by gpw on 2016/8/3.
 * --加油
 */
public class CirclesSharingActivity extends BaseActivity {
    private ListView lv_notice;
    private XRecyclerView lv_forum;
    private ForumAdapter mForumAdapter;
    private NoticeAdapter mNoticeAdapter;
    private ArrayList<Forum> mForums;
    private ArrayList<Notice> mNotices;
    private int schoolId;
    private int teacherId;
    private String nickName;
    private final static int CommentActivity = 99;
    private int CurrentPage = 1;
    private String FirstTime;
    private String token;

    @Override
    protected int getLayout() {
        return R.layout.activity_circle_sharing;
    }

    @Override
    protected void initData() {
        mForums = new ArrayList<>();
        mNotices = new ArrayList<>();
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_NAME, MODE_PRIVATE);
        schoolId = prefs.getInt("SchoolId", 0);
        teacherId = prefs.getInt("Id", 0);
        nickName = prefs.getString("NicKname", "");
        token = prefs.getString("token", "");
//        schoolId = getIntent().getIntExtra("SchoolId", 0);
//        teacherId = getIntent().getIntExtra("teacherId", 0);
//        nickName = getIntent().getStringExtra("nickName");
    }

    @Override
    protected void initView() {

        RelativeLayout rl_head = (RelativeLayout) findViewById(R.id.head);
        TextView tv_center = (TextView) rl_head.findViewById(R.id.tv_center);
        TextView tv_right = (TextView) rl_head.findViewById(R.id.tv_right);
        ImageView iv_back = (ImageView) rl_head.findViewById(R.id.iv_back);

        lv_notice = (ListView) findViewById(R.id.lv_notice);
        lv_forum = (XRecyclerView) findViewById(R.id.lv_forum);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_forum.setLayoutManager(layoutManager);
        lv_forum.setRefreshing(true);

        lv_forum.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (!NetUtil.checkNetworkConnection(CirclesSharingActivity.this)) {
                    Toast.makeText(CirclesSharingActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
                    lv_forum.refreshComplete("fail");
                    return;
                }
                new GetRefreshForumAsyncTask(CirclesSharingActivity.this, HttpUtil.url_forum, token).execute(getJsonObject(1, FirstTime));
            }

            @Override
            public void onLoadMore() {
                if (!NetUtil.checkNetworkConnection(CirclesSharingActivity.this)) {
                    Toast.makeText(CirclesSharingActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
                    lv_forum.loadMoreComplete();
                    return;
                }
                CurrentPage++;
                new GetLoadForumAsyncTask(CirclesSharingActivity.this, HttpUtil.url_forum, token).execute(getJsonObject(CurrentPage, FirstTime));
            }
        });


        lv_notice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int noticeId = mNotices.get(position).getNoticeId();
                Intent intent = new Intent(CirclesSharingActivity.this, NoticeActivity.class);
                intent.putExtra("noticeId", noticeId);
                intent.putExtra("token", token);
                startActivity(intent);
            }

        });


        tv_center.setText(R.string.circles_sharing);
        tv_right.setText(R.string.publish);


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SchoolId", schoolId);
        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
        FirstTime = DateUtils.getCurrentDate();

        if (NetUtil.checkNetworkConnection(CirclesSharingActivity.this)) {
            new GetNoticeAsyncTask(CirclesSharingActivity.this, HttpUtil.url_notice, token).execute(jsonObject);
            new GetForumAsyncTask(CirclesSharingActivity.this, HttpUtil.url_forum, token).execute(getJsonObject(1, FirstTime));
        } else {
            Toast.makeText(CirclesSharingActivity.this, R.string.Net_error, Toast.LENGTH_SHORT).show();
        }


        iv_back.setOnClickListener(this);
        tv_right.setOnClickListener(this);

    }


    private JsonObject getJsonObject(int pageIndex, String firstTime) {
        JsonObject forum_jsonObject = new JsonObject();
        forum_jsonObject.addProperty("UserId", teacherId);
        forum_jsonObject.addProperty("PageIndex", pageIndex);
        forum_jsonObject.addProperty("PageSize", 12);
        forum_jsonObject.addProperty("FirstTime", firstTime);
        forum_jsonObject.addProperty("Type", 2);
        System.out.println(forum_jsonObject.toString());
        forum_jsonObject = EncryptUtils.encryptDES(forum_jsonObject.toString());
        return forum_jsonObject;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            setResult(RESULT_OK, getIntent());
            finish();
        } else if (v.getId() == R.id.tv_right) {
            Intent intent = new Intent(CirclesSharingActivity.this, PublishActivity.class);
            intent.putExtra("teacherId", teacherId);
            intent.putExtra("token", token);
            startActivityForResult(intent, 69);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommentActivity && resultCode == RESULT_OK) {
            Forum forum = data.getParcelableExtra("forum");
            int position = data.getIntExtra("position", 0);
            mForums.set(position, forum);
            mForumAdapter.notifyDataSetChanged();
        }
        if (requestCode == 69 && resultCode == RESULT_OK) {
            new GetRefreshForumAsyncTask(CirclesSharingActivity.this, HttpUtil.url_forum, token).execute(getJsonObject(1, FirstTime));
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, getIntent());
        super.onBackPressed();
    }

    class GetNoticeAsyncTask extends BaseAsyncTask {


        public GetNoticeAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(CirclesSharingActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Type listType = new TypeToken<ArrayList<Notice>>() {
                }.getType();
                mNotices = gson.fromJson(totalData.getData(), listType);
                mNoticeAdapter = new NoticeAdapter(mNotices, CirclesSharingActivity.this);
                lv_notice.setAdapter(mNoticeAdapter);
            } else {
                Toast.makeText(CirclesSharingActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    class GetForumAsyncTask extends BaseAsyncTask {


        public GetForumAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(CirclesSharingActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                System.out.println(totalData.getData().toString());
                if (totalData.getData().toString().equals("[]")) {
                    Toast.makeText(CirclesSharingActivity.this, "当前数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Type listType = new TypeToken<ArrayList<Forum>>() {
                }.getType();
                mForums = gson.fromJson(totalData.getData(), listType);
                FirstTime = mForums.get(mForums.size() - 1).getFirstTime();
                mForumAdapter = new ForumAdapter(mForums, CirclesSharingActivity.this);
                lv_forum.setAdapter(mForumAdapter);

                mForumAdapter.setOnItemClickListener(new ForumAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Forum forum = mForums.get(position - 1);
                        Intent intent = new Intent(CirclesSharingActivity.this, CommentActivity.class);
                        intent.putExtra("forum", forum);
                        intent.putExtra("position", position - 1);
                        startActivityForResult(intent, CommentActivity);

                    }

                    @Override
                    public void onPraiseBtnClick(View view, int position) {
                        if (!mForums.get(position - 1).getIsZambia()) {
                            mForums.get(position - 1).setIsZambia(true);
                            int num = mForums.get(position - 1).getZambiaNumber();
                            mForums.get(position - 1).setZambiaNumber(num + 1);
                            mForumAdapter.notifyDataSetChanged();
                        }
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("StudentId", teacherId);
                        jsonObject.addProperty("Type", 2);
                        jsonObject.addProperty("DeliverId", mForums.get(position - 1).getDeliverId());
                        jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                        new ClickALikeAsyncTask(CirclesSharingActivity.this, HttpUtil.url_clickALike, token).execute(jsonObject);
                    }
                });
            } else {
                Toast.makeText(CirclesSharingActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CirclesSharingActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Toast.makeText(CirclesSharingActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CirclesSharingActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    class GetRefreshForumAsyncTask extends BaseAsyncTask {


        public GetRefreshForumAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(CirclesSharingActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                lv_forum.refreshComplete("fail");
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                lv_forum.refreshComplete("success");
                Type listType = new TypeToken<ArrayList<Forum>>() {
                }.getType();
                CurrentPage = 1;
                mForums.clear();
                ArrayList<Forum> refresh_mForums = gson.fromJson(totalData.getData(), listType);
                FirstTime = refresh_mForums.get(refresh_mForums.size() - 1).getFirstTime();
                mForums.addAll(refresh_mForums);
                mForumAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(CirclesSharingActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
                lv_forum.refreshComplete("fail");
            }
        }
    }

    class GetLoadForumAsyncTask extends BaseAsyncTask {


        public GetLoadForumAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            lv_forum.loadMoreComplete();
            if (s.isEmpty()) {
                Toast.makeText(CirclesSharingActivity.this, R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Type listType = new TypeToken<ArrayList<Forum>>() {
                }.getType();
                ArrayList<Forum> Load_mForums = gson.fromJson(totalData.getData(), listType);

                if (Load_mForums.size() < 5) {
                    lv_forum.setNoMore(true);
                } else {
                    FirstTime = Load_mForums.get(Load_mForums.size() - 1).getFirstTime();
                }
                mForums.addAll(Load_mForums);
                mForumAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(CirclesSharingActivity.this, totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
