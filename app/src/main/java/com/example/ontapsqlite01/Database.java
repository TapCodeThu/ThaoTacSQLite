package com.example.ontapsqlite01;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME = "modelDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "model";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTIY = "quantity";


    public Database(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_QUANTIY + " INTEGER)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    //add model
    public void addModel(Model model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME,model.getName());
        cv.put(COLUMN_PRICE,model.getPrice());
        cv.put(COLUMN_QUANTIY,model.getQuantity());
        db.insert(TABLE_NAME,null,cv);
        db.close();
    }
    //update model
    public void updateModel(Model model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRICE,model.getPrice());
        cv.put(COLUMN_QUANTIY,model.getQuantity());
        db.update(TABLE_NAME,cv,COLUMN_NAME+" =?",new String[]{model.getName()});
        db.close();
    }
    //delete model
    public void deleteModel(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME + " = ?", new String[]{name});
        db.close();
    }
    // get all model from database

    public List<Model> getAllModel(){
        List<Model> modelList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                Model model = new Model();
                String name = cursor.getString(0);
                double price = cursor.getDouble(1);
                int quantity = cursor.getInt(2);
                modelList.add(new Model(name,price,quantity));


            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return modelList;
    }
}
