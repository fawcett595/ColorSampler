package com.colorsampler.android.test.color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.colorsampler.android.color.CSColor;
import com.colorsampler.android.color.ColorManager;

public class FindClosestColorTest extends TestCase
{
    private ColorManager _colorManager;
    
    public FindClosestColorTest(String name)
    {
        super(name);
        
        _colorManager = new ColorManager();
        ColorManager.LoadedColors = new ArrayList<CSColor>();
    }

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    // Sister color example - Green #008000, Maroon #800000, Navy Blue #000080
    public void SisterColorsTest()
    {
        CSColor maroon = new CSColor();
        maroon.addAttribute("name", "Maroon");
        maroon.addValue("800000");
        
        CSColor green = new CSColor();
        green.addAttribute("name", "Green");
        green.addValue("008000");
        
        CSColor navy = new CSColor();
        navy.addAttribute("name", "Navy");
        navy.addValue("000080");
        
        ColorManager.LoadedColors.add(maroon);
        ColorManager.LoadedColors.add(green);
        ColorManager.LoadedColors.add(navy);
        
        SortColors();

        CSColor maroonVariation = new CSColor();
        maroonVariation.addValue("700000");
        
        CSColor greenVariation = new CSColor();
        greenVariation.addValue("007000");
        
        CSColor navyVariation = new CSColor();
        navyVariation.addValue("000070");
               
        CSColor matchingMaroon = _colorManager.findClosestMatchingColor(maroonVariation);
        CSColor matchingGreen = _colorManager.findClosestMatchingColor(greenVariation);
        CSColor matchingNavy = _colorManager.findClosestMatchingColor(navyVariation);
        
        Assert.assertEquals("Maroon", matchingMaroon.Name);
        Assert.assertEquals("Green", matchingGreen.Name);
        Assert.assertEquals("Navy", matchingNavy.Name);
    }
    
    private void SortColors()
    {
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
    }
}
