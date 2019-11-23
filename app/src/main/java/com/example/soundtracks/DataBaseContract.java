package com.example.soundtracks;

import android.provider.BaseColumns;

public class DataBaseContract {

    public static class PlayListTable implements BaseColumns {
        public static final String TABLE_NAME = "PlayList";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_RADIUS = "radius";
        public static final String COLUMN_NAME_NAME = "name";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_NAME + " TEXT, "
                + COLUMN_NAME_LATITUDE + " TEXT, "
                + COLUMN_NAME_LONGITUDE + " TEXT, "
                + COLUMN_NAME_RADIUS + " TEXT)";
    }

    public static class SongTable implements BaseColumns {
        public static final String TABLE_NAME = "Songs";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ARTIST = "artist";
        public static final String COLUMN_NAME_BLOB = "blob";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_NAME + " TEXT, "
                + COLUMN_NAME_ARTIST + " TEXT, "
                + COLUMN_NAME_BLOB + " BLOB)";
    }

    public static class PlaylistSongsTable implements BaseColumns {
        public static final String TABLE_NAME = "PlayListSongs";
        public static final String COLUMN_NAME_SONG_ID = "songID";
        public static final String COLUMN_NAME_PLAYLIST_ID = "playlistID";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME_SONG_ID + " TEXT, "
                + COLUMN_NAME_PLAYLIST_ID + " TEXT)";
    }



}
