package com.example.ginji.rssfeed;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Ginji on 19/02/2015.
 */
public class Item implements Serializable {
    public String title;
    public final String link;
    public final String summary;

    public Item(String title, String summary, String link) {
        this.title = title;
        this.summary = summary;
        this.link = link;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}