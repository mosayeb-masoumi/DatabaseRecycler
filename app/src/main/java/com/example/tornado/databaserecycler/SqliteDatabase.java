package com.example.tornado.databaserecycler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tornado on 12/6/2018.
 */

public class SqliteDatabase  extends SQLiteOpenHelper {
    private    static final int DATABASE_VERSION =    5;
    private    static final String    DATABASE_NAME = "product";
    private    static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "_id";
    private static final   String COLUMN_PRODUCTNAME = "productname";
    private static final String COLUMN_QUANTITY = "quantity";
    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE    TABLE " + TABLE_PRODUCTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PRODUCTNAME + " TEXT," + COLUMN_QUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }


    public List<ModelProduct> listProducts(){
        String sql = "select * from " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        List<ModelProduct> storeModelProducts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                int quantity = Integer.parseInt(cursor.getString(2));
                storeModelProducts.add(new ModelProduct(id, name, quantity));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeModelProducts;
    }



    //add data in database
    public void addProduct(ModelProduct modelProduct){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, modelProduct.getName());
        values.put(COLUMN_QUANTITY, modelProduct.getQuantity());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
    }


    //update saved data
    public void updateProduct(ModelProduct modelProduct){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, modelProduct.getName());
        values.put(COLUMN_QUANTITY, modelProduct.getQuantity());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PRODUCTS, values, COLUMN_ID    + "    = ?", new String[] { String.valueOf(modelProduct.getId())});
    }



    //find saved data
    public ModelProduct findProduct(String name){
        String query = "Select * FROM "    + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " = " + "name";
        SQLiteDatabase db = this.getWritableDatabase();
        ModelProduct mModelProduct = null;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String productName = cursor.getString(1);
            int productQuantity = Integer.parseInt(cursor.getString(2));
            mModelProduct = new ModelProduct(id, productName, productQuantity);
        }
        cursor.close();
        return mModelProduct;
    }


    //delete saved data
    public void deleteProduct(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID    + "    = ?", new String[] { String.valueOf(id)});
    }
}
