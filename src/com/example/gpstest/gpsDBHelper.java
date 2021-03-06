package com.example.gpstest;

import java.util.*;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class gpsDBHelper extends SQLiteOpenHelper {

	public static final String tableName = "gpsinfo";
	public static final String colID = "_id";
	public static final String colDate = "date";
	public static final String colGpsinfo1 = "gpsinfo1";
	public static final String colGpsinfo2 = "gpsinfo2";

	public gpsDBHelper(Context context, CursorFactory factory, int version) {
		super(context, tableName + ".db", factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + tableName + "(" + colID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + colDate + " TEXT, "
				+ colGpsinfo1 + " TEXT, " + colGpsinfo2 + " TEXT)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	/**
	 * CRUD 함수
	 */

	// 새로운 Contact 함수 추가
	public void addContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(colDate, contact.gettime());
		values.put(colGpsinfo1, contact.getgpsinfo1());
		values.put(colGpsinfo2, contact.getgpsinfo2());

		// Inserting Row
		db.insert(tableName, null, values);
		db.close(); // Closing database connection
	}

	// id 에 해당하는 Contact 객체 가져오기
	public Contact getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE "
				+ colID + " = " + id, null);
		if (cursor != null)
			cursor.moveToFirst();

		Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3));

		
//		contact.setID(Integer.parseInt(cursor.getString(0)));
//		contact.settime(cursor.getString(1));
//		contact.setgpsinfo1(cursor.getString(2));
//		contact.setgpsinfo2(cursor.getString(3));
		
		
		// return contact
//		 Contact contact = new Contact(1, "test", "test1", "test2");
		return contact;
	}

	// 모든 Contact 정보 가져오기
	public List<Contact> getAllContacts() {
		List<Contact> contactList = new ArrayList<Contact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + tableName;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.settime(cursor.getString(1));
				contact.setgpsinfo1(cursor.getString(2));
				contact.setgpsinfo2(cursor.getString(3));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// Contact 정보 업데이트
	public int updateContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(colDate, contact.gettime());
		values.put(colGpsinfo1, contact.getgpsinfo1());
		values.put(colGpsinfo2, contact.getgpsinfo2());
		// updating row
		return db.update(tableName, values, colID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
	}

	// Contact 정보 삭제하기
	public void deleteContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(tableName, colID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
		db.close();
	}

	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE " + tableName);
		db.execSQL("CREATE TABLE " + tableName + "(" + colID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + colDate + " TEXT, "
				+ colGpsinfo1 + " TEXT, " + colGpsinfo2 + " TEXT)");
		db.close();
	}

	// Contact 정보 숫자
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + tableName;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
