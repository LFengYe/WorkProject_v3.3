package cn.guugoo.jiapeiteacher.fragment;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Set;

import cn.guugoo.jiapeiteacher.R;
import cn.guugoo.jiapeiteacher.activity.LoginActivity;
import cn.guugoo.jiapeiteacher.activity.MyEvaluationActivity;
import cn.guugoo.jiapeiteacher.activity.MyScoreActivity;
import cn.guugoo.jiapeiteacher.base.Constants;
import cn.guugoo.jiapeiteacher.bean.OwnInfo;
import cn.guugoo.jiapeiteacher.bean.TotalData;
import cn.guugoo.jiapeiteacher.base.BaseAsyncTask;
import cn.guugoo.jiapeiteacher.util.EncryptUtils;
import cn.guugoo.jiapeiteacher.util.HttpUtil;
import cn.guugoo.jiapeiteacher.util.NetUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class PersonFragment extends Fragment implements View.OnClickListener {

    private TextView tv_teaching_hours;
    private TextView tv_my_invitation;
    private TextView tv_contact_client;
    private int teacherId;
    private OwnInfo ownInfo;
    private String token;
    private String tel;
    private SharedPreferences prefs;



    public PersonFragment() {
        super();
    }

    public static PersonFragment newInstance(int teacherId,String tel) {
        PersonFragment personFragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putInt("teacherId", teacherId);
        args.putString("tel", tel);

        personFragment.setArguments(args);

        return personFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            teacherId = args.getInt("teacherId");
            tel = args.getString("tel");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        ownInfo = new OwnInfo();
        prefs = getActivity().getSharedPreferences(Constants.SHARED_NAME, Context.MODE_PRIVATE);
        token = prefs.getString("token","");
        System.out.println("token"+token);

    }

    private void initView(View view) {
        RelativeLayout rl_exit_login = (RelativeLayout) view.findViewById(R.id.rl_exit_login);
        RelativeLayout rl_my_evaluation = (RelativeLayout) view.findViewById(R.id.rl_my_evaluation);
        RelativeLayout rl_my_score = (RelativeLayout) view.findViewById(R.id.rl_my_score);
        tv_teaching_hours = (TextView) view.findViewById(R.id.tv_teaching_hours);
        tv_my_invitation = (TextView) view.findViewById(R.id.tv_my_invitation);
        tv_contact_client = (TextView) view.findViewById(R.id.tv_contact_client);


        rl_exit_login.setOnClickListener(this);
        rl_my_evaluation.setOnClickListener(this);
        rl_my_score.setOnClickListener(this);

        getData();

    }

    private void getData() {

        if (!NetUtil.checkNetworkConnection(getActivity())) {
            Toast.makeText(getActivity(), R.string.Net_error, Toast.LENGTH_SHORT).show();
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("TeacherId", teacherId);
            System.out.println(jsonObject.toString());
            jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
            new GetOwnInfoAsyncTask(getActivity(),HttpUtil.url_teacherMessage,token).execute(jsonObject);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.rl_exit_login:
                intent.setClass(getActivity(), LoginActivity.class);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("autoState", false);
                editor.apply();  //存入
                JPushInterface.setAlias(getActivity(), //上下文对象
                        "", //别名
                        new TagAliasCallback() {//回调接口,i=0表示成功,其它设置失败
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                                if(i==0){
                                    System.out.println("绑定成功");}
                            }
                        });
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Tel", tel);
                jsonObject.addProperty("UseType", 2);
                System.out.println(jsonObject.toString());
                jsonObject = EncryptUtils.encryptDES(jsonObject.toString());
                new ExitLoginAsyncTask(getActivity(),HttpUtil.url_exitLogin,token).execute(jsonObject);
                getActivity().finish();
                break;
            case R.id.rl_my_evaluation:
                intent.setClass(getActivity(), MyEvaluationActivity.class);
                break;
            case R.id.rl_my_score:
                intent.setClass(getActivity(), MyScoreActivity.class);
                break;

        }
        startActivity(intent);

    }


    class GetOwnInfoAsyncTask extends BaseAsyncTask {

        public GetOwnInfoAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(getActivity(), R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                ownInfo = gson.fromJson(totalData.getData(), OwnInfo.class);
                tv_teaching_hours.setText(ownInfo.getTeachingSum());
                tv_my_invitation.setText(ownInfo.getInvitationCode());
                tv_contact_client.setText(ownInfo.getCustomerService());

            } else {
                Toast.makeText(getActivity(), totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    class ExitLoginAsyncTask extends BaseAsyncTask {

        public ExitLoginAsyncTask(Context mContext, String url, String token) {
            super(mContext, url, token);
        }

        @Override
        protected void dealResults(String s) {
            if (s.isEmpty()) {
                Toast.makeText(getActivity(), R.string.servlet_error, Toast.LENGTH_SHORT).show();
                return;
            }
            System.out.println(s);
            Gson gson = new Gson();
            TotalData totalData = gson.fromJson(s, TotalData.class);
            if (totalData.getStatus() == 0) {
                Toast.makeText(getActivity(), totalData.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
