package com.bjcathay.qt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * 查询历史记录
 * Created by dengt on 15-6-24.
 */
public class SearchHistory {

    public static class Contracts implements BaseColumns {
        public static final String TABLE_NAME = "golf_history";
        public static final String STADIUM_FULL_NAME = "stadium_full_name";
        public static final String STADIUM_ADDRESS = "stadium_address";
        public static final String STADIUM_KEY = "stadium_key";
        public static final String STADIUM_ID = "stadium_id";
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "golf_search_history.db";
        private static final int DATABASE_VERSION = 1;
        private static final String CREATE_TABLE_HISTORY =
                "CREATE TABLE " + Contracts.TABLE_NAME + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY,"
                        + Contracts.STADIUM_KEY + " TEXT,"
                        + Contracts.STADIUM_ID + " TEXT,"
                        + Contracts.STADIUM_ADDRESS + " TEXT,"
                        + Contracts.STADIUM_FULL_NAME + " TEXT"
                        + ")";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_HISTORY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Contracts.TABLE_NAME);
            onCreate(db);
        }
    }

}
