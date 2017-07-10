package com.guugoo.jiapeistudent.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guugoo.jiapeistudent.Data.Site;
import com.guugoo.jiapeistudent.R;
import com.guugoo.jiapeistudent.Tools.Utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class SiteAdapter extends BaseAdapter{
    private static final String TAG = "SiteAdapter";
    private int resourceId;
    private Context context;
    List<Site> list;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public SiteAdapter (Context context, int resource, List<Site> objects) {

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
            viewHolder.BranchImg= (ImageView) convertView.findViewById(R.id.site_hand);
            viewHolder.BranchSchoolName = (TextView) convertView.findViewById(R.id.site_name);
            viewHolder.Distance = (TextView) convertView.findViewById(R.id.site_where);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Site data = list.get(position);
        if(!TextUtils.isEmpty(data.getBranchImg())){
            DisplayImageOptions options= Utils.getOption(1);
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            imageLoader.getInstance().displayImage(data.getBranchImg(),viewHolder.BranchImg,options);
        }
        viewHolder.BranchSchoolName.setText(data.getBranchSchoolName());
        viewHolder.Distance.setText(
                String.format(context.getString(R.string.site_distance),data.getDistance()));

        return convertView;
    }
    class ViewHolder {
        public ImageView BranchImg;
        public TextView BranchSchoolName;
        public TextView Distance;
    }

}
