package com.colorsampler.android.activities;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.colorsampler.android.R;
import com.colorsampler.android.camera.CameraManager;
import com.colorsampler.android.camera.PreviewManager;
import com.colorsampler.android.camera.PreviewManagerCallback;
import com.colorsampler.android.color.CSColor;
import com.colorsampler.android.color.ColorManager;
import com.colorsampler.android.constants.Constants;
import com.colorsampler.android.ui.CrosshairView;
import com.colorsampler.android.xml.XMLParser;

public class CameraActivity extends Activity implements PreviewManagerCallback, SurfaceHolder.Callback, OnTouchListener
{
    private SurfaceView    _cameraSurfaceView;
    private TextView       _redValueLabel;
    private TextView       _greenValueLabel;
    private TextView       _blueValueLabel;
    private TextView       _hexValueLabel;
    private TextView       _closestMatchValueLabel;
    private CrosshairView  _crossHairView;
    private ProgressBar    _cameraProgressBar;
    
    private TextView _cameraInstructionsLabel;

    private CameraManager _cameraManager;
    private ColorManager _colorManager;
    private PreviewManager _previewManager;
    private ErrorManager _errorManager;
    
    private LoadCameraTask _cameraLoadingTask;
    private LoadXMLTask _xmlLoadingTask;

    /**
     * onCreate
     * 
     * Called when the Activity is created, Entry point of the program
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
        setContentView(R.layout.activity_camera);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        _redValueLabel = (TextView) findViewById(R.id.RedValue);
        _greenValueLabel = (TextView) findViewById(R.id.GreenValue);
        _blueValueLabel = (TextView) findViewById(R.id.BlueValue);
        _hexValueLabel = (TextView) findViewById(R.id.HexValue);
        _cameraInstructionsLabel = (TextView) findViewById(R.id.CameraInstructionLabel);
        _closestMatchValueLabel = (TextView) findViewById(R.id.ClosestMatchValue);
        _cameraSurfaceView = (SurfaceView) findViewById(R.id.CameraSurfaceView);
        _crossHairView = (CrosshairView) findViewById(R.id.CrossHairView);
        _cameraProgressBar = (ProgressBar) findViewById(R.id.CameraProgressBar);

        _errorManager = new ErrorManager(this);
        _colorManager = new ColorManager();
        _previewManager = new PreviewManager(this);
        _cameraManager = new CameraManager(this, _cameraSurfaceView, _previewManager, this);
        
        _cameraInstructionsLabel.setText(getString(R.string.camera_instructions));
    }

    /**
     * Called if the Activity ends.
     */
    @Override
    public void onPause()
    {
        super.onPause();
        
        _cameraLoadingTask.cancel(true);
        _xmlLoadingTask.cancel(true);
        
        _cameraManager.releaseCamera();
    }

    /**
     * Called if the Activity is started/restarted.
     */
    @Override
    public void onResume()
    {
        super.onResume();
        
        initialize();
    }

    private void initialize()
    {
        if(_cameraLoadingTask == null || _cameraLoadingTask.getStatus() != Status.RUNNING)
        {
            _cameraLoadingTask = new LoadCameraTask();
            _cameraLoadingTask.execute();
        }
        
        if((_xmlLoadingTask == null || _xmlLoadingTask.getStatus() != Status.RUNNING) && ColorManager.LoadedColors.size() == 0)
        {
            _xmlLoadingTask = new LoadXMLTask("colors.xml");
            _xmlLoadingTask.execute();
        }
        
        _cameraSurfaceView.setOnTouchListener(this);
    }

    public void onPreviewUpdated(CSColor selectedColor, Bitmap screenCaptureBitmap, int width, int height)
    {
        _crossHairView.setPreviewColorsFromBitmap(screenCaptureBitmap, width / 2, height / 2);
        
        CSColor bestMatchColor = _colorManager.findClosestMatchingColor(selectedColor);

        _closestMatchValueLabel.setText(bestMatchColor.Name);
        
        _hexValueLabel.setText(selectedColor.HexValue);
        _redValueLabel.setText(Integer.toString(selectedColor.RedValue));
        _greenValueLabel.setText(Integer.toString(selectedColor.GreenValue));
        _blueValueLabel.setText(Integer.toString(selectedColor.BlueValue));
    }
    
    private class LoadCameraTask extends AsyncTask<Void, Void, Boolean>
    {
        private Throwable _throwable;
        
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            
            _cameraProgressBar.setVisibility(View.VISIBLE);
            
