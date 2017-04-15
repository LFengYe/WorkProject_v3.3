package cn.com.caronwer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.com.caronwer.R;

public class UploadTextView extends TextView {
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStrokeWidth(2.5f);
        //  将边框设为黑色
        paint.setColor(getResources().getColor(R.color.back_top));
        //  画TextView的4个边
        canvas.drawLine((this.getHeight()-1)/6, (this.getWidth()-1)/2, (this.getWidth()-1)*5/6, (this.getHeight()-1)/2, paint);
        canvas.drawLine((this.getWidth()-1)/2, (this.getHeight()-1)/6, (this.getWidth()-1)/2, (this.getHeight()-1)*5/6, paint);

//        canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
//        canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
//        canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1, paint);
//        canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, paint);



    }
    public UploadTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
}
