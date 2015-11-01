package com.mycompany.antibes;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private final Context fContext;


    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fContext = context;

    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        ContentValues values = new ContentValues();
        Resources res = fContext.getResources();
        String[] myArray = res.getStringArray(R.array.my_array);
        for (String item : myArray){
            String[] split = item.split("\\s+");

            values.put(FeedReaderContract.FeedEntry.STOP, split[0]);
            values.put(FeedReaderContract.FeedEntry.LINE, split[1]);
            values.put(FeedReaderContract.FeedEntry.DIRECTION, split[2]);

            String schedule="";

            String[] stringArray = item.split("\\s+");
            int length = stringArray.length;

            for (int i = 3; i < length; i++) {
                if(schedule.trim().length()>0){
                    schedule += split[i]+" ";
                }else{
                    schedule = split[i]+" ";
                }
            }
            values.put(FeedReaderContract.FeedEntry.SCHEDULE, schedule);
            db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " ( " +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                    FeedReaderContract.FeedEntry.STOP + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.LINE + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.DIRECTION + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.SCHEDULE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;
}