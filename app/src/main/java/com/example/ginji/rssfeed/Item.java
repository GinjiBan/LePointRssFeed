package com.example.ginji.rssfeed;

import android.graphics.Bitmap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ginji on 19/02/2015.
 */
public class Item implements Serializable {
    private String title;
    private String desc;
    private Date date;
    private Bitmap pic;

    public Item(String title, String desc, Date date, Bitmap pic) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.pic = pic;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }
}