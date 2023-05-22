package com.sanapplications.mygarage.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sanapplications.mygarage.Model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "vehicle.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_VEHICLE = "CREATE TABLE vehicle (_id INTEGER PRIMARY KEY AUTOINCREMENT, make TEXT, model TEXT);";

    public VehicleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_VEHICLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS vehicle");
        onCreate(db);
    }

    public long addVehicle(Vehicle vehicle) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("make", vehicle.getMake());
        values.put("model", vehicle.getModel());
        return db.insert("vehicle", null, values);
    }

    public List<Vehicle> getAllVehicles() {
        SQLiteDatabase db = getReadableDatabase();
        List<Vehicle> vehicles = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM vehicle", null);
        if (cursor.moveToFirst()) {
            do {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                vehicle.setMake(cursor.getString(cursor.getColumnIndex("make")));
                vehicle.setModel(cursor.getString(cursor.getColumnIndex("model")));
                vehicles.add(vehicle);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return vehicles;
    }
}
