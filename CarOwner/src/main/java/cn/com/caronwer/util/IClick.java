package cn.com.caronwer.util;

import android.view.View;

public abstract class IClick implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        listViewItemClick((Integer) v.getTag(), v);
    }

    public abstract void listViewItemClick(int position, View v);

}