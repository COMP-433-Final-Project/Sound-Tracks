package com.example.soundtracks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.io.File;

public class DbHelper extends SQLiteOpenHelper {

    // VERSION 2 adds the appearance table
    // VERSION 3 adds the _ID column to the AppearanceTable
    // VERSION 4 removes it, since we need to change our query to support it
    private static final int VERSION = 4;
    private static final String NAME = "Contract.db";
    public long lastInsertID;
    public long lastInsertSongID;


    public DbHelper(@Nullable Context context) { super(context, NAME, null, VERSION); }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                DataBaseContract.PlayListTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(
                DataBaseContract.SongTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(
                DataBaseContract.PlaylistSongsTable.CREATE_TABLE);

        sqLiteDatabase.execSQL("INSERT INTO " + DataBaseContract.SongTable.TABLE_NAME + " (" + DataBaseContract.SongTable.COLUMN_NAME_NAME
                + ", " + DataBaseContract.SongTable.COLUMN_NAME_ARTIST + ", " + DataBaseContract.SongTable.COLUMN_NAME_SONG + ")"
                + " VALUES ('Phoenix', 'Cailin Russo', 'phoenix')");

        sqLiteDatabase.execSQL("INSERT INTO " + DataBaseContract.SongTable.TABLE_NAME + " (" + DataBaseContract.SongTable.COLUMN_NAME_NAME
                + ", " + DataBaseContract.SongTable.COLUMN_NAME_ARTIST + ", " + DataBaseContract.SongTable.COLUMN_NAME_SONG + ")"
                + " VALUES ('GoodByes', 'Post Malone', 'goodbyes')");


    }

    //insert playlist
    @WorkerThread
    public boolean insertPlaylist(String name, String latitude, String longitude, String radius) {
        return insertPlaylist(getWritableDatabase(), name, latitude, longitude, radius);
    }

    @WorkerThread
    private boolean insertPlaylist(SQLiteDatabase db, String name, String latitude, String longitude, String radius) {
        ContentValues values = makeContentValues(name, latitude, longitude, radius);

        long rowId = db.insert(DataBaseContract.PlayListTable.TABLE_NAME, null,
                values);

        if(rowId != -1){
            MainActivity.log("success insert");
            lastInsertID = rowId;
            return true;
        }
        else{
            MainActivity.log("error");
            return false;
        }
    }

    private ContentValues makeContentValues(String name, String latitude, String longitude, String radius) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.PlayListTable.COLUMN_NAME_NAME, name);
        values.put(DataBaseContract.PlayListTable.COLUMN_NAME_LATITUDE, latitude);
        values.put(DataBaseContract.PlayListTable.COLUMN_NAME_LONGITUDE, longitude);
        values.put(DataBaseContract.PlayListTable.COLUMN_NAME_RADIUS, radius);

        return values;
    }

    public String tableToString(SQLiteDatabase db, String tableName) {
        MainActivity.log("tableToString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        tableString += cursorToString(allRows);
        return tableString;
    }

    public String cursorToString(Cursor cursor){
        String cursorString = "";
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            for (String name: columnNames)
                cursorString += String.format("%s ][ ", name);
            cursorString += "\n";
            do {
                for (String name: columnNames) {
                    cursorString += String.format("%s ][ ",
                            cursor.getString(cursor.getColumnIndex(name)));
                }
                cursorString += "\n";
            } while (cursor.moveToNext());
        }
        return cursorString;
    }






    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + DataBaseContract.PlayListTable.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + DataBaseContract.SongTable.TABLE_NAME);
        onCreate(sqLiteDatabase);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "
                + DataBaseContract.PlaylistSongsTable.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }



}
