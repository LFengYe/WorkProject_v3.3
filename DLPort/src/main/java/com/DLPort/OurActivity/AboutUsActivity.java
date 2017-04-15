package com.DLPort.OurActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.DLPort.R;
import com.DLPort.app.Constant;
import com.DLPort.myactivity.BaseActivity;
import com.DLPort.mytool.GlobalParams;
import com.DLPort.mytool.MyHandler;
import com.DLPort.mytool.MyThread;
import com.DLPort.mytool.MyToast;
import com.DLPort.myview.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fuyzh on 16/5/18.
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {
    private static final String TAG = "AboutUsActivity";

    private RelativeLayout logoEditionLayout;
    private TextView logoEditionDescribe;
    private View logoEditionImage;

    private RelativeLayout privacyLayout;
    private TextView privacyDescribe;
    private View privacyImage;

    private RelativeLayout funIntroduceLayout;
    private TextView funIntroduceDescribe;
    private View funIntroduceImage;

    private RelativeLayout handbookLayout;
    private TextView handbookDescribe;
    private View handbookImage;

    private MyHandler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    JSONObject jsonUser = null;
                    jsonUser = new JSONObject((String) msg.obj);
                    Log.d(TAG, "响应数据:" + jsonUser.toString());
                    int status = jsonUser.getInt("Status");
                    if (0 == status) {
                        JSONObject object = new JSONObject(jsonUser.getString("Data"));
                        logoEditionDescribe.setText(object.getString("LogoEdition"));
                        privacyDescribe.setText(object.getString("Privacy"));
                        funIntroduceDescribe.setText(object.getString("FunctionIntroduce"));
                        handbookDescribe.setText(object.getString("Handbook"));
                    }else {
                        MyToast.makeText(AboutUsActivity.this, jsonUser.getString("Message"));
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "异常——————————————————————————————");
                    e.printStackTrace();
                }
            }else if (msg.what == 2){
                Log.i(TAG, "响应不正常");

                MyToast.makeText(AboutUsActivity.this, "服务器异常");
            }else if (msg.what == 3) {
                // 编码异常
                Log.i(TAG, "编码异常");
            } else if (msg.what == 4) {
                // ClientProtocolException
                Log.i(TAG, "ClientProtocolException");
            } else if (msg.what == 5) {
                // IO异常
                Log.i(TAG, "IO异常");
                MyToast.makeText(AboutUsActivity.this, "服务器连接异常");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initTitle();
        initView();
        getAboutUsInfo();
    }

    private void initTitle() {
        TitleView titleView = (TitleView) findViewById(R.id.about_us_title);
        titleView.setLeftViewVisible(true);
        titleView.setMiddleTextVisible(true);
        titleView.setWineText(R.string.My);
        titleView.setMiddleText(R.string.about_us);
        View view =findViewById(R.id.title_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        logoEditionLayout = (RelativeLayout) findViewById(R.id.logo_edition_layout);
        logoEditionLayout.setOnClickListener(new View.OnClickListener() {
            boolean isExpand;
            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                logoEditionDescribe.clearAnimation();
                int durationMillis = 350;
                Animation describeAnimation;
                if (isExpand) {
                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    logoEditionImage.startAnimation(animation);

                    describeAnimation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            logoEditionDescribe.setVisibility(View.VISIBLE);
                        }
                    };
                } else {
                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    logoEditionImage.startAnimation(animation);

                    describeAnimation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            logoEditionDescribe.setVisibility(View.GONE);
                        }
                    };
                }

                describeAnimation.setDuration(durationMillis);
                logoEditionDescribe.startAnimation(describeAnimation);
            }
        });
        logoEditionDescribe = (TextView) findViewById(R.id.logo_edition_describe);
        logoEditionImage = (View) findViewById(R.id.logo_edition_image);

        privacyLayout = (RelativeLayout) findViewById(R.id.privacy_layout);
        privacyLayout.setOnClickListener(new View.OnClickListener() {
            boolean isExpand;
            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                privacyDescribe.clearAnimation();
                int durationMillis = 350;
                Animation describeAnimation;
                if (isExpand) {
                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    privacyImage.startAnimation(animation);

                    describeAnimation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            privacyDescribe.setVisibility(View.VISIBLE);
                        }
                    };
                } else {
                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    privacyImage.startAnimation(animation);

                    describeAnimation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            privacyDescribe.setVisibility(View.GONE);
                        }
                    };
                }

                describeAnimation.setDuration(durationMillis);
                privacyDescribe.startAnimation(describeAnimation);
            }
        });
        privacyDescribe = (TextView) findViewById(R.id.privacy_describe);
        privacyImage = findViewById(R.id.privacy_image);

        funIntroduceLayout = (RelativeLayout) findViewById(R.id.fun_introduce_layout);
        funIntroduceLayout.setOnClickListener(new View.OnClickListener() {
            boolean isExpand;
            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                funIntroduceDescribe.clearAnimation();
                int durationMillis = 350;
                Animation describeAnimation;
                if (isExpand) {
                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    funIntroduceImage.startAnimation(animation);

                    describeAnimation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            funIntroduceDescribe.setVisibility(View.VISIBLE);
                        }
                    };
                } else {
                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    funIntroduceImage.startAnimation(animation);

                    describeAnimation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            funIntroduceDescribe.setVisibility(View.GONE);
                        }
                    };
                }

                describeAnimation.setDuration(durationMillis);
                funIntroduceDescribe.startAnimation(describeAnimation);
            }
        });
        funIntroduceDescribe = (TextView) findViewById(R.id.fun_introduce_describe);
        funIntroduceImage = findViewById(R.id.fun_introduce_image);

        handbookLayout = (RelativeLayout) findViewById(R.id.handbook_layout);
        handbookLayout.setOnClickListener(new View.OnClickListener() {
            boolean isExpand;

            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                handbookDescribe.clearAnimation();
                int durationMillis = 350;
                Animation describeAnimation;
                if (isExpand) {
                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    handbookImage.startAnimation(animation);

                    describeAnimation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            handbookDescribe.setVisibility(View.VISIBLE);
                        }
                    };
                } else {
                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    handbookImage.startAnimation(animation);

                    describeAnimation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            handbookDescribe.setVisibility(View.GONE);
                        }
                    };
                }

                describeAnimation.setDuration(durationMillis);
                handbookDescribe.startAnimation(describeAnimation);
            }
        });
        handbookDescribe = (TextView) findViewById(R.id.handbook_describe);
        handbookImage = findViewById(R.id.handbook_image);
    }

    private void getAboutUsInfo() {
        SharedPreferences sp = null;
        Bundle bundle = getIntent().getExtras();
        int type = bundle.getInt("Type");
        if (type == 0)
            sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        if (type == 1)
            sp = getSharedPreferences("huo", Context.MODE_PRIVATE);

        if (GlobalParams.isNetworkAvailable(AboutUsActivity.this)) {
            if (null != sp) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Type", type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                new MyThread(Constant.URL_UserPostAboutUs, handler, jsonObject, sp.getString("Token", null)).start();
                new MyThread(Constant.URL_UserPostAboutUs, handler, jsonObject, AboutUsActivity.this).start();
            }
        } else {
            MyToast.makeText(AboutUsActivity.this, "亲,网络未连接");
        }
    }
}
