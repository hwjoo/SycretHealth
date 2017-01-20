package com.muhanbit.sycrethealth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;

import static net.sqlcipher.database.SQLiteDatabase.loadLibs;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.c;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hwjoo on 2017. 1. 18..
 */

public class DBHandler extends SQLiteOpenHelper {
    private Context mContext;
    private static DBHandler instance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sycrethealth.db";
    public static final String TABLE_NAME = "records";

    public static final String COLUMN_ID ="id";
    public static final String COLUMN_STEP = "step";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_DATE = "date";

    public static final int ORDER_BY_ASC = 1;
    public static final int ORDER_BY_DESC = 2;


    private DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
        SQLiteDatabase.loadLibs(context);
    }
    public static DBHandler getInstance(Context context){
        if(instance == null){
            instance = new DBHandler(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    public long addRecord(Record record){
        ContentValues values = new ContentValues();
        values.put(COLUMN_STEP, record.getStep());
        values.put(COLUMN_START_TIME, record.getStartTime());
        values.put(COLUMN_END_TIME, record.getEndTime());
        values.put(COLUMN_DATE, record.getDate());

//        SQLiteDatabase db = this.getWritableDatabase(SycretWare.getDBKey());
        SQLiteDatabase db = this.getWritableDatabase("abcdefg");

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }
    public boolean deleteRecord(int primaryKey){
        int resultCode=-1;
        // select * from records where id = "1";
        String query = "SELECT * FROM "+TABLE_NAME + " WHERE "+ COLUMN_ID+"= \""
                +primaryKey+"\"";
        SQLiteDatabase db = this.getWritableDatabase("abcdefg");
        Cursor cursor = db.rawQuery(query, null);
        /*
         * id값으로 row를 조회한 뒤 실제 id와 조회된 id가 같은지 check 후
         *  delete 진행
         */
        if(cursor.moveToFirst()){
            int selectId = cursor.getInt(0);
            if(primaryKey == selectId) {
                resultCode = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(selectId)});
            }
            cursor.close();
            db.close();
        }
        return resultCode >-1;
    }
    public ArrayList<Record> selectAllRecord(int selectOrder){
        /*
         * 모든 record select
         */
        ArrayList<Record> selectRecords = new ArrayList<>();
        String query = "";
        switch (selectOrder){
            case ORDER_BY_ASC :
                query = "SELECT * FROM "+TABLE_NAME +" ORDER BY id ASC";
                break;
            case ORDER_BY_DESC :
                query = "SELECT * FROM "+TABLE_NAME +" ORDER BY id DESC";
                break;
        }
//        SQLiteDatabase db = this.getWritableDatabase(SycretWare.getDBKey());
        SQLiteDatabase db = this.getWritableDatabase("abcdefg");

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            Record record = new Record(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3), cursor.getString(4));
            selectRecords.add(record);
            while (cursor.moveToNext()){
                record = new Record(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
                Log.d("TEST","cursor while");
                selectRecords.add(record);
                record = null;
            }
        }
        return selectRecords;
    }
    public Record selectLastRecord(){
        //select * from tablename where id= "+"(select Max(id) from tablename)";
        String query ="SELECT * FROM "+TABLE_NAME+" WHERE id = "+"(SELECT MAX(id) FROM "+TABLE_NAME+")";
        SQLiteDatabase db = this.getWritableDatabase("abcdefg");
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            Record record = new Record(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                    cursor.getString(3), cursor.getString(4));
            return record;
        }else{
            return null;
        }

    }
}
