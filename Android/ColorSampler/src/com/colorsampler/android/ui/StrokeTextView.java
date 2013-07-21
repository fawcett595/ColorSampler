package com.colorsampler.android.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class StrokeTextView extends TextView
{
    private Paint strokePaint;
    private Paint textPaint;
    
    public StrokeTextView(Context context)
    {
        super(context);
        strokePaint = new Paint();
        textPaint = new Paint();
    }
    
    public StrokeTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        strokePaint = new Paint();
        textPaint = new Paint();
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        strokePaint = new Paint();
        textPaint = new Paint();
    }
    
    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        strokePaint.setARGB(255, 0, 0, 0);
        strokePaint.setTextSize(getTextSize());
        strokePaint.setTypeface(Typeface.DEFAULT_BOLD);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(5);
    
        textPaint.setARGB(255, 255, 255, 255);
        textPaint.setTextSize(getTextSize());
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        
        canvas.drawText(getText().toString(), getLeft(), getTop() + getMeasuredHeight(), strokePaint);
        canvas.drawText(getText().toString(), getLeft(), getTop() + getMeasuredHeight(), textPaint);
        
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
    }
}
