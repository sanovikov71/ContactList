package com.gmail.sanovikov71.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {

	private static DBHelper instance = null;

	private static final String DATABASE_NAME = "myDB";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_CONTACT_LIST = "contact_list_table";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_FIRST_NAME = "firstname";
	public static final String COLUMN_SECOND_NAME = "secondname";
	public static final String COLUMN_PHONE = "phone";

	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DBHelper getInstance(Context context) {

		if (instance == null) {
			instance = new DBHelper(context.getApplicationContext());
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("SNOV", "is created");
		db.execSQL("create table " + TABLE_CONTACT_LIST + " (" + COLUMN_ID
				+ " integer primary key autoincrement, " + COLUMN_FIRST_NAME
				+ " text, " + COLUMN_SECOND_NAME + " text, " + COLUMN_PHONE
				+ " text);");

		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_FIRST_NAME, "Add New Item +");
		cv.put(DBHelper.COLUMN_SECOND_NAME, "");
		cv.put(DBHelper.COLUMN_PHONE, "");

		db.insert(DBHelper.TABLE_CONTACT_LIST, null, cv);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