            try
            {
                _cameraManager.getFirstCamera();
            }
            catch (Exception e)
            {
                _cameraLoadingTask.cancel(true);
                _cameraManager.releaseCamera();
                _cameraProgressBar.setVisibility(View.GONE);
                
                _errorManager.alertDialog(R.string.error_camera_access_title, 
                                           e.getMessage(), 
                                           R.string.try_again_button,
                                           R.string.cancel_button);
            }
        }
        
        @Override
        protected Boolean doInBackground(Void... params)
        {
            if(!_cameraLoadingTask.isCancelled())
            {
                try
                {
                    _cameraManager.setupCameraParameters();
                    return true;
                }
                catch (Throwable t)
                {
                    _throwable = t;
                }
            }                    
            
            return false;
        }
        
        @Override
        protected void onPostExecute(Boolean cameraLoaded)
        {
            super.onPostExecute(cameraLoaded);

            if (!cameraLoaded)
            {
                _cameraLoadingTask.cancel(true);
                _cameraManager.releaseCamera();
                _cameraProgressBar.setVisibility(View.GONE);
                
                _errorManager.alertDialog(R.string.error_camera_setup_title, 
                                           _throwable.getMessage(), 
                                           R.string.try_again_button,
                                           R.string.cancel_button);
                
                return;
            }
            
            try
            {
                _cameraManager.showCamera();
            }
            catch (Throwable t)
            {
                _cameraLoadingTask.cancel(true);
                _cameraManager.releaseCamera();
                
                _errorManager.alertDialog(R.string.error_camera_show_title, 
                                           t.getMessage(), 
                                           R.string.try_again_button,
                                           R.string.cancel_button);
            }
            
            _cameraProgressBar.setVisibility(View.GONE);
        }
        
        @Override
        protected void onCancelled()
        {
            super.onCancelled();

            _cameraManager.releaseCamera();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        initialize();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        initialize();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        _cameraManager.releaseCamera();
    }
    
    private class LoadXMLTask extends AsyncTask<Void, Void, Boolean>
    {
        private String _targetFileName;
        private Throwable _throwable;
        
        public LoadXMLTask(String fileName)
        {
            _targetFileName = fileName;
        }
        
        @Override
        protected Boolean doInBackground(Void... params)
        {
            if (!_xmlLoadingTask.isCancelled())
            {
                Log.i(Constants.LOG, "Loading colors from XML.");
                
                AssetManager assetManager = getAssets();
                InputStream inputStream = null;
                
                try
                {
                    inputStream = assetManager.open(_targetFileName);
                    
                    ColorManager.LoadedColors = XMLParser.<CSColor> parseAttributes(inputStream, "color", CSColor.class);

                    if (ColorManager.LoadedColors.isEmpty())
                    {
                        throw new Exception(getString(R.string.error_colors_not_found));
                    }
                }
                catch (Throwable t)
                {
                    _throwable = t;
                    return false;
                }
                finally
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch (Throwable t)
                    {
                        // Eat Exception
                    }
                }
                    
                Collections.sort(ColorManager.LoadedColors, new Comparator<CSColor>()
                                                             {
                                                                public int compare(CSColor c1, CSColor c2)
                                                                {
                                                                    if(c1.SortValue < c2.SortValue)
                                                                    {
                                                                        return -1;
                                                                    }
                                                                    else
                                                                    {
                                                                        return 1;
                                                                    }
                                                                }
                                                             });
                
                return true;
            }
            
            return false;
        }

        @Override
        protected void onPostExecute(Boolean colorsLoaded)
        {
            if (!colorsLoaded)
            {
                // We can continue with the application without the closest
                // matching color functionality
                _closestMatchValueLabel.setText(_throwable.getMessage());
                return;
            }
            
            Log.i(Constants.LOG, "Colors successfully loaded.");
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent e)
    {
        try
        {
            if (e.getActionMasked() == MotionEvent.ACTION_UP)
            {
                boolean isPreviewActive = _cameraManager.onScreenTouched();

                if (isPreviewActive)
                {
                    _cameraInstructionsLabel.setText(getString(R.string.camera_instructions));
                }
                else
                {
                    _cameraInstructionsLabel.setText(getString(R.string.camera_instructions2));
                }
            }
        }
        catch (Throwable t)
        {
            _cameraLoadingTask.cancel(true);
            _cameraManager.releaseCamera();
            
            _errorManager.alertDialog(R.string.error_camera_show_title, 
                                       t.getMessage(), 
                                       R.string.try_again_button,
                                       R.string.cancel_button);
        }
        
        return true;
    }
}
