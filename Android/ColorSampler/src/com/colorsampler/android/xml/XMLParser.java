package com.colorsampler.android.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class XMLParser
{
    private XMLParser()
    {

    }

    @SuppressWarnings("unchecked")
    public static <T extends IXMLTag> List<T> parseAttributes(InputStream in, String tag, Class<? extends IXMLTag> returnClass) throws XmlPullParserException, IOException, InstantiationException, IllegalAccessException
    {
        List<T> returnList = new ArrayList<T>();

        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);

            int nextTag = 0;
            while (nextTag != XmlPullParser.END_DOCUMENT)
            {
                nextTag = parser.next();

                if (nextTag == XmlPullParser.START_TAG && parser.getName().compareToIgnoreCase(tag) == 0)
                {
                    IXMLTag returnTypeInstance = returnClass.newInstance();

                    int attrCount = parser.getAttributeCount();
                    for (int i = 0; i < attrCount; ++i)
                    {
                        returnTypeInstance.addAttribute(parser.getAttributeName(i), parser.getAttributeValue(i));
                    }

                    nextTag = parser.next();
                    if(nextTag == XmlPullParser.TEXT)
                    {
                        returnTypeInstance.addValue(parser.getText());
                    }

                    returnList.add((T) returnTypeInstance);
                }
            }
        }
        finally
        {
            in.close();
        }

        return returnList;
    }
}
