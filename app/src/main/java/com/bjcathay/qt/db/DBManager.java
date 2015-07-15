
package com.bjcathay.qt.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bjcathay.qt.application.GApplication;
import com.bjcathay.qt.model.BookModel;
import com.bjcathay.qt.model.GetCitysModel;
import com.bjcathay.qt.model.GolfCourseModel;
import com.bjcathay.qt.model.ProvinceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengt on 15-3-11.
 */
public class DBManager {
    private GApplication fmApplication;
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private static DBManager dbManager;

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    private DBManager() {
        Log.d("db", "DBManager --> Constructor");
        fmApplication = GApplication.getInstance();
        helper = new DatabaseHelper(fmApplication);
        db = helper.getWritableDatabase();
    }

    public void add(GolfCourseModel stadium) {
        Log.d("db",
                "DBManager --> add" + stadium.getId() + "," + stadium.getName() + ","
                        + stadium.getAddress());

        try {
            db.beginTransaction(); // 开始事务
            db.execSQL("REPLACE INTO " + DatabaseHelper.HISTORY_TABLE_NAME
                    + " VALUES(?,?,?)", new Object[] {
                    String.valueOf(stadium.getId()),
                    stadium.getAddress(), stadium.getName()
            });
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // 1,　热门　
    // 2,非热门
    public void addCitys(List<GetCitysModel> stadiums) {
        Log.d("db", "DBManager --> add");

        try {
            db.beginTransaction(); // 开始事务
            for (GetCitysModel stadium : stadiums) {

                db.execSQL("INSERT INTO " + DatabaseHelper.CITY_TABLE_NAME
                        + " VALUES(?, ?, ?, ?)", new Object[] {
                        String.valueOf(stadium.getId()),
                        stadium.isHot() ? "1" : "2", String.valueOf(stadium.getProvinceId()),
                        stadium.getName()
                });
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addProvinces(List<ProvinceModel> stadiums) {
        Log.d("db", "DBManager --> add");

        try {
            db.beginTransaction(); // 开始事务
            for (ProvinceModel stadium : stadiums) {

                db.execSQL("INSERT INTO " + DatabaseHelper.PROVINCE_TABLE_NAME
                        + " VALUES(?, ?, ?)",
                        new Object[] {
                                String.valueOf(stadium.getId()), stadium.isHot() ? "1" : "2",
                                stadium.getName()
                        });
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addPlayers(List<BookModel> stadiums) {
        Log.d("db", "DBManager --> add");

        try {
            db.beginTransaction(); // 开始事务
            for (BookModel stadium : stadiums) {
                db.execSQL("REPLACE INTO " + DatabaseHelper.PLAYER_TABLE_NAME
                        + " VALUES(?, ?)",
                        new Object[] {
                                String.valueOf(stadium.getName()), stadium.getPhone()
                        });
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void updatePlayer(BookModel oldBook, BookModel newBook) {
        Log.d("db", "DBManager --> add");
        try {
            db.beginTransaction(); // 开始事务

            db.delete(DatabaseHelper.PLAYER_TABLE_NAME, DatabaseHelper.PLAYER_NAME + " =?",
                    new String[] {
                        oldBook.getName()
                    });

            db.execSQL("REPLACE INTO " + DatabaseHelper.PLAYER_TABLE_NAME
                    + " VALUES(?, ?)",
                    new Object[] {
                            String.valueOf(newBook.getName()), newBook.getPhone()
                    });
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }

    public void addPlayer(BookModel stadium) {
        Log.d("db", "DBManager --> add");
        try {
            db.beginTransaction(); // 开始事务

            db.execSQL("REPLACE INTO " + DatabaseHelper.PLAYER_TABLE_NAME
                    + " VALUES(?, ?)",
                    new Object[] {
                            String.valueOf(stadium.getName()), stadium.getPhone()
                    });
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void clearHistory() {
        // 清空历史数据表
        Log.d("db", "DBManager --> history clear");
        try {
            db.beginTransaction(); // 开始事务
            db.delete(DatabaseHelper.HISTORY_TABLE_NAME, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<GolfCourseModel> queryHistory() {
        Log.d("", "DBManager -->history query");
        List<GolfCourseModel> stadiums = new ArrayList<GolfCourseModel>();
        Cursor c = queryTheCursor();

        while (c.moveToNext()) {
            GolfCourseModel stadium = new GolfCourseModel();
            stadium.setId(Long.parseLong(c.getString(c.getColumnIndex(DatabaseHelper.STADIUM_ID))));
            stadium.setName(c.getString(c.getColumnIndex(DatabaseHelper.STADIUM_FULL_NAME)));
            stadium.setAddress(c.getString(c.getColumnIndex(DatabaseHelper.STADIUM_ADDRESS)));
            stadiums.add(stadium);
        }
        return stadiums;
    }

    public List<ProvinceModel> queryProvinces() {
        Log.d("", "DBManager -->history query");
        List<ProvinceModel> stadiums = new ArrayList<ProvinceModel>();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.PROVINCE_TABLE_NAME,
                null);

        while (c.moveToNext()) {
            ProvinceModel stadium = new ProvinceModel();
            stadium.setId(Long.parseLong(c.getString(c.getColumnIndex(DatabaseHelper.PROVINCE_ID))));
            stadium.setName(c.getString(c.getColumnIndex(DatabaseHelper.PROVINCE_NAME)));
            stadium.setHot("1".equals(c.getString(c.getColumnIndex(DatabaseHelper.PROVINCE_HOT))));
            stadiums.add(stadium);
        }
        return stadiums;
    }

    public List<GetCitysModel> queryCitys() {
        Log.d("", "DBManager -->history query");
        List<GetCitysModel> stadiums = new ArrayList<GetCitysModel>();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.CITY_TABLE_NAME,
                null);

        while (c.moveToNext()) {
            GetCitysModel stadium = new GetCitysModel();
            stadium.setId(Long.parseLong(c.getString(c.getColumnIndex(DatabaseHelper.CITY_ID))));
            stadium.setName(c.getString(c.getColumnIndex(DatabaseHelper.CITY_NAME)));
            stadium.setHot("1".equals(c.getString(c.getColumnIndex(DatabaseHelper.CITY_HOT))));
            stadium.setProvinceId(Long.parseLong(c.getString(c
                    .getColumnIndex(DatabaseHelper.CITY_PROVINCE_ID))));
            stadiums.add(stadium);
        }
        return stadiums;
    }

    public GetCitysModel getCity(String name) {
        GetCitysModel stadium=new GetCitysModel();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.CITY_TABLE_NAME + " WHERE "
                + DatabaseHelper.CITY_NAME + " = '" + name + "'",
                null);
        while (c.moveToNext()) {
            stadium.setId(Long.parseLong(c.getString(c.getColumnIndex(DatabaseHelper.CITY_ID))));
            stadium.setName(c.getString(c.getColumnIndex(DatabaseHelper.CITY_NAME)));
            stadium.setHot("1".equals(c.getString(c.getColumnIndex(DatabaseHelper.CITY_HOT))));
            stadium.setProvinceId(Long.parseLong(c.getString(c
                    .getColumnIndex(DatabaseHelper.CITY_PROVINCE_ID))));
            return stadium;
        }
        return stadium;
    }

    public List<BookModel> queryPlayers() {
        Log.d("", "DBManager -->history query");
        List<BookModel> stadiums = new ArrayList<BookModel>();
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.PLAYER_TABLE_NAME,
                null);
        while (c.moveToNext()) {
            BookModel stadium = new BookModel();
            stadium.setPhone(c.getString(c.getColumnIndex(DatabaseHelper.PLAYER_NUMBER)));
            stadium.setName(c.getString(c.getColumnIndex(DatabaseHelper.PLAYER_NAME)));
            stadiums.add(stadium);
        }
        return stadiums;
    }

    /**
     * query all persons, return cursor
     *
     * @return Cursor
     */
    private Cursor queryTheCursor() {
        Log.d("db", "DBManager --> queryTheCursor");
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.HISTORY_TABLE_NAME
                + " order by stadium_id limit 8",
                null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        Log.d("db", "DBManager --> closeDB");
        // 释放数据库资源
        db.close();
    }
}
