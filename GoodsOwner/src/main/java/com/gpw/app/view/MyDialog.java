package com.gpw.app.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gpw.app.R;
import com.gpw.app.util.DensityUtil;
import com.gpw.app.util.FastBlurUtil;


/**
 * Created by gpw on 2016/8/2.
 * --加油
 */
public class MyDialog extends Dialog {

    private static MyDialog nickDialog;
    private static MyDialog endDialog;
    private static MyDialog psdDialog;
    private static MyDialog selectSexDialog;
    private static MyDialog selectPicDialog;
    private static MyDialog loginPicDialog;
    private static MyDialog registerDialog;

    public static NickListener nickListener;
    public static EndListener endListener;
    public static PsdListener psdListener;
    public static LoginListener loginListener;
    public static RegisterListener registerListener;

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


    public static MyDialog selectSexDialog(final Activity activity, View.OnClickListener sexOnClick) {
        selectSexDialog = new MyDialog(activity);
        View v = View.inflate(activity, R.layout.dialog_sex, null);

        Button manBtn = (Button) v.findViewById(R.id.manBtn);
        Button womanBtn = (Button) v.findViewById(R.id.womanBtn);
        Button cancelBtn = (Button) v.findViewById(R.id.cancelBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSexDialog.dismiss();
            }
        });

        manBtn.setOnClickListener(sexOnClick);
        womanBtn.setOnClickListener(sexOnClick);

        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        selectSexDialog.setContentView(v);
        selectSexDialog.setCancelable(true);
        Window dialogWindow = selectSexDialog.getWindow();
        dialogWindow.setWindowAnimations(R.style.PopupAnimation); //设置窗口弹出动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.height = DensityUtil.dip2px(activity, 183.2f);
        lp.width = width;
        dialogWindow.setAttributes(lp);
        return selectSexDialog;
    }

    public static MyDialog selectPicDialog(final Activity activity, View.OnClickListener picOnClick) {
        selectPicDialog = new MyDialog(activity);
        View v = View.inflate(activity, R.layout.dialog_pic, null);

        Button takePhotoBtn = (Button) v.findViewById(R.id.takePhotoBtn);
        Button pickPhotoBtn = (Button) v.findViewById(R.id.pickPhotoBtn);
        Button cancelBtn = (Button) v.findViewById(R.id.cancelBtn);

        pickPhotoBtn.setOnClickListener(picOnClick);
        takePhotoBtn.setOnClickListener(picOnClick);
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectPicDialog.dismiss();
            }
        });
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        selectPicDialog.setContentView(v);
        selectPicDialog.setCancelable(true);
        Window dialogWindow = selectPicDialog.getWindow();
        assert dialogWindow != null;
        dialogWindow.setWindowAnimations(R.style.PopupAnimation); //设置窗口弹出动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.BOTTOM);

        lp.height = DensityUtil.dip2px(activity, 183.2f);
        lp.width = width;
        dialogWindow.setAttributes(lp);
        return selectPicDialog;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static MyDialog loginDialog(final Activity activity, String name, String password) {
        loginPicDialog = new MyDialog(activity, R.style.fullDialog);
        View v = View.inflate(activity, R.layout.dialog_login, null);
        final EditText et_account = (EditText) v.findViewById(R.id.et_account);
        final EditText et_password = (EditText) v.findViewById(R.id.et_password);
        ImageView iv_close = (ImageView) v.findViewById(R.id.iv_close);
        TextView tv_forget_psd = (TextView) v.findViewById(R.id.tv_forget_psd);
        TextView tv_register = (TextView) v.findViewById(R.id.tv_register);
        final Button bt_login = (Button) v.findViewById(R.id.bt_login);
        CheckBox cb_eye = (CheckBox) v.findViewById(R.id.cb_eye);
        RelativeLayout login_layout = (RelativeLayout) v.findViewById(R.id.login_layout);
        et_account.setText(name);
        et_password.setText(password);
        if (name.length() > 2) {
            bt_login.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_red_bg));
        }
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int size = s.length();
                if (size != 0) {
                    bt_login.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_red_bg));
                } else {
                    bt_login.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_login));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cb_eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        Bitmap bmp = FastBlurUtil.getBlurBackgroundDrawer(activity);

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginListener.onLoginClick(0);
            }
        });

        tv_forget_psd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginListener.onLoginClick(1);
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginListener.onLoginClick(2);
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginListener.onSetting(et_account.getText().toString(), et_password.getText().toString());
            }
        });


        login_layout.setBackground(new BitmapDrawable(activity.getResources(), bmp));
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        loginPicDialog.setContentView(v);
        loginPicDialog.setCancelable(true);
        Window dialogWindow = loginPicDialog.getWindow();
        assert dialogWindow != null;
        dialogWindow.setWindowAnimations(R.style.AnimationFade); //设置窗口弹出动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.height = height;
        lp.width = width;
        dialogWindow.setAttributes(lp);
        return loginPicDialog;
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static MyDialog registerDialog(final Activity activity) {
        registerDialog = new MyDialog(activity, R.style.fullDialog);
        View v = View.inflate(activity, R.layout.dialog_register, null);
        final EditText et_account = (EditText) v.findViewById(R.id.et_account);
        final EditText et_password = (EditText) v.findViewById(R.id.et_password);
        final EditText et_validate_code = (EditText) v.findViewById(R.id.et_validate_code);
        TextView tv_get_code = (TextView) v.findViewById(R.id.tv_get_code);
        final Button bt_ok = (Button) v.findViewById(R.id.bt_ok);
        CheckBox cb_eye = (CheckBox) v.findViewById(R.id.cb_eye);
        ImageView iv_close = (ImageView) v.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerListener.onRegisterClose();
            }
        });
        RelativeLayout register_layout = (RelativeLayout) v.findViewById(R.id.register_layout);
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int size = s.length();
                if (size != 0) {
                    bt_ok.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_red_bg));
                } else {
                    bt_ok.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_login));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cb_eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        Bitmap bmp = FastBlurUtil.getBlurBackgroundDrawer(activity);


        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerListener.onRegister(et_account.getText().toString(), et_password.getText().toString(),et_validate_code.getText().toString());
            }
        });

        tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerListener.onRegisterCode(et_account.getText().toString());
            }
        });


        register_layout.setBackground(new BitmapDrawable(activity.getResources(), bmp));
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        registerDialog.setContentView(v);
        registerDialog.setCancelable(true);
        Window dialogWindow = registerDialog.getWindow();
        assert dialogWindow != null;
        dialogWindow.setWindowAnimations(R.style.AnimationFade); //设置窗口弹出动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.height = height;
        lp.width = width;
        dialogWindow.setAttributes(lp);
        return registerDialog;
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

    public void setOnSettingListener(EndListener listener) {
        endListener = listener;
    }


    public interface LoginListener {
        void onSetting(String name, String psd);
        void onLoginClick(int type);
    }

    public void setOnLoginListener(LoginListener listener) {
        loginListener = listener;
    }


    public interface RegisterListener {
        void onRegister(String name, String psd,String code);
        void onRegisterCode(String name);
        void onRegisterClose();
    }

    public void setRegisterListener(RegisterListener listener) {
        registerListener = listener;
    }

}
