package com.example.ginji.rssfeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Ginji on 22/02/2015.
 */
public class NewsDAO extends DAOBase {

    public static final String NEWS_KEY = "id";
    public static final String NEWS_TITLE = "title";
    public static final String NEWS_DESC = "desc";
    public static final String NEWS_DATE = "date";
    public static final String NEWS_PIC = "pic";
    public static final String TABLE_NAME = "News";

    public NewsDAO(Context pContext) {
        super(pContext);
    }

    /**
     * @param m l'item à ajouter à la base
     */
    public void insert(Item m) {
        ContentValues value = new ContentValues();
        value.put(NewsDAO.NEWS_TITLE, m.getTitle());
        value.put(NewsDAO.NEWS_DESC, m.getDesc());
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss");
        value.put(NewsDAO.NEWS_DATE, formatter.format(m.getDate()));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        m.getPic().compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        value.put(NewsDAO.NEWS_PIC, bArray);
        long test = mDb.insert(NewsDAO.TABLE_NAME, null, value);
    }

    public void delete() {
        mDb.delete("sqlite_sequence", "name = ?", new String[]{TABLE_NAME});
        mDb.delete(TABLE_NAME, null, null);
    }

    public Item getItemWithId(int id2) {
        Cursor c = null;
        Item tmp = null;
        try {
            //Récupère dans un Cursor les valeur correspondant à un item contenu dans la BDD
            c = mDb.query(TABLE_NAME, new String[]{NEWS_KEY, NEWS_TITLE, NEWS_DESC, NEWS_DATE, NEWS_PIC}, NEWS_KEY + " = " + id2, null, null, null, null, null);
            tmp = cursorToItem(c);
        } finally {
            c.close();
        }
        return tmp;
    }

    private Item cursorToItem(Cursor c) {
        Item item = null;
        if (c.getCount() == 0)
            return null;
        c.moveToFirst();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss");
        Bitmap bm = BitmapFactory.decodeByteArray(c.getBlob(4), 0, c.getBlob(4).length);
        try {
            item = new Item(c.getString(1), c.getString(2), formatter.parse(c.getString(3)), bm, null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.close();
        return item;
    }
}
