package com.colorsampler.android.camera;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.RelativeLayout.LayoutParams;

import com.colorsampler.android.R;
import com.colorsampler.android.constants.Constants;
import com.colorsampler.android.preview.PreviewManager;

public class CameraManager implements PreviewCallback
{
    private Camera _cameraObject;
    private SurfaceView _surfaceView;
    private SurfaceHolder _surfaceViewHolder;
    private PreviewManager _previewManager;
    private LayoutParams _optimalLayoutParams;
    private Callback _surfaceViewCallback;
    private Activity _activity;
    
    private boolean _cameraPreviewStopped;
    
    public CameraManager(Activity activity, SurfaceView surfaceView, PreviewManager previewManager, Callback surfaceCallback)
    {
        _activity = activity;
        _surfaceView = surfaceView;
        _previewManager = previewManager;
        _surfaceViewHolder = _surfaceView.getHolder();
        _surfaceViewCallback = surfaceCallback;
        _surfaceViewHolder.addCallback(surfaceCallback);
        _surfaceViewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // Required for 2.3.3
        _cameraPreviewStopped = true;
    }
    
    public void freezeCamera()
    {
        _cameraObject.setOneShotPreviewCallback(this);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        
    }

    public void getFirstCamera() throws Exception
    {
        Log.d(Constants.LOG, "Finding first usable camera.");
        
        StringBuilder exceptions = new StringBuilder();
        
        for (int i = 0; i < Camera.getNumberOfCameras(); ++i)
        {
            try
            {
                _cameraObject = Camera.open(i);
                _previewManager.onCameraChanged(_cameraObject);
                
                Log.d(Constants.LOG, "Opened camera number " + i);
                
                break;
            }
            catch (Exception e)
            {
                exceptions.append(_activity.getString(R.string.error_camera_access_number) + i);
                exceptions.append("\n");
                exceptions.append(_activity.getString(R.string.error_message_from_system) + e.getMessage());
                exceptions.append("\n");
            }
        }
        
        if(_cameraObject == null)
        {
            throw new Exception(exceptions.toString());
        }
    }

    public void setupCameraParameters() throws Exception
    {
        Log.d(Constants.LOG, "Setting up camera parameters");
        
        Parameters cameraParameters = _cameraObject.getParameters();
        cameraParameters.setPreviewFrameRate(30);

        _optimalLayoutParams = calculateOptimalLayoutParams(cameraParameters.getSupportedPreviewSizes());
        
        _previewManager.onCameraChanged(_cameraObject);
    }
    
    private LayoutParams calculateOptimalLayoutParams(List<Size> supportedPreviewSizes) throws Exception
    {
        Log.d(Constants.LOG, "Calculating optimal layout size");
        
        if(supportedPreviewSizes.isEmpty())
        {
            throw new Exception(_activity.getString(R.string.error_supported_preview_size));
        }

        Size maxSize = supportedPreviewSizes.get(0);

        for (Size currentSize : supportedPreviewSizes)
        {
            if (currentSize.width * currentSize.height > maxSize.width * maxSize.height)
            {
                // The current size has a bigger area
                maxSize = currentSize;
            }
        }

        Log.d(Constants.LOG, "Optimal layout calculated as " + maxSize.width + "x" + maxSize.height);
        
        return new LayoutParams(maxSize.width, maxSize.height);
    }
    
    public void showCamera() throws IOException
    {
        Log.d(Constants.LOG, "Initializing Camera");
        
        _surfaceView.setLayoutParams(_optimalLayoutParams);
        
        Parameters cameraParams = _cameraObject.getParameters();
        cameraParams.setPreviewSize(_optimalLayoutParams.width, _optimalLayoutParams.height);
        cameraParams.setZoom(0);
        
        _cameraObject.stopPreview();
        
        _cameraPreviewStopped = true;
        
        _cameraObject.setPreviewCallback(_previewManager);
        _cameraObject.setPreviewDisplay(_surfaceViewHolder);
        _cameraObject.setParameters(cameraParams);
        _previewManager.onCameraChanged(_cameraObject);
        
        _cameraObject.startPreview();

        _cameraPreviewStopped = false;
        
        Log.d(Constants.LOG, "Camera Initialized");
    }
    
    public void releaseCamera()
    {
        if(_cameraObject != null)
        {
            _surfaceViewHolder.removeCallback(_surfaceViewCallback);
            
            _cameraObject.setPreviewCallback(null);
            _cameraObject.stopPreview();
            _cameraObject.release();
            _cameraObject = null;
        }
    }

    public boolean onScreenTouched() throws Exception
    {
        if (_cameraPreviewStopped)
        {
            _cameraObject.startPreview();
            _cameraPreviewStopped = false;
            return true;
        }

        _cameraPreviewStopped = true;
        _cameraObject.stopPreview();
        
        return false;
    }
}
