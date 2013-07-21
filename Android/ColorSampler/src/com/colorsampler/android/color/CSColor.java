package com.colorsampler.android.color;

import android.graphics.Color;

import com.colorsampler.android.xml.IXMLTag;

public class CSColor implements IXMLTag
{
    public static CSColor InvalidColor = new CSColor("Invalid");
    
    public String Name;
    public String HexValue;
    public int    RedValue;
    public int    GreenValue;
    public int    BlueValue;

    public int    SortValue;
    
    public CSColor()
    {
        RedValue = -255;
        GreenValue = -255;
        BlueValue = -255;
    }
    
    public CSColor(String name)
    {
        Name = name;
    }
    
    public CSColor(int pixel)
    {
        RedValue = Color.red(pixel);
        GreenValue = Color.green(pixel);
        BlueValue = Color.blue(pixel);
        
        HexValue = createHexString(RedValue, GreenValue, BlueValue);
    }
    
    private String createHexString(int red, int green, int blue)
    {
        StringBuilder hexString = new StringBuilder();
        hexString.append(String.format("%02x", red));
        hexString.append(String.format("%02x", green));
        hexString.append(String.format("%02x", blue));
        return hexString.toString();
    }
    
    private void setRGB(String hexValue)
    {
        RedValue = Integer.parseInt(hexValue.substring(0,2), 16);
        GreenValue = Integer.parseInt(hexValue.substring(2,4), 16);
        BlueValue = Integer.parseInt(hexValue.substring(4,6), 16);
    }
    
    public void addValue(String value)
    {
        HexValue = value.replace("#", "");
        SortValue = Integer.parseInt(HexValue, 16);
        
        setRGB(HexValue);
    }

    public void addAttribute(String name, String value)
    {
        // Cannot use switch statement in JRE 1.6
        if (name.equals("name"))
        {
            Name = value;
        }
    }
}
