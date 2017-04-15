package cn.com.caronwer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.caronwer.R;
import cn.com.caronwer.bean.MeAllOrderInfo;
import cn.com.caronwer.bean.OrderAddressBean;
import cn.com.caronwer.view.BorderTextView;

public class MeAllOrderInfoAdapter extends RecyclerView.Adapter<MeAllOrderInfoAdapter.ViewHolder> {

    private ArrayList<MeAllOrderInfo> newsInfos;
    private int type;


    public MeAllOrderInfoAdapter(ArrayList<MeAllOrderInfo> newsInfos, int type) {
        super();
        this.newsInfos = newsInfos;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dingdan, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final MeAllOrderInfo newsInfo = newsInfos.get(position);
        if (newsInfos != null) {
            List<OrderAddressBean> orderAddress = newsInfo.getOrderAddress();

            viewHolder.mTvDingdanhao.setText(newsInfo.getOrderNo());
            viewHolder.mTvDate.setText(newsInfo.getPlanSendTime());
            viewHolder.mTvJiage.setText("¥" + newsInfo.getFreight());
            viewHolder.mTvKehu.setText(newsInfo.getIsFamiliar().equals("true") ? "熟客" : "生客");
            if (orderAddress.size() > 0) {
                viewHolder.mTvCong.setText(orderAddress.get(0).getReceiptAddress());
                switch (orderAddress.size()) {
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
                        viewHolder.mTvJing.setText(orderAddress.get(1).getReceiptAddress());
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
                        viewHolder.mTvJing.setText(orderAddress.get(1).getReceiptAddress());
                        viewHolder.mLl_jing2.setVisibility(View.VISIBLE);
                        viewHolder.mView_visib2.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing2.setText(orderAddress.get(2).getReceiptAddress());
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
                        viewHolder.mTvJing.setText(orderAddress.get(1).getReceiptAddress());
                        viewHolder.mLl_jing2.setVisibility(View.VISIBLE);
                        viewHolder.mView_visib2.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing2.setText(orderAddress.get(2).getReceiptAddress());
                        viewHolder.mLl_jing3.setVisibility(View.VISIBLE);
                        viewHolder.mView_visib3.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing3.setText(orderAddress.get(3).getReceiptAddress());
                        viewHolder.mLl_jing4.setVisibility(View.GONE);
                        viewHolder.mView_visib4.setVisibility(View.GONE);
                        viewHolder.mLl_jing5.setVisibility(View.GONE);
                        viewHolder.mView_visib5.setVisibility(View.GONE);
                        break;
                    case 6:
                        viewHolder.mLl_jing.setVisibility(View.VISIBLE);
                        viewHolder.view_visib.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing.setText(orderAddress.get(1).getReceiptAddress());
                        viewHolder.mLl_jing2.setVisibility(View.VISIBLE);
                        viewHolder.mView_visib2.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing2.setText(orderAddress.get(2).getReceiptAddress());
                        viewHolder.mLl_jing3.setVisibility(View.VISIBLE);
                        viewHolder.mView_visib3.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing3.setText(orderAddress.get(3).getReceiptAddress());
                        viewHolder.mLl_jing4.setVisibility(View.VISIBLE);
                        viewHolder.mView_visib4.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing4.setText(orderAddress.get(4).getReceiptAddress());
                        viewHolder.mLl_jing5.setVisibility(View.GONE);
                        viewHolder.mView_visib5.setVisibility(View.GONE);
                        break;
                    case 7:
                        viewHolder.mLl_jing.setVisibility(View.VISIBLE);
                        viewHolder.view_visib.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing.setText(orderAddress.get(1).getReceiptAddress());
                        viewHolder.mLl_jing2.setVisibility(View.VISIBLE);
                        viewHolder.mView_visib2.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing2.setText(orderAddress.get(2).getReceiptAddress());
                        viewHolder.mLl_jing3.setVisibility(View.VISIBLE);
                        viewHolder.mView_visib3.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing3.setText(orderAddress.get(3).getReceiptAddress());
                        viewHolder.mLl_jing4.setVisibility(View.VISIBLE);
                        viewHolder.mView_visib4.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing4.setText(orderAddress.get(4).getReceiptAddress());
                        viewHolder.mLl_jing5.setVisibility(View.VISIBLE);
                        viewHolder.mView_visib5.setVisibility(View.VISIBLE);
                        viewHolder.mTvJing5.setText(orderAddress.get(5).getReceiptAddress());
                        break;
                    default:
                        break;
                }
                viewHolder.mTvDao.setText(orderAddress.get(orderAddress.size() - 1).getReceiptAddress());
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

            switch (newsInfo.getOrderStatus()) {//判断订单状态
                case 1://已接单
                    viewHolder.mTvZhuangtai.setText("已接单");
                    viewHolder.mBtvChakan.setText("查看详情");
                    viewHolder.mBtvChakan.setVisibility(View.VISIBLE);
                    break;
                case 2://进行中
                    viewHolder.mTvZhuangtai.setText("进行中");
                    viewHolder.mBtvChakan.setText("查看详情");
                    viewHolder.mBtvChakan.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    viewHolder.mTvZhuangtai.setText("已到达");
                    viewHolder.mBtvChakan.setText("查看详情");
                    viewHolder.mBtvChakan.setVisibility(View.VISIBLE);
                    viewHolder.mTvZhuangtai.setTextColor(0xff96a19b);
                    viewHolder.mTvJiage.setTextColor(0xff96a19b);
//                    viewHolder.mLlJing.setVisibility(View.GONE);//隐藏经过
//                    viewHolder.view_visib.setVisibility(View.GONE);
                    break;
                case 4:
                    viewHolder.mTvZhuangtai.setText("已结束");
                    viewHolder.mBtvChakan.setText("查看详情");
                    viewHolder.mBtvChakan.setVisibility(View.VISIBLE);
                    viewHolder.mTvZhuangtai.setTextColor(0xff96a19b);
                    viewHolder.mTvJiage.setTextColor(0xff96a19b);
//                    viewHolder.mLlJing.setVisibility(View.GONE);//隐藏经过
//                    viewHolder.view_visib.setVisibility(View.GONE);
                    break;
                case -1:
                    if (newsInfo.getCancelFee() <= 0) {
                        viewHolder.mTvZhuangtai.setText("已取消");
                        viewHolder.mTvZhuangtai.setTextColor(0xff96a19b);
                        viewHolder.mBtvChakan.setVisibility(View.GONE);
                        viewHolder.mLlJing.setVisibility(View.GONE);//隐藏经过
                        viewHolder.mTvJiage.setTextColor(0xff96a19b);
//                        viewHolder.view_visib.setVisibility(View.GONE);
                    } else {
                        viewHolder.mTvZhuangtai.setText("违约金");
                        viewHolder.mTvJiage.setText("¥" + newsInfo.getCancelFee());
                        viewHolder.mBtvChakan.setText("支付");
                        viewHolder.mBtvChakan.setVisibility(View.VISIBLE);
                    }
                    break;
            }

            if (orderAddress.size() <= 0)
                viewHolder.mBtvChakan.setVisibility(View.GONE);

            String sta = viewHolder.mTvZhuangtai.getText().toString();
            if (sta.equals("已结束") || sta.equals("已取消")) {
                viewHolder.mTvDingdanhao.setTextColor(0xff96a19b);
                viewHolder.mTvDate.setTextColor(0xff96a19b);
                viewHolder.mTvKehu.setTextColor(0xff96a19b);
                viewHolder.mTvDh.setTextColor(0xff96a19b);
                viewHolder.mTvCong.setTextColor(0xff96a19b);
                viewHolder.mTvDao.setTextColor(0xff96a19b);
                viewHolder.mTvJing.setTextColor(0xff96a19b);
                viewHolder.mTvJing2.setTextColor(0xff96a19b);
                viewHolder.mTvJing3.setTextColor(0xff96a19b);
                viewHolder.mTvJing4.setTextColor(0xff96a19b);
                viewHolder.mTvJing5.setTextColor(0xff96a19b);
            }
            if (sta.equals("已接单") || sta.equals("进行中") || sta.equals("已到达")) {
                viewHolder.mTvDingdanhao.setTextColor(0xff4e4e4e);
                viewHolder.mTvDate.setTextColor(0xff4e4e4e);
                viewHolder.mTvKehu.setTextColor(0xff4e4e4e);
                viewHolder.mTvDh.setTextColor(0xff4e4e4e);
                viewHolder.mTvCong.setTextColor(0xff4e4e4e);
                viewHolder.mTvDao.setTextColor(0xff4e4e4e);
                viewHolder.mTvJing.setTextColor(0xff4e4e4e);
                viewHolder.mTvJing2.setTextColor(0xff4e4e4e);
                viewHolder.mTvJing3.setTextColor(0xff4e4e4e);
                viewHolder.mTvJing4.setTextColor(0xff4e4e4e);
                viewHolder.mTvJing5.setTextColor(0xff4e4e4e);

                viewHolder.mTvJiage.setTextColor(0xffFF4900);
                viewHolder.mTvZhuangtai.setTextColor(0xffFF4900);
            }


            viewHolder.mBtvChakan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView button = (TextView) view;
                    String text = button.getText().toString();
                    mOnBtnClickListener.onBtnClick(newsInfo, text);
                }
            });
        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return newsInfos.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvDate;
        TextView mTvDh;
        TextView mTvDingdanhao;
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
        TextView mTvKehu;
        TextView mTvJiage;
        TextView mTvZhuangtai;
        BorderTextView mBtvChakan;


