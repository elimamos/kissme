package com.example.elisa.kissmekate;

/**
 * Created by e.mamos on 2017-08-22.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class DatabaseManager {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "Name";
    public static final String KEY_SURENAME = "Surename";
    public static final String KEY_TRANS= "Transcript";
    public static final String KEY_AB= "AB";
    public static final String KEY_AC= "AC";
    public static final String KEY_AD = "AD";
    public static final String KEY_AE = "AE";
    public static final String KEY_AF = "AF";
    public static final String KEY_BC = "BC";
    public static final String KEY_BD = "BD";
    public static final String KEY_BE = "BE";
    public static final String KEY_BF = "BF";
    public static final String KEY_CD = "CD";
    public static final String KEY_CE = "CE";
    public static final String KEY_CF = "CF";
    public static final String KEY_DE = "DE";
    public static final String KEY_DF = "DF";
    public static final String KEY_EF = "EF";
    private static final String TAG = "dbManager";
    private DatabaseHelper mDbHelper;
    public SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Users";
    public static final String SQLITE_TABLE = "AddedUsers";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_NAME + "," +
                    KEY_SURENAME + "," +
                    KEY_TRANS + "," +
                    KEY_AB + ","+
                    KEY_AC + ","+
                    KEY_AD + ","+
                    KEY_AE + ","+
                    KEY_AF + ","+
                    KEY_BC + ","+
                    KEY_BD + ","+
                    KEY_BE + ","+
                    KEY_BF + ","+
                    KEY_CD + ","+
                    KEY_CE + ","+
                    KEY_CF + ","+
                    KEY_DE + ","+
                    KEY_DF + ","+
                    KEY_EF +");";

    static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public DatabaseManager(Context ctx) {
        this.mCtx = ctx;
    }

    public DatabaseManager open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long addUser(String name, String surename,
                          int transcript, double []numbers) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_SURENAME, surename);
        initialValues.put(KEY_TRANS, transcript);
        initialValues.put(KEY_AB, numbers[0]);
        initialValues.put(KEY_AC, numbers[1]);
        initialValues.put(KEY_AD, numbers[2]);
        initialValues.put(KEY_AE, numbers[3]);
        initialValues.put(KEY_AF, numbers[4]);
        initialValues.put(KEY_BC, numbers[5]);
        initialValues.put(KEY_BD, numbers[6]);
        initialValues.put(KEY_BE, numbers[7]);
        initialValues.put(KEY_BF, numbers[8]);
        initialValues.put(KEY_CD, numbers[9]);
        initialValues.put(KEY_CE, numbers[10]);
        initialValues.put(KEY_CF, numbers[11]);
        initialValues.put(KEY_DE, numbers[12]);
        initialValues.put(KEY_DF, numbers[13]);
        initialValues.put(KEY_EF, numbers[14]);



        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllCountries() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public Cursor fetchUser(int index) throws SQLException {
        Cursor mCursor = null;
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                       KEY_NAME, KEY_SURENAME, KEY_TRANS, KEY_AB, KEY_AC, KEY_AD,KEY_AE,KEY_AF,KEY_BC,KEY_BD,KEY_BE,KEY_BF,KEY_CD,KEY_CE,KEY_CF,KEY_DF,KEY_DE,KEY_EF},
                    KEY_TRANS + " like '%" + index + "%'", null,
                    null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllRows() {
        if(mDb.isOpen()){ Log.d(TAG, "I AM OPEN");}

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_NAME, KEY_SURENAME, KEY_TRANS, KEY_AB, KEY_AC, KEY_AD,KEY_AE,KEY_AF,KEY_BC,KEY_BD,KEY_BE,KEY_BF,KEY_CD,KEY_CE,KEY_CF,KEY_DF,KEY_DE,KEY_EF},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertUser(String name, String surename,
                           int transcript, double []numbers) {

        addUser(name,surename,transcript,numbers);
        Log.d(TAG,"ADDED USER!");


    }


}

