package com.colorsampler.android.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class TransparentView extends View
{
    private Paint backgroundPaint;
    private Paint borderPaint;
    private RectF drawRect;

    public TransparentView(Context context)
    {
    	super(context);
    	init();
    }

    public TransparentView(Context context, AttributeSet attrs)
    {
    	super(context, attrs);
    	init();
    }

    private void init()
    {
    	backgroundPaint = new Paint();
    	backgroundPaint.setARGB(255, 75, 75, 75);
    	backgroundPaint.setAntiAlias(true);
    
    	borderPaint = new Paint();
    	borderPaint.setARGB(255, 255, 255, 255);
    	borderPaint.setAntiAlias(true);
    	borderPaint.setStyle(Style.STROKE);
    	borderPaint.setStrokeWidth(2);
        
    	drawRect = new RectF();
        drawRect.set(0, 0, 50, 50);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(parentWidth, parentHeight);
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawRect(drawRect, backgroundPaint);
        super.onDraw(canvas);
    }
}
