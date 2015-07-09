
package com.bjcathay.qt.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dengt on 15-3-11.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // 数据库版本号
    private static final int DATABASE_VERSION = 2;
    // 数据库名
    private static final String DATABASE_NAME = "golf.db";
    // 历史记录表名
    public static final String HISTORY_TABLE_NAME = "golf_history";
    public static final String STADIUM_FULL_NAME = "stadium_full_name";
    public static final String STADIUM_ADDRESS = "stadium_address";
    public static final String STADIUM_ID = "stadium_id";
    private static final String CREATE_TABLE_HISTORY =
            "CREATE TABLE " + HISTORY_TABLE_NAME + "("
                    + STADIUM_ID + " TEXT NOT NULL PRIMARY KEY,"
                    + STADIUM_ADDRESS + " TEXT,"
                    + STADIUM_FULL_NAME + " TEXT"
                    + ")";

    public static final String CITY_TABLE_NAME = "golf_city";
    public static final String CITY_NAME = "city_full_name";
    public static final String CITY_HOT = "city_hot";
    public static final String CITY_ID = "city_id";
    public static final String CITY_PROVINCE_ID = "province_id";
    private static final String CREATE_TABLE_CITY =
            "CREATE TABLE " + CITY_TABLE_NAME + "("
                    + CITY_ID + " TEXT NOT NULL PRIMARY KEY,"
                    + CITY_HOT + " TEXT,"
                    + CITY_PROVINCE_ID + " TEXT,"
                    + CITY_NAME + " TEXT"
                    + ")";

    public static final String PROVINCE_TABLE_NAME = "golf_province";
    public static final String PROVINCE_NAME = "province_full_name";
    public static final String PROVINCE_HOT = "province_hot";
    public static final String PROVINCE_ID = "province_id";

    private static final String CREATE_TABLE_PROVINCE =
            "CREATE TABLE " + PROVINCE_TABLE_NAME + "("
                    + PROVINCE_ID + " TEXT NOT NULL PRIMARY KEY,"
                    + PROVINCE_HOT + " TEXT,"
                    + PROVINCE_NAME + " TEXT"
                    + ")";

    public static final String PLAYER_TABLE_NAME = "golf_player";
    public static final String PLAYER_NAME = "player_name";
    public static final String PLAYER_NUMBER = "player_number";

    private static final String CREATE_TABLE_PLAYER =
            "CREATE TABLE " + PLAYER_TABLE_NAME + "("
                    + PLAYER_NAME + " TEXT,"
                    + PLAYER_NUMBER + " TEXT"
                    + ")";

    // 构造函数，调用父类SQLiteOpenHelper的构造函数
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
            int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // 数据库实际被创建是在getWritableDatabase()或getReadableDatabase()方法调用时
        Log.d("db", "DatabaseHelper Constructor");
        // CursorFactory设置为null,使用系统默认的工厂类
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 调用时间：数据库第一次创建时onCreate()方法会被调用

        // onCreate方法有一个 SQLiteDatabase对象作为参数，根据需要对这个对象填充表和初始化数据
        // 这个方法中主要完成创建数据库后对数据库的操作
        Log.d("db", "DatabaseHelper onCreate");

        // 构建创建表的SQL语句（可以从SQLite Expert工具的DDL粘贴过来加进StringBuffer中）
        StringBuffer lBuffer = new StringBuffer();
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_CITY);
        db.execSQL(CREATE_TABLE_PROVINCE);
        db.execSQL(CREATE_TABLE_PLAYER);
        // 即便程序修改重新运行，只要数据库已经创建过，就不会再进入这个onCreate方法

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        // 调用时间：如果DATABASE_VERSION值被改为别的数,系统发现现有数据库版本不同,即会调用onUpgrade

        // onUpgrade方法的三个参数，一个 SQLiteDatabase对象，一个旧的版本号和一个新的版本号
        // 这样就可以把一个数据库从旧的模型转变到新的模型
        // 这个方法中主要完成更改数据库版本的操作
        Log.d("db", "DatabaseHelper onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CITY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROVINCE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PLAYER_TABLE_NAME);
        onCreate(db);
        // 上述做法简单来说就是，通过检查常量值来决定如何，升级时删除旧表，然后调用onCreate来创建新表
        // 一般在实际项目中是不能这么做的，正确的做法是在更新数据表结构时，还要考虑用户存放于数据库中的数据不丢失
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // 每次打开数据库之后首先被执行

        Log.d("db", "DatabaseHelper onOpen");
    }
}
