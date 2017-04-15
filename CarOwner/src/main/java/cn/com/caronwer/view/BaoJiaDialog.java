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

import cn.com.caronwer.R;

public class BaoJiaDialog extends Dialog {

    private static BaoJiaDialog nickDialog;;
    public static NickListener nickListener;

    public BaoJiaDialog(Activity activity) {
        super(activity, R.style.myDialog);
    }

    public BaoJiaDialog(Activity activity, int style) {
        super(activity, style);
    }

    public static BaoJiaDialog nickDialog(Activity activity) {
        nickDialog = new BaoJiaDialog(activity);
        View v = View.inflate(activity, R.layout.dialog_baojia, null);
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

    public interface NickListener {
        void onSetting(String name);
    }

    public void setOnSettingListener(NickListener listener) {
        nickListener = listener;
    }



}
