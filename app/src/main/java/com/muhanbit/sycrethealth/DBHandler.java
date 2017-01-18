package com.muhanbit.sycrethealth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hwjoo on 2017. 1. 18..
 */

public class DBHandler extends SQLiteOpenHelper {
    private static DBHandler instance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sycrethealth.db";
    public static final String TABLE_NAME = "records";

    public static final String COLUMN_ID ="id";
    public static final String COLUMN_STEP = "step";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_DATE = "date";

    private DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public static DBHandler getInstance(Context context, int version){
        if(instance == null){
            instance = new DBHandler(context, null, null, version);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE records(id INTEGER PRIMARY KEY, step VARCHAR(20), start_time VARCHAR(20), end_time VARCHAR(20), date VARCHAR(20))
        String CREATE_RECORDS_TABLE = "CREATE TABLE "+
                TABLE_NAME + "("
                + COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +COLUMN_STEP+" VARCHAR(20),"
                + COLUMN_START_TIME + " VARCHAR(20),"+COLUMN_END_TIME+" VARCHAR(20),"
                + COLUMN_DATE + " VARCHAR(20)" + ")";

        db.execSQL(CREATE_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DELET_RECORDS_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(DELET_RECORDS_TABLE);
        onCreate(db);
    }

    public void addRecord(Record record){
        ContentValues values = new ContentValues();
        values.put(COLUMN_STEP, record.getStep());
        values.put(COLUMN_START_TIME, record.getStartTime());
        values.put(COLUMN_END_TIME, record.getEndTime());
        values.put(COLUMN_DATE, record.getDate());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public boolean deleteRecord(Record record){
        boolean result = false;
        // select * from records where id = "1";
        String query = "SELECT * FROM "+TABLE_NAME + " WHERE "+ COLUMN_ID+"= \""
                +record.getId()+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Record selctRecord = new Record();
        /*
         * id값으로 row를 조회한 뒤 실제 id와 조회된 id가 같은지 check 후
         *  delete 진행
         */
        if(cursor.moveToFirst()){
            int selectId = cursor.getInt(0);
            if(record.getId() == selectId) {
                db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(selectId)});
            }
            cursor.close();
            result = true;
        }

        return result;

    }
}
