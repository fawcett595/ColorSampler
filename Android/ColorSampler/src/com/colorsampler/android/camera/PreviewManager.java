package com.colorsampler.android.camera;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;

import com.colorsampler.android.color.CSColor;

public class PreviewManager implements PreviewCallback
{
    public PreviewManagerCallback _previewManagerCallback;

    private CSColor _selectedColor;
    private int _activeCameraWidth;
    private int _activeCameraHeight;
    
    public PreviewManager(PreviewManagerCallback callback)
    {
        _previewManagerCallback = callback;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, _activeCameraWidth, _activeCameraHeight, null);
      
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        Rect rect = new Rect(0, 0, _activeCameraWidth, _activeCameraHeight);
      
        yuvImage.compressToJpeg(rect, 100, output_stream);
      
        byte[] formatData = output_stream.toByteArray();
  
        Bitmap screenCaptureBitmap = BitmapFactory.decodeByteArray(formatData, 0, formatData.length);
      
        int centerPixelX = _activeCameraWidth / 2;
        int centerPixelY = _activeCameraHeight / 2;
      
        _selectedColor = new CSColor(screenCaptureBitmap.getPixel(centerPixelX, centerPixelY));
      
        if(_previewManagerCallback != null)
        {
            _previewManagerCallback.onPreviewUpdated(_selectedColor, screenCaptureBitmap, _activeCameraWidth, _activeCameraHeight);
        }
    }
    
    public void onCameraChanged(Camera camera)
    {
        Parameters cameraParameters = camera.getParameters();
        _activeCameraWidth = cameraParameters.getPreviewSize().width;
        _activeCameraHeight = cameraParameters.getPreviewSize().height;
    }
}