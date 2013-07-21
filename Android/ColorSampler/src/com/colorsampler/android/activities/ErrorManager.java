package com.colorsampler.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.colorsampler.android.R;

public class ErrorManager
{
    private Activity _activity;
    private LayoutInflater _inflater;
    private View _reportErrorView;

    public ErrorManager(Activity activity)
    {
        _activity = activity;
        _inflater = _activity.getLayoutInflater();
    }
    
    public void reportDialog(int title, String message, int positive, int negative)
    {
        TextView errorMessage;
        
        try
        {
            _reportErrorView = _inflater.inflate(R.layout.dialog_error, null);
            errorMessage = (TextView) _reportErrorView.findViewById(R.id.ErrorMessage);
        }
        catch (Throwable t)
        {
            // Something is wrong with the view, so just display the regular alert
            alertDialog(title, message, positive, negative);
            return;
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(_activity);

        builder.setView(_reportErrorView);
        
        builder.setTitle(_activity.getString(title));
        errorMessage.setText(message);
        builder.setPositiveButton(_activity.getString(positive), null);
        builder.setNegativeButton(_activity.getString(negative), null);
        
        builder.show();
    }

    public void alertDialog(int title, String message, int positive, int negative)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(_activity);
        
        builder.setTitle(_activity.getString(title));
        builder.setMessage(message);
        builder.setPositiveButton(_activity.getString(positive), null);
        builder.setNegativeButton(_activity.getString(negative), null);
        
        builder.show();
    }
}
