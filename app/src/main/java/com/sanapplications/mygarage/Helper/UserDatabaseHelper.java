package com.sanapplications.mygarage.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_LOGGED_IN = "logged_in";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_LOGGED_IN + "INTEGER, " +
                    COLUMN_PASSWORD + " TEXT)";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(String email, String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        if (!isTableExists(db, TABLE_NAME)) {

             db.execSQL(CREATE_TABLE);
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        //values.put(COLUMN_LOGGED_IN, 1);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);

        int count = cursor.getCount();
        cursor.close();

        if(count > 0){
            ContentValues values = new ContentValues();
            values.put(COLUMN_LOGGED_IN, 1);
        }

        return count > 0;
    }

    public boolean isUserLoggedIn() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Check if table exists
        if (!isTableExists(db, TABLE_NAME)) {
            return false;
        }

        // Check if column exists
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.getColumnIndex(COLUMN_LOGGED_IN) == -1) {
            cursor.close();
            return false;
        }
        cursor.close();

        // Check if any user is logged in
        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_LOGGED_IN + " = 1", null);
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        return false;
    }


}
