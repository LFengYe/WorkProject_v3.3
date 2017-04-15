package com.DLPort.myview;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.DLPort.R;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/5/7.
 */
public class MyDialog {
    private static final String TAG = "MyDialog";
    private Context context;
    private Dialogcallback dialogcallback;
    private Dialog dialog;
    private Button sure, cancel;
    private TextView textView;
    private EditText editText;
    private View time_select, xiang_select, dui;
    private int Nyear, NmonthOfYear, NdayOfMonth;
    private int NhourOfDay, Nminute;
    private int Type;//1表示输入框，2表示时间选择框，3表示选择框
    private RadioGroup size, dui_size;
    private String select = null;
    private String time = null;

    /**
     * init the dialog
     *
     * @return
     */
    public MyDialog(Context con, int type) {
        this.context = con;
        this.Type = type;
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_text);
        textView = (TextView) dialog.findViewById(R.id.dialog_name);
        cancel = (Button) dialog.findViewById(R.id.dialog_quxiao);
        sure = (Button) dialog.findViewById(R.id.dialog_OK);
        editText = (EditText) dialog.findViewById(R.id.dialog_content);
        time_select = dialog.findViewById(R.id.select_time);
        xiang_select = dialog.findViewById(R.id.select_xiang);
        dui = dialog.findViewById(R.id.select_dui);
        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        size = (RadioGroup) dialog.findViewById(R.id.RadioGroup);
        dui_size = (RadioGroup) dialog.findViewById(R.id.dui_RadioGroup);
        switch (Type) {
            case 1:
                editText.setVisibility(View.VISIBLE);
                break;
            case 2:
                time_select.setVisibility(View.VISIBLE);
                break;
            case 3:
                xiang_select.setVisibility(View.VISIBLE);
                break;
            case 4:
                dui.setVisibility(View.VISIBLE);
        }
        Calendar calendar = Calendar.getInstance();
        Nyear = calendar.get(Calendar.YEAR);
        NmonthOfYear = calendar.get(Calendar.MONTH);
        NdayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        NhourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        Nminute = calendar.get(Calendar.MINUTE);
        datePicker.init(Nyear, NmonthOfYear, NdayOfMonth, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Nyear = year;
                NmonthOfYear = monthOfYear;
                NdayOfMonth = dayOfMonth;
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                NhourOfDay = hourOfDay;
                Nminute = minute;
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Type) {
                    case 1:
                        dialogcallback.dialogdo(editText.getText().toString());
                        dismiss();
                        break;
                    case 2:
                        timePicker.clearFocus();
                        datePicker.clearFocus();
                        NmonthOfYear += 1;
                        if (Nminute == 0) {
                            time = Nyear + "-" + ((NmonthOfYear > 9) ? (NmonthOfYear) : ("0" + NmonthOfYear))
                                    + "-" + ((NdayOfMonth > 9) ? NdayOfMonth : ("0" + NdayOfMonth))
                                    + " " + ((NhourOfDay > 9) ? NhourOfDay : ("0" + NhourOfDay)) + ":00" + ":00";
                            Log.d(TAG, time);
                        } else {
                            time = Nyear + "-" + ((NmonthOfYear > 9) ? (NmonthOfYear) : ("0" + NmonthOfYear))
                                    + "-" + ((NdayOfMonth > 9) ? NdayOfMonth : ("0" + NdayOfMonth))
                                    + " " + ((NhourOfDay > 9) ? NhourOfDay : ("0" + NhourOfDay))
                                    + ":" + ((Nminute > 9) ? Nminute : ("0" + Nminute)) + ":00";
//                            time = Nyear + "-" + NmonthOfYear + "-" + NdayOfMonth + " " + NhourOfDay + ":" + Nminute + ":00";
                            Log.d(TAG, time);
                        }

                        dialogcallback.dialogdo(time);
                        dismiss();
                        break;
                    case 3:
                        switch (size.getCheckedRadioButtonId()) {
                            case R.id.Radio_one:
                                select = "20Gp-0";
                                break;
                            case R.id.Radio_two:
                                select = "20OT-1";
                                break;
                            case R.id.Radio_three:
                                select = "20罐装-2";
                                break;
                            case R.id.Radio_four:
                                select = "20G柜架-3";
                                break;
                            case R.id.Radio_five:
                                select = "20冷藏-4";
                                break;
                            case R.id.Radio_six:
                                select = "40GP-5";
                                break;
                            case R.id.Radio_seven:
                                select = "40HT-6";
                                break;
                            case R.id.Radio_eight:
                                select = "40冷藏-7";
                                break;
                            case R.id.Radio_nine:
                                select = "40高冷-8";
                                break;
                            case R.id.Radio_ten:
                                select = "40柜架-9";
                                break;
                            case R.id.Radio_eleven:
                                select = "40灌箱-10";
                                break;
                            case R.id.Radio_twelve:
                                select = "45GP-11";
                                break;
                            case R.id.Radio_thirteen:
                                select = "45HT-12";
                                break;
                            case R.id.Radio_fourteen:
                                select = "45冷藏-13";
                                break;
                            case R.id.Radio_fifteen:
                                select = "45高冷-14";
                                break;
                            case R.id.Radio_sixteen:
                                select = "特殊箱型-15";
                                break;
                        }
                        Log.d(TAG, String.valueOf(size.getCheckedRadioButtonId()));

                        Log.d(TAG, select);

                        dialogcallback.dialogdo(select);
                        dismiss();
                        break;
                    case 4:
                        switch (dui_size.getCheckedRadioButtonId()) {
                            case R.id.dui_Radio_one:
                                select = "20Gp-0";
                                break;
                            case R.id.dui_Radio_sixteen:
                                select = "特殊箱型-15";
                                break;
                        }
                        dialogcallback.dialogdo(select);
                        dismiss();
                        break;
                }
            }

        });
    }

    public void setDialogCallback(Dialogcallback dialogcallback) {
        this.dialogcallback = dialogcallback;
    }

    /**
     * @category Set The Content of the TextView
     */
    public void setContent(String content) {
        textView.setText(content);
    }

    /**
     * Get the Text of the EditText
     */
    public String getText() {
        return editText.getText().toString();
    }

    public void sethineText(String string) {
        editText.setHint(string);
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        dialog.hide();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    /**
     * 设定一个interfack接口,使mydialog可以處理activity定義的事情
     *
     * @author sfshine
     */
    public interface Dialogcallback {
        public void dialogdo(String string);
    }

}
