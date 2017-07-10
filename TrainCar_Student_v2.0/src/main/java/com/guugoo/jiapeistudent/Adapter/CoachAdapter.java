package com.guugoo.jiapeistudent.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.guugoo.jiapeistudent.Data.Coach;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.Utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class CoachAdapter extends BaseAdapter{
    private static final String TAG = "CoachAdapter";
    private int resourceId;
    private Context context;
    private List<Coach> list;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public CoachAdapter(int resourceId, Context context, List<Coach> list) {
        this.resourceId = resourceId;
        this.context = context;
        this.list = list;
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
            viewHolder.HeadPortrait = (ImageView) convertView.findViewById(R.id.teacher_hand);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.RatingBarId);
            viewHolder.name = (TextView) convertView.findViewById(R.id.coach_name);
            viewHolder.Branch = (TextView) convertView.findViewById(R.id.cancel_where);
            viewHolder.Age = (TextView) convertView.findViewById(R.id.cancel_age);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Coach data = list.get(position);

        if(!TextUtils.isEmpty(data.getHeadPortrait())){
            DisplayImageOptions options= Utils.getOption(0);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.getInstance().displayImage(data.getHeadPortrait(),viewHolder.HeadPortrait,options);
        }
        viewHolder.ratingBar.setRating(data.getComprehensiveLevel());
        viewHolder.name.setText(data.getName());
        viewHolder.Branch.setText(data.getBranch());
        viewHolder.Age.setText(data.getSeniority()+"年");
        return convertView;
    }

    class ViewHolder {
        private ImageView HeadPortrait;
        private RatingBar ratingBar;
        private TextView name;
        private TextView Branch;
        private TextView Age;
    }

}
