package me.itangqi.buildingblocks.utils.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oreo on 2016/7/20.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "base.db";
    private static final String TB_NAME_BASE = "baseInfoTb";
    private static final String TB_NAME_CAMERA = "cameraInfoTb";
    private static final String EXEC = "create table "
            + TB_NAME_BASE
            + " (_baseid integer primary key,basename varchar(80),baseposition varchar(80),basecode varchar(20),isshanxi integer)";
    private static final String EXEC1 = "create table "
            + TB_NAME_CAMERA
            + " (_cameraid integer primary key,cameraposition varchar(80),basecode varchar(20),ip varchar(20),port integer)";
    private static final int VERSION = 1;
    private SQLiteDatabase database;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.database = db;
        database.execSQL(EXEC);
        database.execSQL(EXEC1);

    }

    public void insert(String tbName, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        if (tbName == TB_NAME_BASE) {
            db.insert(TB_NAME_BASE, null, values);
        } else if (tbName == TB_NAME_CAMERA) {
            db.insert(TB_NAME_CAMERA, null, values);
        }
        db.close();
    }

    public void del(String tbName, String str) {
        if (database == null) {
            database = getWritableDatabase();
        }
        if (tbName == TB_NAME_BASE) {
            database.delete(TB_NAME_BASE, "basename=?", new String[] { str });
        } else if (tbName == TB_NAME_CAMERA) {
            database.delete(TB_NAME_CAMERA, "cameraposition=?",
                    new String[] { str });
        }

    }

    public void relationdel(String str) {// 级联删除
        if (database == null) {
            database = getWritableDatabase();
        }
        database.delete(TB_NAME_CAMERA, "basecode=?", new String[] { str });
    }

    public Cursor query(int IsShanXi) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TB_NAME_BASE, null, "isshanxi =" + IsShanXi,
                null, null, null, null);
        return cursor;
    }

    public Cursor query(String basecode) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TB_NAME_CAMERA, null, "basecode =" + "'"
                + basecode + "'", null, null, null, null);
        return cursor;
    }

    public String queryBaseCode(String basename) {
        String basecode = "";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TB_NAME_BASE, null, "basename =" + "'"
                + basename + "'", null, null, null, null);
        while (cursor.moveToNext()) {
            basecode = cursor.getString(cursor.getColumnIndex("basecode"));
        }
        return basecode;
    }

    public void close() {
        if (database != null)
            database.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
