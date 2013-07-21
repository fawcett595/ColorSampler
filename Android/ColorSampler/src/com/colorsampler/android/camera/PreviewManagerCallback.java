package com.colorsampler.android.camera;

import android.graphics.Bitmap;

import com.colorsampler.android.color.CSColor;

public interface PreviewManagerCallback
{
    void onPreviewUpdated(CSColor selectedColor, Bitmap screenCaptureBitmap, int width, int height);
}
