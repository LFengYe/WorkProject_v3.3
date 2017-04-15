package cn.com.caronwer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.com.caronwer.R;
import cn.com.caronwer.bean.QiangOrderInfo;

public class QiangOrderAdapter extends RecyclerView.Adapter<QiangOrderAdapter.ViewHolder> {

    private ArrayList<QiangOrderInfo> newsInfos;
    private List<QiangOrderInfo.AddressListBean> mAddressList;
    private QiangOrderInfo.OrdersdetailsBean mOrdersdetails;


    public QiangOrderAdapter(ArrayList<QiangOrderInfo> newsInfos) {
        super();
        this.newsInfos = newsInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_guanzhu, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final QiangOrderInfo newsInfo = newsInfos.get(position);

        mAddressList = newsInfo.getAddressList();
        mOrdersdetails = newsInfo.getOrdersdetails();

        if (mAddressList.size() > 0) {
            viewHolder.mTvCong.setText(mAddressList.get(0).getReceiptAddress());
            switch (mAddressList.size()) {
                case 2:
                    viewHolder.mLl_jing.setVisibility(View.GONE);
                    viewHolder.view_visib.setVisibility(View.GONE);
                    viewHolder.mLl_jing2.setVisibility(View.GONE);
                    viewHolder.mView_visib2.setVisibility(View.GONE);
                    viewHolder.mLl_jing3.setVisibility(View.GONE);
                    viewHolder.mView_visib3.setVisibility(View.GONE);
                    viewHolder.mLl_jing4.setVisibility(View.GONE);
                    viewHolder.mView_visib4.setVisibility(View.GONE);
                    viewHolder.mLl_jing5.setVisibility(View.GONE);
                    viewHolder.mView_visib5.setVisibility(View.GONE);
                    break;
                case 3:
                    viewHolder.mLl_jing.setVisibility(View.VISIBLE);
                    viewHolder.view_visib.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing.setText(mAddressList.get(1).getReceiptAddress());
                    viewHolder.mLl_jing2.setVisibility(View.GONE);
                    viewHolder.mView_visib2.setVisibility(View.GONE);
                    viewHolder.mLl_jing3.setVisibility(View.GONE);
                    viewHolder.mView_visib3.setVisibility(View.GONE);
                    viewHolder.mLl_jing4.setVisibility(View.GONE);
                    viewHolder.mView_visib4.setVisibility(View.GONE);
                    viewHolder.mLl_jing5.setVisibility(View.GONE);
                    viewHolder.mView_visib5.setVisibility(View.GONE);
                    break;
                case 4:
                    viewHolder.mLl_jing.setVisibility(View.VISIBLE);
                    viewHolder.view_visib.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing.setText(mAddressList.get(1).getReceiptAddress());
                    viewHolder.mLl_jing2.setVisibility(View.VISIBLE);
                    viewHolder.mView_visib2.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing2.setText(mAddressList.get(2).getReceiptAddress());
                    viewHolder.mLl_jing3.setVisibility(View.GONE);
                    viewHolder.mView_visib3.setVisibility(View.GONE);
                    viewHolder.mLl_jing4.setVisibility(View.GONE);
                    viewHolder.mView_visib4.setVisibility(View.GONE);
                    viewHolder.mLl_jing5.setVisibility(View.GONE);
                    viewHolder.mView_visib5.setVisibility(View.GONE);
                    break;
                case 5:
                    viewHolder.mLl_jing.setVisibility(View.VISIBLE);
                    viewHolder.view_visib.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing.setText(mAddressList.get(1).getReceiptAddress());
                    viewHolder.mLl_jing2.setVisibility(View.VISIBLE);
                    viewHolder.mView_visib2.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing2.setText(mAddressList.get(2).getReceiptAddress());
                    viewHolder.mLl_jing3.setVisibility(View.VISIBLE);
                    viewHolder.mView_visib3.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing3.setText(mAddressList.get(3).getReceiptAddress());
                    viewHolder.mLl_jing4.setVisibility(View.GONE);
                    viewHolder.mView_visib4.setVisibility(View.GONE);
                    viewHolder.mLl_jing5.setVisibility(View.GONE);
                    viewHolder.mView_visib5.setVisibility(View.GONE);
                    break;
                case 6:
                    viewHolder.mLl_jing.setVisibility(View.VISIBLE);
                    viewHolder.view_visib.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing.setText(mAddressList.get(1).getReceiptAddress());
                    viewHolder.mLl_jing2.setVisibility(View.VISIBLE);
                    viewHolder.mView_visib2.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing2.setText(mAddressList.get(2).getReceiptAddress());
                    viewHolder.mLl_jing3.setVisibility(View.VISIBLE);
                    viewHolder.mView_visib3.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing3.setText(mAddressList.get(3).getReceiptAddress());
                    viewHolder.mLl_jing4.setVisibility(View.VISIBLE);
                    viewHolder.mView_visib4.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing4.setText(mAddressList.get(4).getReceiptAddress());
                    viewHolder.mLl_jing5.setVisibility(View.GONE);
                    viewHolder.mView_visib5.setVisibility(View.GONE);
                    break;
                case 7:
                    viewHolder.mLl_jing.setVisibility(View.VISIBLE);
                    viewHolder.view_visib.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing.setText(mAddressList.get(1).getReceiptAddress());
                    viewHolder.mLl_jing2.setVisibility(View.VISIBLE);
                    viewHolder.mView_visib2.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing2.setText(mAddressList.get(2).getReceiptAddress());
                    viewHolder.mLl_jing3.setVisibility(View.VISIBLE);
                    viewHolder.mView_visib3.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing3.setText(mAddressList.get(3).getReceiptAddress());
                    viewHolder.mLl_jing4.setVisibility(View.VISIBLE);
                    viewHolder.mView_visib4.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing4.setText(mAddressList.get(4).getReceiptAddress());
                    viewHolder.mLl_jing5.setVisibility(View.VISIBLE);
                    viewHolder.mView_visib5.setVisibility(View.VISIBLE);
                    viewHolder.mTvJing5.setText(mAddressList.get(5).getReceiptAddress());
                    break;
                default:
                    break;
            }
            viewHolder.mTvDao.setText(mAddressList.get(mAddressList.size() - 1).getReceiptAddress());
        } else {
            viewHolder.mLl_jing.setVisibility(View.GONE);
            viewHolder.view_visib.setVisibility(View.GONE);
            viewHolder.mLl_jing2.setVisibility(View.GONE);
            viewHolder.mView_visib2.setVisibility(View.GONE);
            viewHolder.mLl_jing3.setVisibility(View.GONE);
            viewHolder.mView_visib3.setVisibility(View.GONE);
            viewHolder.mLl_jing4.setVisibility(View.GONE);
            viewHolder.mView_visib4.setVisibility(View.GONE);
            viewHolder.mLl_jing5.setVisibility(View.GONE);
            viewHolder.mView_visib5.setVisibility(View.GONE);
            viewHolder.mTvCong.setText("");
            viewHolder.mTvDao.setText("");
        }


        viewHolder.mTvCong.setText(mAddressList.get(0).getReceiptAddress());
        viewHolder.mTvJing.setText(mAddressList.get(1).getReceiptAddress());
        viewHolder.mTvDao.setText(mAddressList.get(mAddressList.size()-1).getReceiptAddress());


        viewHolder.mTvJuli.setText("约"+getTwodouble(Double.parseDouble(mOrdersdetails.getStartToEndDistance()))+"Km");
        viewHolder.mTvKehu.setText("距您"+getTwodouble2(mOrdersdetails.getVehToStartDistance())+"Km");
        viewHolder.mTvChe.setText(mOrdersdetails.getVehicleTypeName());
        viewHolder.mTvDate.setText(mOrdersdetails.getPlanSendTime().replace("T"," "));
        viewHolder.mTv_yu.setText("预");
        selectOrderStaus(viewHolder);
        selectOrderType(viewHolder);


        viewHolder.mTvQiangdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView button = (TextView) view;
                String text = button.getText().toString();
                mOnBtnClickListener.onBtnClick(newsInfo.getOrdersdetails().getPlanSendTime(),newsInfo.getOrdersdetails().getOrderNo(),text);
            }
        });

        viewHolder.mTvJiage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView button = (TextView) view;
                String text = button.getText().toString();
                mOnBtnClickListener.onBtnClick(newsInfo.getOrdersdetails().getPlanSendTime(),newsInfo.getOrdersdetails().getOrderNo(),text);
            }
        });


    }



    //获取数据的数量
    @Override
    public int getItemCount() {
        return newsInfos.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvDate;
        TextView mTvChe;
        TextView mTvJuli;
        ImageView mIvCong;
        TextView mTvCong;
        LinearLayout mLlCong;
        ImageView mIvJing;

        TextView mTvJing;
        TextView mTvJing2;
        TextView mTvJing3;
        TextView mTvJing4;
        TextView mTvJing5;
        LinearLayout mLl_jing;
        LinearLayout mLl_jing2;
        LinearLayout mLl_jing3;
        LinearLayout mLl_jing4;
        LinearLayout mLl_jing5;
        View view_visib;
        View mView_visib2;
        View mView_visib3;
        View mView_visib4;
        View mView_visib5;

        LinearLayout mLlJing;
        ImageView mIvDao;
        TextView mTvDao;
        LinearLayout mLlDao;
        TextView mTvJiage;
        TextView mTvQiangdan;
        TextView mTvKehu;
        TextView mTc_ge;

        ImageView mIv_cong;
        ImageView mIv_jing;
        ImageView mIv_dao;

        private final TextView mTv_yu;


        public ViewHolder(View view) {
            super(view);
            mTvQiangdan = (TextView) view.findViewById(R.id.tv_qiangdan);
            mTvDate = (TextView) view.findViewById(R.id.tv_date);
            mTvChe = (TextView) view.findViewById(R.id.tv_che);
            mTvJuli = (TextView) view.findViewById(R.id.tv_juli);


            mTvJing = (TextView) view.findViewById(R.id.tv_jing);
            mTvJing2 = (TextView) view.findViewById(R.id.tv_jing2);
            mTvJing3 = (TextView) view.findViewById(R.id.tv_jing3);
            mTvJing4 = (TextView) view.findViewById(R.id.tv_jing4);
            mTvJing5 = (TextView) view.findViewById(R.id.tv_jing5);

            mLl_jing = (LinearLayout) view.findViewById(R.id.ll_jing);
            mLl_jing2 = (LinearLayout) view.findViewById(R.id.ll_jing2);
            mLl_jing3 = (LinearLayout) view.findViewById(R.id.ll_jing3);
            mLl_jing4 = (LinearLayout) view.findViewById(R.id.ll_jing4);
            mLl_jing5 = (LinearLayout) view.findViewById(R.id.ll_jing5);

            view_visib = view.findViewById(R.id.view_visib);
            mView_visib2 = view.findViewById(R.id.view_visib2);
            mView_visib3 = view.findViewById(R.id.view_visib3);
            mView_visib4 = view.findViewById(R.id.view_visib4);
            mView_visib5 = view.findViewById(R.id.view_visib5);

            mTvCong = (TextView) view.findViewById(R.id.tv_cong);
            mTvDao = (TextView) view.findViewById(R.id.tv_dao);
            mTvJiage = (TextView) view.findViewById(R.id.tv_jiage);
            mTvKehu = (TextView) view.findViewById(R.id.tv_kehu);
            mIvCong = (ImageView) view.findViewById(R.id.iv_cong);
            mIvJing = (ImageView) view.findViewById(R.id.iv_jing);
            mIvDao = (ImageView) view.findViewById(R.id.iv_dao);
            mLlCong = (LinearLayout) view.findViewById(R.id.ll_cong);
            mLlJing = (LinearLayout) view.findViewById(R.id.ll_jing);
            mLlDao = (LinearLayout) view.findViewById(R.id.ll_dao);
            mTc_ge = (TextView) view.findViewById(R.id.tc_ge);
            mTv_yu = (TextView) view.findViewById(R.id.tv_yu);
            mIv_cong = (ImageView) view.findViewById(R.id.iv_cong);
            mIv_jing = (ImageView) view.findViewById(R.id.iv_jing);
            mIv_dao = (ImageView) view.findViewById(R.id.iv_dao);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnBtnClickListener {
        void onBtnClick(String time, String no, String viewName);
    }
    private OnBtnClickListener mOnBtnClickListener;
    public void setOnBtnClickListener(OnBtnClickListener mOnBtnClickListener) {
        this.mOnBtnClickListener = mOnBtnClickListener;
    }


    public double getTwodouble(Double dd){
        BigDecimal bd   =   new   BigDecimal(dd);
        bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public double getTwodouble2(Double dd){
        BigDecimal bd   =   new   BigDecimal(dd);
        bd   =   bd.setScale(1,BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }


    public double getXdouble(Double dd){
        Double ddd = dd;
        BigDecimal bd   =   new   BigDecimal(dd);
        if (dd<10){
            bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);
            ddd = bd.doubleValue();
        }else if (dd<100){
            bd   =   bd.setScale(1,BigDecimal.ROUND_HALF_UP);
            ddd = bd.doubleValue();
        }
        return ddd;
    }

    private void selectOrderStaus(ViewHolder viewHolder) {//1:已发布、2:进行中、3：已送达，4：已完成、-1：已取消
        if (mOrdersdetails.getOrderStatus() == 3||mOrdersdetails.getOrderStatus()==4||mOrdersdetails.getOrderStatus()==-1){
            viewHolder.mTvQiangdan.setBackgroundResource(R.color.bg_texton);
            viewHolder.mTvQiangdan.setClickable(false);//设置不能点击

            viewHolder.mTvDate.setTextColor(0xff96a19b);
            viewHolder.mTvKehu.setTextColor(0xff96a19b);
            viewHolder.mTvJiage.setTextColor(0xff96a19b);
            viewHolder.mTvJuli.setTextColor(0xff96a19b);
            viewHolder.mTvChe.setTextColor(0xff96a19b);

            viewHolder.mTvCong.setTextColor(0xff96a19b);
            viewHolder.mTvJing.setTextColor(0xff96a19b);
            viewHolder.mTvDao.setTextColor(0xff96a19b);

            viewHolder.mIv_cong.setImageResource(R.mipmap.start);
            viewHolder.mIv_jing.setImageResource(R.mipmap.pass);
            viewHolder.mIv_dao.setImageResource(R.mipmap.arrive);
        }else {
            viewHolder.mTvQiangdan.setClickable(true);//设置不能点击
        }
    }
    public void selectOrderType(ViewHolder viewHolder){
        switch (mOrdersdetails.getOrderType()){
            case 1:         //订单类型，1:抢单，2:预约，3:议价
                viewHolder.mTvJiage.setText("¥"+mOrdersdetails.getFreight());
                viewHolder.mTvJiage.setClickable(false);//设置不能点击
                viewHolder.mTvQiangdan.setText("抢单承运");
                break;
            case 2:
                viewHolder.mTvJiage.setText("¥"+mOrdersdetails.getFreight());//????
                viewHolder.mTvJiage.setClickable(false);//设置不能点击
                viewHolder.mTvQiangdan.setText("预约抢单");
                break;
            case 3:
                viewHolder.mTvJiage.setText("货主询价");
                viewHolder.mTvJiage.setClickable(true);//设置不能点击
                viewHolder.mTvQiangdan.setText("报价抢单");
                break;
            default:
                break;

        }
    }

}
