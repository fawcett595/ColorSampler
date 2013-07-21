package com.colorsampler.android.activities;

import android.app.Activity;
import android.app.AlertDialog;

public class ErrorManager
{
    private Activity _activity;

    public ErrorManager(Activity activity)
    {
        _activity = activity;
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
