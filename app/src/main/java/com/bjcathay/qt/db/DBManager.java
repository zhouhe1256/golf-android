package com.bjcathay.qt.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bjcathay.qt.application.GApplication;
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
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public void add(GolfCourseModel stadium) {
        Log.d("db", "DBManager --> add" + stadium.getId() + "," + stadium.getName() + "," + stadium.getAddress());

        try {
            // 采用事务处理，确保数据完整性
            db.beginTransaction(); // 开始事务
            db.execSQL("REPLACE INTO " + DatabaseHelper.HISTORY_TABLE_NAME
                    + " VALUES(?,?,?)", new Object[]{String.valueOf(stadium.getId()),
                    stadium.getAddress(), stadium.getName()});
            // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
            // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
            // 使用占位符有效区分了这种情况
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    //1,　热门　
    //2,非热门
    public void addCitys(List<GetCitysModel> stadiums) {
        Log.d("db", "DBManager --> add");

        try {
            // 采用事务处理，确保数据完整性
            db.beginTransaction(); // 开始事务
            for (GetCitysModel stadium : stadiums) {

                db.execSQL("INSERT INTO " + DatabaseHelper.CITY_TABLE_NAME
                        + " VALUES(?, ?, ?, ?)", new Object[]{String.valueOf(stadium.getId()),
                        stadium.isHot() ? "1" : "2", String.valueOf(stadium.getProvinceId()), stadium.getName()});
            }
            // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
            // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
            // 使用占位符有效区分了这种情况
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addProvinces(List<ProvinceModel> stadiums) {
        Log.d("db", "DBManager --> add");

        try {
            // 采用事务处理，确保数据完整性
            db.beginTransaction(); // 开始事务
            for (ProvinceModel stadium : stadiums) {

                db.execSQL("INSERT INTO " + DatabaseHelper.PROVINCE_TABLE_NAME
                        + " VALUES(?, ?, ?)", new Object[]{String.valueOf(stadium.getId()),stadium.isHot() ? "1" : "2", stadium.getName()
                        });
            }
            // 带两个参数的execSQL()方法，采用占位符参数？，把参数值放在后面，顺序对应
            // 一个参数的execSQL()方法中，用户输入特殊字符时需要转义
            // 使用占位符有效区分了这种情况
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
/*
    public void delete(List<StadiumModel> modelList) {
        Log.d("db", "DBManager --> delete");
        try {
            // 采用事务处理，确保数据完整性
            db.beginTransaction(); // 开始事务
            for (StadiumModel stadium : modelList) {
                db.delete(DatabaseHelper.HISTORY_TABLE_NAME, "pid = ? and cid = ? and stime = ? and etime = ? and _type = ?",
                        new String[]{Long.toString(stadium.getProgramId()), Long.toString(stadium.getColumnId()),
                                Long.toString(stadium.getStartAt()), Long.toString(stadium.getEndAt()), stadium.getType()}
                );
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }*/

    public void clearHistory() {
        //清空历史数据表
        Log.d("db", "DBManager --> history clear");
        try {
            // 采用事务处理，确保数据完整性
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
            stadium.setProvinceId(Long.parseLong(c.getString(c.getColumnIndex(DatabaseHelper.CITY_PROVINCE_ID))));
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
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.HISTORY_TABLE_NAME + " order by stadium_id limit 8",
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
