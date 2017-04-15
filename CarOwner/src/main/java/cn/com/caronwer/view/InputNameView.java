package cn.com.caronwer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import cn.com.caronwer.R;

public class InputNameView extends LinearLayout {
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStrokeWidth(4.0f);
        //  将边框设为黑色
        paint.setColor(getResources().getColor(R.color.back_top));
        //  画TextView的4个边
        canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
        canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
        canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1, paint);
        canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, paint);
    }
    public InputNameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
}
