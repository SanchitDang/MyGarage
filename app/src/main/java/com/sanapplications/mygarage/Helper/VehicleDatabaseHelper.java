package com.sanapplications.mygarage.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sanapplications.mygarage.Model.VehicleModel;

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

    public long addVehicle(VehicleModel vehicleModel) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("make", vehicleModel.getMake());
        values.put("model", vehicleModel.getModel());
        return db.insert("vehicle", null, values);
    }

    public List<VehicleModel> getAllVehicles() {
        SQLiteDatabase db = getReadableDatabase();
        List<VehicleModel> vehicleModels = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM vehicle", null);
        if (cursor.moveToFirst()) {
            do {
                VehicleModel vehicleModel = new VehicleModel();
                vehicleModel.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                vehicleModel.setMake(cursor.getString(cursor.getColumnIndex("make")));
                vehicleModel.setModel(cursor.getString(cursor.getColumnIndex("model")));
                vehicleModels.add(vehicleModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return vehicleModels;
    }
}
