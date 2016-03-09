package com.ass1.mandeep.singh_mandeep_213347007;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mandee on 17/09/2015.
 */

public class MyOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Players";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SCORE = "score";
    private static final String DATABASE_CREATE = "create table "+ TABLE_NAME +
            "(" + COLUMN_ID + " integer primary key autoincrement, " + COLUMN_NAME + " text not null, " + COLUMN_SCORE +
            " integer not null);";
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
