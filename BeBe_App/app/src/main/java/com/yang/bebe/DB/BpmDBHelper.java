package com.yang.bebe.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017-01-17.
 */

public class BpmDBHelper extends SQLiteOpenHelper {
    public BpmDBHelper(Context context) {
        super(context, "Bpm", null, 1);
    }
    public static BpmDBHelper dbHelper;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE bpm (" +
                "'code' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`date` TEXT," +
                "`contents` TEXT);";

        db.execSQL(sql);


        db.execSQL("insert into bpm ('date', 'contents') values('17년 10월 10일 17시 6분 30초','76')");
        db.execSQL("insert into bpm ('date', 'contents') values('17년 10월 10일 18시 10분 27초','74')");
        db.execSQL("insert into bpm ('date', 'contents') values('17년 10월 11일 16시 33분 11초','78')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists bpm";
        db.execSQL(sql);

        onCreate(db);
    }
}
