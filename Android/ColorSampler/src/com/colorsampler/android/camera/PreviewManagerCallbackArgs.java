package com.colorsampler.android.camera;

import com.colorsampler.android.color.CSColor;

public class PreviewManagerCallbackArgs
{
    public CSColor LeftColor;
    public CSColor TopColor;
    public CSColor RightColor;
    public CSColor BottomColor;
    public CSColor SelectedColor;
    public CSColor BestMatchColor;
    
    public PreviewManagerCallbackArgs(CSColor left, CSColor top, CSColor right, CSColor bottom, CSColor center, CSColor bestMatch)
    {
        LeftColor = left;
        TopColor = top;
        RightColor = right;
        BottomColor = bottom;
        SelectedColor = center;
        BestMatchColor = bestMatch;
    }
}
