package cn.com.caronwer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.caronwer.R;
import cn.com.caronwer.bean.MeAllOrderInfo;
import cn.com.caronwer.bean.OrderAddressBean;

/**
 * Created by LFeng on 16/12/15.
 */
public class OrderDetailAdapter extends BaseAdapter {

    private List<OrderAddressBean> orderAddressList;
    private Context context;
    private ItemBtnClick itemBtnClick;
    private MeAllOrderInfo orderInfo;
    private boolean isComment;

    public boolean isComment() {
        return isComment;
    }

    public void setIsComment(boolean isComment) {
        this.isComment = isComment;
    }

    public MeAllOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(MeAllOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public ItemBtnClick getItemBtnClick() {
        return itemBtnClick;
    }

    public void setItemBtnClick(ItemBtnClick itemBtnClick) {
        this.itemBtnClick = itemBtnClick;
    }

    public OrderDetailAdapter(Context context, List<OrderAddressBean> data, ItemBtnClick itemBtnClick, MeAllOrderInfo orderInfo) {
        this.context = context;
        this.orderAddressList = data;
        this.itemBtnClick = itemBtnClick;
        this.orderInfo = orderInfo;
    }

    @Override
    public int getCount() {
        if (orderAddressList != null)
            return orderAddressList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (orderAddressList != null)
            return orderAddressList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final OrderAddressBean bean = orderAddressList.get(position);
        OrderAddressBean nextBean = null;
        if (position < getCount() - 1)
            nextBean = orderAddressList.get(position + 1);

        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_order_detail, null);
            holder = new ViewHolder();
            holder.itemImg = (ImageView) convertView.findViewById(R.id.item_img);
            holder.itemStatus = (TextView) convertView.findViewById(R.id.item_status);
            holder.itemTime = (TextView) convertView.findViewById(R.id.item_time);
            holder.itemAddress = (TextView) convertView.findViewById(R.id.item_address);
            holder.itemPayment = (TextView) convertView.findViewById(R.id.item_payment);
            holder.itemInfo = (TextView) convertView.findViewById(R.id.item_info);
            holder.itemLeftBtn = (Button) convertView.findViewById(R.id.item_left_btn);
            holder.itemLeftBtn.setBackgroundResource(R.drawable.shap_selector2);
            holder.itemRightBtn = (Button) convertView.findViewById(R.id.item_right_btn);
            holder.itemRightBtn.setBackgroundResource(R.drawable.shap_selector2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.itemTime.setText(bean.getDischargeTime());
        String address = context.getResources().getString(R.string.item_address, bean.getReceiptAddress(), bean.getReceipter(), bean.getReceiptTel());
        SpannableString spannableString = new SpannableString(context.getResources().getString(R.string.item_address, bean.getReceiptAddress(), bean.getReceipter(), bean.getReceiptTel()));
        spannableString.setSpan(new URLSpan("tel:" + bean.getReceiptTel()), address.length() - bean.getReceiptTel().length(), address.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), address.length() - bean.getReceiptTel().length(), address.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.itemAddress.setText(spannableString);
        holder.itemAddress.setMovementMethod(LinkMovementMethod.getInstance());
        if (bean.getPaymentGoods() < 0.0001) {
            holder.itemPayment.setVisibility(View.GONE);
        } else {
            holder.itemPayment.setText(context.getResources().getString(R.string.item_payment, bean.getPaymentGoods()));
        }

        if (position == 0) {
            holder.itemInfo.setVisibility(View.GONE);
            holder.itemStatus.setText(R.string.arrive_start_address);
            holder.itemLeftBtn.setText(R.string.confirm_cargo);
            holder.itemRightBtn.setText(R.string.nav_next_address);
            if (position == getCount() - 2) {
                holder.itemRightBtn.setText(R.string.nav_end_address);
            }
            if (TextUtils.isEmpty(bean.getDischargeTime())) {
                holder.itemTime.setVisibility(View.GONE);
                holder.itemImg.setImageResource(R.mipmap.start);
            } else if (TextUtils.isEmpty(nextBean.getDischargeTime())) {
                holder.itemTime.setVisibility(View.VISIBLE);
                holder.itemImg.setImageResource(R.mipmap.start_red);
                holder.itemLeftBtn.setClickable(false);
                holder.itemLeftBtn.setBackgroundResource(R.drawable.shape_button2);
            } else {
                holder.itemTime.setVisibility(View.VISIBLE);
                holder.itemImg.setImageResource(R.mipmap.start_gray);
                holder.itemLeftBtn.setClickable(false);
                holder.itemLeftBtn.setBackgroundResource(R.drawable.shape_button2);
            }
        } else if (position < getCount() - 1) {
            holder.itemInfo.setVisibility(View.GONE);
            holder.itemStatus.setText(R.string.arrive_pass_address);
            holder.itemLeftBtn.setVisibility(View.GONE);
            holder.itemRightBtn.setText(R.string.nav_next_address);
            if (position == getCount() - 2) {
                holder.itemRightBtn.setText(R.string.nav_end_address);
            }

            if (TextUtils.isEmpty(bean.getDischargeTime())) {
                holder.itemImg.setImageResource(R.mipmap.pass);
            } else if (TextUtils.isEmpty(nextBean.getDischargeTime())) {
                holder.itemImg.setImageResource(R.mipmap.pass_red);
            } else {
                holder.itemImg.setImageResource(R.mipmap.pass_gray);
            }
        } else if (position == getCount() - 1) {
            holder.itemInfo.setVisibility(View.VISIBLE);
            if (orderInfo.getIsCollectionPayment()) {
                holder.itemInfo.setText(R.string.cash_on_delivery);
            } else {
                holder.itemInfo.setText(R.string.wait_payment);
            }
            holder.itemStatus.setText(R.string.arrive_end_address);
            holder.itemLeftBtn.setVisibility(View.GONE);
            holder.itemRightBtn.setVisibility(View.GONE);

            if (TextUtils.isEmpty(bean.getDischargeTime())) {
                holder.itemImg.setImageResource(R.mipmap.arrive);
            } else if (isComment()) {
                holder.itemImg.setImageResource(R.mipmap.arrive_gray);
            } else {
                holder.itemImg.setImageResource(R.mipmap.arrive_red);
            }
        }

        holder.itemLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemBtnClick.leftBtnClick(orderInfo.getOrderNo(), position);
            }
        });
        holder.itemRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemBtnClick.rightBtnClick(position + 1);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView itemImg;
        TextView itemStatus;
        TextView itemTime;
        TextView itemAddress;
        TextView itemPayment;
        TextView itemInfo;
        Button itemLeftBtn;
        Button itemRightBtn;
    }

    public interface ItemBtnClick {
        void leftBtnClick(String orderNo, int addressIndex);

        void rightBtnClick(int index);
    }
}
