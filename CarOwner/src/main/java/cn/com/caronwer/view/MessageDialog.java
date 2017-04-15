package cn.com.caronwer.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import cn.com.caronwer.R;

public class MessageDialog extends Dialog {

    private static MessageDialog nickDialog;;
    public static NickListener nickListener;
    public static String mmsg = "";

    public MessageDialog(Activity activity) {
        super(activity, R.style.myDialog);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public static MessageDialog nickDialog(Activity activity,String msg) {
        mmsg = msg;
        nickDialog = new MessageDialog(activity);
        View v = View.inflate(activity, R.layout.dialog_msg, null);
        final TextView et_nick = (TextView) v.findViewById(R.id.et_nick);
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
        et_nick.setText(mmsg);

        return nickDialog;
    }

    public interface NickListener {
        String onSetting();
    }

    public void setOnSettingListener(NickListener listener) {
        nickListener = listener;
    }



}
