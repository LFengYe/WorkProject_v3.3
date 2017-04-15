package cn.com.caronwer.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.caronwer.R;

public class MyDialog extends Dialog {

    private static MyDialog nickDialog;
    private static MyDialog endDialog;
    private static MyDialog sureDialog;
    private static MyDialog psdDialog;

    public static NickListener nickListener;
    public static EndListener endListener;
    public static SureListener sureListener;
    public static PsdListener psdListener;

    public MyDialog(Activity activity) {
        super(activity, R.style.myDialog);
    }

    public MyDialog(Activity activity, int style) {
        super(activity, style);
    }

    public static MyDialog nickDialog(Activity activity) {
        nickDialog = new MyDialog(activity);

        View v = View.inflate(activity, R.layout.dialog_edit, null);
        final EditText et_nick = (EditText) v.findViewById(R.id.et_nick);
        Button bt_cancel = (Button) v.findViewById(R.id.bt_cancel);
        Button bt_ok = (Button) v.findViewById(R.id.bt_ok);
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = (int) (size.x * 0.8);
        int height = (int) (size.y * 0.3);
        nickDialog.setContentView(v);
        nickDialog.setCancelable(true);
        Window dialogWindow = nickDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = height;
        lp.width = width;
        dialogWindow.setAttributes(lp);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickDialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickListener.onSetting(et_nick.getText().toString());
            }
        });

        return nickDialog;
    }


    /**
     * 修改密码dialog
     * @param activity
     * @return
     */
    public static MyDialog psdDialog(Activity activity) {
        psdDialog = new MyDialog(activity);
        View v = View.inflate(activity, R.layout.dialog_psd, null);
        final EditText et_old = (EditText) v.findViewById(R.id.et_old);
        final EditText et_new = (EditText) v.findViewById(R.id.et_new);
        final EditText et_new1 = (EditText) v.findViewById(R.id.et_new1);
        Button bt_cancel = (Button) v.findViewById(R.id.bt_cancel);
        Button bt_ok = (Button) v.findViewById(R.id.bt_ok);

        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = (int) (size.x * 0.9);
        int height = (int) (size.y * 0.53);
        psdDialog.setContentView(v);
        psdDialog.setCancelable(true);
        Window dialogWindow = psdDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = height;
        lp.width = width;
        dialogWindow.setAttributes(lp);


        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psdDialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psdListener.onSetting(et_old.getText().toString(), et_new.getText().toString(), et_new1.getText().toString());
            }
        });
        return psdDialog;
    }

    /**
     * 撤销订单dialog
     * @param activity
     * @return
     */
    public static MyDialog endDialog(final Activity activity) {
        endDialog = new MyDialog(activity);
        View v = View.inflate(activity, R.layout.dialog_reason, null);

        final EditText et_content = (EditText) v.findViewById(R.id.et_reason);
        Button bt_cancel = (Button) v.findViewById(R.id.bt_cancel);
        Button bt_ok = (Button) v.findViewById(R.id.bt_ok);

        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = (int) (size.x * 0.8);
        int height = (int) (size.y * 0.3);
        endDialog.setContentView(v);
        endDialog.setCancelable(true);
        Window dialogWindow = endDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = height;
        lp.width = width;
        dialogWindow.setAttributes(lp);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_content.getText().toString().isEmpty()) {
                    Toast.makeText(activity, "请输入原因", Toast.LENGTH_SHORT).show();
                    return;
                }
                endListener.onSetting(et_content.getText().toString());
            }
        });

        return endDialog;
    }

    /**
     * 确认抢单dialog
     * @param activity
     * @return
     */
    public static MyDialog sureDialog(final Activity activity) {
        sureDialog = new MyDialog(activity);
        View v = View.inflate(activity, R.layout.dialog_querenqiangdan, null);

        Button bt_cancel = (Button) v.findViewById(R.id.bt_cancel);
        Button bt_ok = (Button) v.findViewById(R.id.bt_ok);

        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = (int) (size.x * 0.8);
        int height = (int) (size.y * 0.3);
        sureDialog.setContentView(v);
        sureDialog.setCancelable(true);
        Window dialogWindow = sureDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = height;
        lp.width = width;
        dialogWindow.setAttributes(lp);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sureDialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endListener.onSetting("");
            }
        });

        return sureDialog;
    }

    /**
     * 确认打电话dialog
     * @param activity
     * @return
     */
    public static MyDialog playDialog(final Activity activity,String no) {
        sureDialog = new MyDialog(activity);
        View v = View.inflate(activity, R.layout.dialog_play, null);

        TextView tv_no = (TextView) v.findViewById(R.id.tv_no);
        Button bt_cancel = (Button) v.findViewById(R.id.bt_cancel);
        Button bt_ok = (Button) v.findViewById(R.id.bt_ok);
        tv_no.setText("你确定要拨打"+no+"吗？");

        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = (int) (size.x * 0.8);
        int height = (int) (size.y * 0.3);
        sureDialog.setContentView(v);
        sureDialog.setCancelable(true);
        Window dialogWindow = sureDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = height;
        lp.width = width;
        dialogWindow.setAttributes(lp);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sureDialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endListener.onSetting("");
            }
        });

        return sureDialog;
    }

    /**
     * 确认装货dialog
     * @param activity
     * @return
     */
    public static MyDialog zhuanghuoDialog(final Activity activity) {
        sureDialog = new MyDialog(activity);
        View v = View.inflate(activity, R.layout.dialog_zhuang, null);

        Button bt_cancel = (Button) v.findViewById(R.id.bt_cancel);
        Button bt_ok = (Button) v.findViewById(R.id.bt_ok);

        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = (int) (size.x * 0.8);
        int height = (int) (size.y * 0.3);
        sureDialog.setContentView(v);
        sureDialog.setCancelable(true);
        Window dialogWindow = sureDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = height;
        lp.width = width;
        dialogWindow.setAttributes(lp);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sureDialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endListener.onSetting("");
            }
        });

        return sureDialog;
    }

    public interface NickListener {
        void onSetting(String name);
    }

    public void setOnSettingListener(NickListener listener) {
        nickListener = listener;
    }

    public interface PsdListener {
        void onSetting(String old, String new1, String new2);
    }

    public void setOnSettingListener(PsdListener listener) {
        psdListener = listener;
    }

    public interface EndListener {
        void onSetting(String content);
    }
    public interface SureListener {
        void onSetting();
    }

    public void setOnSettingListener(EndListener listener) {
        endListener = listener;
    }

}
