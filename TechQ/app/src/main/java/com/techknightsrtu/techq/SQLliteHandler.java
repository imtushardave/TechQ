package com.techknightsrtu.techq;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLliteHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "";
    private static final String TABLE_NAME = "";

    private static final String KEY_ID = "id", KEY_NAME = "name", KEY_EMAIL = "email",
                                KEY_UID = "uid", KEY_CREATED_AT = "created_at" ;

    public SQLliteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
