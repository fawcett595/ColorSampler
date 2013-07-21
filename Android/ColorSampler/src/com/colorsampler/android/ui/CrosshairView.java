package com.colorsampler.android.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class CrosshairView extends View
{
    private Paint[] _fillPaints;
    private Paint _strokePaint;
    
    public CrosshairView(Context context)
    {
    	super(context);
    	initialize();
    }

    public CrosshairView(Context context, AttributeSet attrs)
    {
    	super(context, attrs);
    	initialize();
    }

    public CrosshairView(Context context, AttributeSet attrs, int defStyle)
    {
    	super(context, attrs, defStyle);
    	initialize();
    }

    private void initialize()
    {
        _fillPaints = new Paint[5];
        for(int i = 0; i < 5; ++i)
        {
            _fillPaints[i] = new Paint();
        }
        
    	_strokePaint = new Paint();
    	_strokePaint.setStyle(Style.STROKE);
    	_strokePaint.setStrokeWidth(3);
    	_strokePaint.setARGB(255, 0, 0, 0);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
    {
        setMeasuredDimension(150, 150);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
    	canvas.drawRect(0, 50, 50, 100, _fillPaints[0]);
        canvas.drawRect(50, 0, 100, 50, _fillPaints[1]);
        canvas.drawRect(100, 50, 150, 100, _fillPaints[2]);
        canvas.drawRect(50, 100, 100, 150, _fillPaints[3]);
        canvas.drawRect(50, 50, 100, 100, _fillPaints[4]);
    }

    public void setPreviewColorsFromBitmap(Bitmap screenCaptureBitmap, int x, int y)
    {
        _fillPaints[0].setColor(screenCaptureBitmap.getPixel(x - 1, y));
        _fillPaints[1].setColor(screenCaptureBitmap.getPixel(x, y - 1));
        _fillPaints[2].setColor(screenCaptureBitmap.getPixel(x + 1, y));
        _fillPaints[3].setColor(screenCaptureBitmap.getPixel(x, y + 1));
        _fillPaints[4].setColor(screenCaptureBitmap.getPixel(x, y));
        
        invalidate();
    }
}
