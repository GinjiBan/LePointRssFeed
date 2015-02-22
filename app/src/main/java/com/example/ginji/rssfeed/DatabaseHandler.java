package com.example.ginji.rssfeed;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ginji on 21/02/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String NEWS_KEY = "id";
    public static final String NEWS_TITLE = "title";
    public static final String NEWS_DESC = "desc";
    public static final String NEWS_DATE = "date";
    public static final String NEWS_PIC = "pic";

    public static final String METIER_TABLE_NAME = "News";
    public static final String METIER_TABLE_CREATE =
            "CREATE TABLE " + METIER_TABLE_NAME + " (" +
                    NEWS_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NEWS_TITLE + " TEXT, " +
                    NEWS_DESC + " TEXT, " +
                    NEWS_DATE + " TEXT, " +
                    NEWS_PIC + " BLOB);";
    public static final String METIER_TABLE_DROP = "DROP TABLE IF EXISTS " + METIER_TABLE_NAME + ";";


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(METIER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(METIER_TABLE_DROP);
        onCreate(db);
    }
}