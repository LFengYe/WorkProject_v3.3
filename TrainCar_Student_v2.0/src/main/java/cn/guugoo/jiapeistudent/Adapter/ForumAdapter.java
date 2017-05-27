package cn.guugoo.jiapeistudent.Adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import cn.guugoo.jiapeistudent.App.Constant;
import cn.guugoo.jiapeistudent.Data.Forum;
import cn.guugoo.jiapeistudent.Data.ReturnData;
import cn.guugoo.jiapeistudent.R;
import cn.guugoo.jiapeistudent.Tools.DES;
import cn.guugoo.jiapeistudent.Tools.MyHandler;
import cn.guugoo.jiapeistudent.Tools.MyThread;
import cn.guugoo.jiapeistudent.Tools.MyToast;
import cn.guugoo.jiapeistudent.Tools.Utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;


/**
 * Created by gpw on 2016/8/7.
 * --加油
 */
public class ForumAdapter extends BaseAdapter{
    private static final String TAG = "ForumAdapter";


    private int resourceId;
    private List<Forum> list;
    private Context context;
    private SharedPreferences sp;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageView zan;
    private TextView number;


    private Handler handler = new MyHandler(context){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.d(TAG, "handleMessage: "+msg.obj);
                ReturnData data=JSONObject.parseObject((String) msg.obj,ReturnData.class);
                if(data.getStatus()==0){
                    zan.setImageResource(R.mipmap.praise_on);
                    int no = Integer.valueOf(number.getText().toString())+1;
                    number.setText(String.valueOf(no));
                }
                MyToast.makeText(context,data.getMessage());
            }
        }
    };

    public ForumAdapter(List<Forum> list, Context mContext, int resource) {
        this.list = list;
        this.context = mContext;
        resourceId=resource;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,  ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.StudentHeadPortrait = (ImageView) convertView.findViewById(R.id.forum_head);
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.forum_text1);
            viewHolder.tv_createTime = (TextView) convertView.findViewById(R.id.forum_text2);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.forum_text3);
            viewHolder.tv_praiseNum = (TextView) convertView.findViewById(R.id.forum_text4);
            viewHolder.tv_commentNum = (TextView) convertView.findViewById(R.id.forum_text5);
            viewHolder.ll_praise  = (LinearLayout) convertView.findViewById(R.id.ll_praise);
            viewHolder.ll_comment = (LinearLayout) convertView.findViewById(R.id.ll_comment);
            viewHolder.iv_praise = (ImageView) convertView.findViewById(R.id.iv_praise);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Forum data = list.get(position);
        Log.d(TAG, "getView: IsZambia"+data.getIsZambia());
        if(!TextUtils.isEmpty(data.getStudentHeadPortrait())){
            DisplayImageOptions options= Utils.getOption(0);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.getInstance().displayImage(data.getStudentHeadPortrait(),viewHolder.StudentHeadPortrait,options);
        }
        viewHolder.tv_nickname.setText(data.getNickname());
        viewHolder.tv_createTime.setText(data.getCreateTime());
        viewHolder.tv_content.setText(data.getContent());
        viewHolder.tv_commentNum.setText(String.valueOf(data.getCommentNumber()));
        viewHolder.tv_praiseNum.setText(String.valueOf(data.getZambiaNumber()));
        if(data.getIsZambia()){
            viewHolder.iv_praise.setImageResource(R.mipmap.praise_on);
        }else {
            viewHolder.iv_praise.setImageResource(R.mipmap.praise_out);
        }
        viewHolder.ll_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                zan=viewHolder.iv_praise;
                number = viewHolder.tv_praiseNum;
                praise(position);
            }
        });
//        final View View = convertView;
//        viewHolder.ll_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               backInfacter.click(View,parent,position,viewHolder.ll_comment.getId());
//            }
//        });
        return convertView;
    }

//    /**
//     * 这个只是体验一下，本来是可以只用一个方法不用传参数的，就可以实现，嘻嘻搞笑的。
//     */
//    public interface ForumAdapterBack{
//        /**
//         * 点击item条目中某个控件回调的方法
//         * @param item ListView中item布局的View对象
//         * @param parent 父容器对象
//         * @param position item在ListView中所处的位置
//         * @param which item中要点击的控件的id
//         */
//        void click(View item, View parent, int position, int which);
//    }
//    public void setBack(ForumAdapterBack form){
//        backInfacter =form;
//    }


    private final class ViewHolder {
        private ImageView StudentHeadPortrait;
        private ImageView iv_praise;
        public TextView tv_nickname;
        public TextView tv_createTime;
        public TextView tv_content;
        public TextView tv_commentNum;
        public TextView tv_praiseNum;
        public LinearLayout ll_comment;
        private LinearLayout ll_praise;
    }

    private void praise(int position){
        if(Utils.isNetworkAvailable(context)) {
            sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            JSONObject json = new JSONObject(true);
            Forum data = list.get(position);
            json.put("StudentId",sp.getInt("Id",0));
            json.put("Type",1);
            json.put("DeliverId",data.getDeliverId());
            Log.d(TAG, "praise: "+json.toString());
            new MyThread(Constant.URL_ClickALike, handler, DES.encryptDES(json.toString())).start();
        }else {
            MyToast.makeText(context,R.string.Toast_internet);
        }
    }

}


