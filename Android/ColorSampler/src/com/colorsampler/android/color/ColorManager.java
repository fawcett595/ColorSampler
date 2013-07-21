package com.colorsampler.android.color;

import java.util.ArrayList;
import java.util.List;


public class ColorManager
{
    public static List<CSColor> LoadedColors;
    
    private static CSColor _bestMatch;
    private static int _currentVariance;
    private static int _lowestVariance;
    
    public ColorManager()
    {
        LoadedColors = new ArrayList<CSColor>();
    }
    
    public CSColor findClosestMatchingColor(CSColor color)
    {
        _bestMatch = CSColor.InvalidColor;
        _lowestVariance = 1000; // 255 * 3 = 765 is the highest possible variance
        
        for(CSColor loadedColor : LoadedColors)
        {
            _currentVariance = Math.abs(color.RedValue - loadedColor.RedValue) + 
                               Math.abs(color.GreenValue - loadedColor.GreenValue) +
                               Math.abs(color.BlueValue - loadedColor.BlueValue);
            
            if (_currentVariance < _lowestVariance)
            {
                _lowestVariance = _currentVariance;
                _bestMatch = loadedColor;
            }
        }
        
        return _bestMatch;
    }
    
//    if (LoadedColors.size() > 0)
//    {
//        int start = 0;
//        int end = LoadedColors.size() - 1;
//        int mid;
//        CSColor midColor;
//          
//        while(start < end)
//        {
//            mid = (start + end) / 2;
//            
//            if (start == mid)
//            {
//                // We've run out of options in between
//                int startVariance = Math.abs(color.SortValue - LoadedColors.get(start).SortValue);
//                int endVariance = Math.abs(color.SortValue - LoadedColors.get(end).SortValue);
//    
//                return startVariance < endVariance ? LoadedColors.get(start) : LoadedColors.get(end);
//            }
//            
//            midColor = LoadedColors.get(mid);
//              
//            if (color.SortValue > midColor.SortValue)
//            {
//                start = mid;
//            }
//            else if (color.SortValue < midColor.SortValue)
//            {
//                end = mid;
//            }
//            else
//            {
//                // It's unlikely that there will be an exact match, but we must check
//                return midColor;
//            }
//        }
//      }
//      
//      return new CSColor("Invalid");
}
