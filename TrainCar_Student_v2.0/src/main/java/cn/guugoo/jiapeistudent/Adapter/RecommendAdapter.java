package cn.guugoo.jiapeistudent.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.guugoo.jiapeistudent.Data.Recommend;
import cn.guugoo.jiapeistudent.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class RecommendAdapter extends BaseAdapter {
    private static final String TAG = "RecommendAdapter";

    private int resourceId;
    private Context context;
    List<Recommend> list;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public RecommendAdapter (Context context, int resource, List<Recommend> objects) {

        this.context=context;
        resourceId=resource;
        list=objects;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = View.inflate(context, resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.HeadPortrait= (ImageView) convertView.findViewById(R.id.iv_recommend);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.tv_recommend_text1);
            viewHolder.Tel = (TextView) convertView.findViewById(R.id.tv_recommend_text2);
            viewHolder.Type = (TextView) convertView.findViewById(R.id.tv_recommend_text3);
            viewHolder.RewarType = (TextView) convertView.findViewById(R.id.tv_recommend_text4);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Recommend data = list.get(position);
        if(!TextUtils.isEmpty(data.getHeadPortrait())){
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.getInstance().displayImage(data.getHeadPortrait(),viewHolder.HeadPortrait);
        }

        viewHolder.Name.setText(data.getNmae());
        viewHolder.Tel.setText(data.getTel());
        viewHolder.Type.setText(data.getType());
        switch (data.getInvitePeopleRewarType()){
            case 1:
                viewHolder.RewarType.setText(R.string.my_recommend_text3);
                break;
            case 2:
                viewHolder.RewarType.setText(R.string.my_recommend_text4);
                break;
            default:
                viewHolder.RewarType.setText("");
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView HeadPortrait;
        public TextView Name;
        public TextView Tel;
        public TextView Type;
        public TextView RewarType;
}
}