        public ViewHolder(View view) {
            super(view);
            mTvDate = (TextView) view.findViewById(R.id.tv_date);
            mTvDh = (TextView) view.findViewById(R.id.tv_dh);
            mTvZhuangtai = (TextView) view.findViewById(R.id.tv_zhuangtai);
            mTvJiage = (TextView) view.findViewById(R.id.tv_jiage);
            mTvKehu = (TextView) view.findViewById(R.id.tv_kehu);
            mTvDao = (TextView) view.findViewById(R.id.tv_dao);

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

            mTvDingdanhao = (TextView) view.findViewById(R.id.tv_dingdanhao);
            mTvCong = (TextView) view.findViewById(R.id.tv_cong);
            mIvCong = (ImageView) view.findViewById(R.id.iv_cong);
            mIvDao = (ImageView) view.findViewById(R.id.iv_dao);
            mIvJing = (ImageView) view.findViewById(R.id.iv_jing);
            mBtvChakan = (BorderTextView) view.findViewById(R.id.btv_chakan);
            mLlDao = (LinearLayout) view.findViewById(R.id.ll_dao);
            mLlJing = (LinearLayout) view.findViewById(R.id.ll_jing);
            mLlCong = (LinearLayout) view.findViewById(R.id.ll_cong);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private QiangOrderAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(QiangOrderAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnBtnClickListener {
        void onBtnClick(MeAllOrderInfo info, String viewName);
    }

    private MeAllOrderInfoAdapter.OnBtnClickListener mOnBtnClickListener;

    public void setOnBtnClickListener(MeAllOrderInfoAdapter.OnBtnClickListener mOnBtnClickListener) {
        this.mOnBtnClickListener = mOnBtnClickListener;
    }


}
