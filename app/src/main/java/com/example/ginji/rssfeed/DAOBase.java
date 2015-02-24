package com.example.ginji.rssfeed;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ginji on 22/02/2015.
 */
public abstract class DAOBase {
    protected final static int VERSION = 2;
    protected final static String NOM = "news.db";
    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    public DAOBase(Context pContext) {
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }
}