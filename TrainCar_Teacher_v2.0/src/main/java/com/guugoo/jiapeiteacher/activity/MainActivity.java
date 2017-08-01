package com.guugoo.jiapeiteacher.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Set;

import com.guugoo.jiapeiteacher.R;
import com.guugoo.jiapeiteacher.base.Constants;
import com.guugoo.jiapeiteacher.base.MyApplication;
import com.guugoo.jiapeiteacher.bean.LoginInfo;
import com.guugoo.jiapeiteacher.fragment.HomeFragment;
import com.guugoo.jiapeiteacher.fragment.PersonFragment;
import com.guugoo.jiapeiteacher.receiver.MyReceiver;
import com.guugoo.jiapeiteacher.util.BitmapUtil;
import com.guugoo.jiapeiteacher.view.CircleImageView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView iv_home;
    private ImageView iv_person;
    private HomeFragment mHomeFragment;
    private PersonFragment mPersonFragment;
    private CircleImageView civ_head;
    private TextView tv_nickName;
    private LoginInfo loginInfo;
    private static final int PersonCenter = 1;
    private static final String SAVE_SELECT = "SAVE_SELECT";
    private static final String START_STATE = "START_STATE";
    private int selectDex = 0;
    private FragmentManager fManager;
    private int startState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getInstance().addActivity(this);
        initData();
        initView(savedInstanceState);
        Constants.MainActivityState = 1;
    }

    private void initData() {
        loginInfo = getIntent().getParcelableExtra("loginInfo");
        startState = getIntent().getIntExtra("startState",0);
    }

    private void initView(Bundle savedInstanceState) {
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_person = (ImageView) findViewById(R.id.iv_person);
        civ_head = (CircleImageView) findViewById(R.id.civ_head);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);


        iv_home.setOnClickListener(this);
        iv_person.setOnClickListener(this);
        civ_head.setOnClickListener(this);


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
        } else {
            Glide.with(this)
                    .load(loginInfo.getIcon())
                    .crossFade()
                    .skipMemoryCache(false)
                    .error(R.mipmap.icon_head)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(civ_head);
        }


        tv_nickName.setText(loginInfo.getName());

        fManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            //读取上一次界面Save的时候tab选中的状态
            selectDex = savedInstanceState.getInt(SAVE_SELECT, selectDex);

            startState = savedInstanceState.getInt(START_STATE, 0);

            mHomeFragment = (HomeFragment) fManager.findFragmentByTag("HomeTag");
            mPersonFragment = (PersonFragment) fManager.findFragmentByTag("PersonTag");
        }

        setChoiceItem(selectDex);

    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(SAVE_SELECT, selectDex);
        outState.putInt(START_STATE, 0);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_home:
                setChoiceItem(0);
                break;
            case R.id.iv_person:
                setChoiceItem(1);
                break;
            case R.id.civ_head:
                Intent intent = new Intent(MainActivity.this, PersonCenterActivity.class);
                startActivityForResult(intent, PersonCenter);
                break;
            default:
                break;
        }

    }

    private long exitTime = 0;
    private Toast toast;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                toast = Toast.makeText(getApplicationContext(), "再按一次退出",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(false);
                finish();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Glide.with(this)
                            .load(loginInfo.getIcon())
                            .crossFade()
                            .error(R.mipmap.icon_head)
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(civ_head);
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    public void setChoiceItem(int choiceItem) {
        FragmentTransaction transaction = fManager.beginTransaction();
        iv_home.setImageResource(R.mipmap.home_normal);
        iv_person.setImageResource(R.mipmap.account_normal);
        hideFragments(transaction);
        switch (choiceItem) {
            case 0:
                selectDex = 0;
                iv_home.setImageResource(R.mipmap.home);
                if (mHomeFragment == null) {
                    // 如果fg1为空，则创建一个并添加到界面上
                    mHomeFragment = HomeFragment.newInstance(startState, loginInfo);
                    transaction.add(R.id.fl_main, mHomeFragment, "HomeTag");
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mHomeFragment);
                    mHomeFragment.setBallState(Constants.ballState);
                }
                break;

            case 1:
                selectDex = 1;
                iv_person.setImageResource(R.mipmap.account);

                if (mPersonFragment == null) {
                    mPersonFragment = PersonFragment.newInstance(loginInfo.getId(),loginInfo.getTel());
                    transaction.add(R.id.fl_main, mPersonFragment, "PersonTag");
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mPersonFragment);

                }
                break;
        }
        transaction.commit();

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mPersonFragment != null) {
            transaction.hide(mPersonFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PersonCenter && resultCode == RESULT_OK) {
            int backSate = data.getIntExtra("backState",0);
            if (backSate==2){
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }else {
                String HeadPortrait = data.getStringExtra("HeadPortrait");
                if (!HeadPortrait.isEmpty()) {
                    Bitmap photo = BitmapUtil.getimg(HeadPortrait);
                    civ_head.setImageBitmap(photo);
                }
                String NewnNickme = data.getStringExtra("NewnNickme");
                tv_nickName.setText(NewnNickme);
            }
            mHomeFragment.setBallState(Constants.ballState);
        }

    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
        Constants.MainActivityState = 0;
    }
}
