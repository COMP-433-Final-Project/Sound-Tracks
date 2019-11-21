package com.example.soundtracks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    // VERSION 2 adds the appearance table
    // VERSION 3 adds the _ID column to the AppearanceTable
    // VERSION 4 removes it, since we need to change our query to support it
    private static final int VERSION = 4;
    private static final String NAME = "Contract.db";

    public DbHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                DataBaseContract.PlayListTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(
                DataBaseContract.PlayListTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(
                DataBaseContract.PlaylistSongsTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + DataBaseContract.PlayListTable.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + DataBaseContract.PlayListTable.TABLE_NAME);
        onCreate(sqLiteDatabase);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + DataBaseContract.PlaylistSongsTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }



}
