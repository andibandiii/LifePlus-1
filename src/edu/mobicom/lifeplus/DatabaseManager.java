package edu.mobicom.lifeplus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

	public DatabaseManager(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create Task Table
		Log.i("DATABASE", "onCreate()");
		db.execSQL("CREATE TABLE " + Task.TABLE_NAME + " (" + Task.COLUMN_ID
				+ " integer PRIMARY KEY autoincrement," + Task.COLUMN_NAME
				+ " text," + Task.COLUMN_DESC + " text,"
				+ Task.COLUMN_DIFFICULTY + " integer," + Task.COLUMN_DURATION
				+ " text," + Task.COLUMN_TIME + " text," + Task.COLUMN_DATE
				+ " date," + Task.COLUMN_TYPE + " text,"
				+ Task.COLUMN_GENERATED + " boolean," + Task.COLUMN_CHECKED
				+ " boolean," + Task.COLUMN_STATUS + " boolean)");

		db.execSQL("CREATE TABLE " + Indulgence.TABLE_NAME + " ("
				+ Task.COLUMN_ID + " integer PRIMARY KEY autoincrement,"
				+ Indulgence.COLUMN_NAME + " text," + Indulgence.COLUMN_DESC
				+ " text," + Indulgence.COLUMN_PRICE + " integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void editTask(int id, Task t) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(Task.COLUMN_NAME, t.getName());
		values.put(Task.COLUMN_DESC, t.getDesc());
		values.put(Task.COLUMN_DURATION, t.getDuration());
		values.put(Task.COLUMN_TIME, t.getTime());
		values.put(Task.COLUMN_DIFFICULTY, t.getDifficulty());

		if (t.getGenerated() == true)
			values.put(Task.COLUMN_GENERATED, 1);
		else if (t.getGenerated() == false)
			values.put(Task.COLUMN_GENERATED, 0);

		if (t.getChecked() == true)
			values.put(Task.COLUMN_CHECKED, 1);
		else if (t.getChecked() == false)
			values.put(Task.COLUMN_CHECKED, 0);

		values.put(Task.COLUMN_TYPE, t.getType());

		if (t.getStatus() == true)
			values.put(Task.COLUMN_STATUS, 1);
		else if (t.getStatus() == false)
			values.put(Task.COLUMN_STATUS, 0);

		if (t.getType() == 2)
			values.put(Task.COLUMN_DATE, t.getDate().toString());

		db.update(Task.TABLE_NAME, values, Task.COLUMN_ID + "= ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	public void setDone(int id, Boolean Done) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(Task.COLUMN_STATUS, Done);

		db.update(Task.TABLE_NAME, values, Task.COLUMN_ID + "= ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	public void addTask(Task t) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(Task.COLUMN_NAME, t.getName());
		values.put(Task.COLUMN_DESC, t.getDesc());
		values.put(Task.COLUMN_DURATION, t.getDuration());
		values.put(Task.COLUMN_TIME, t.getTime());
		values.put(Task.COLUMN_DIFFICULTY, t.getDifficulty());

		if (t.getGenerated() == true)
			values.put(Task.COLUMN_GENERATED, 1);
		else if (t.getGenerated() == false)
			values.put(Task.COLUMN_GENERATED, 0);

		if (t.getChecked() == true)
			values.put(Task.COLUMN_CHECKED, 1);
		else if (t.getChecked() == false)
			values.put(Task.COLUMN_CHECKED, 0);

		values.put(Task.COLUMN_TYPE, t.getType());

		if (t.getStatus() == true)
			values.put(Task.COLUMN_STATUS, 1);
		else if (t.getStatus() == false)
			values.put(Task.COLUMN_STATUS, 0);

		if (t.getType() == 2)
			values.put(Task.COLUMN_DATE, t.getDate().toString());

		db.insert(Task.TABLE_NAME, null, values);
		db.close();
	}

	public void deleteTask(int id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(Task.TABLE_NAME, Task.COLUMN_ID + " =? ",
				new String[] { String.valueOf(id) });
		db.close();
	}

	public void setAsDone(int id) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(Task.COLUMN_STATUS, 1);

		db.update(Task.TABLE_NAME, values, Task.COLUMN_ID + "= ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	public ArrayList<Task> getDailyQuests() {

		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(Task.TABLE_NAME, /*
											 * new String[] { Task.COLUMN_NAME,
											 * Task.COLUMN_DESC,
											 * Task.COLUMN_TIME,
											 * Task.COLUMN_STATUS }
											 */null, Task.COLUMN_TYPE + " = ?",
				new String[] { "1" }, null, null, null);

		ArrayList<Task> daily = new ArrayList<Task>();

		if (c.moveToFirst()) {
			do {
				int id = c.getInt(c.getColumnIndex(Task.COLUMN_ID));
				String name = c.getString(c.getColumnIndex(Task.COLUMN_NAME));
				String desc = c.getString(c.getColumnIndex(Task.COLUMN_DESC));
				int difficulty = c.getInt(c
						.getColumnIndex(Task.COLUMN_DIFFICULTY));
				String duration = c.getString(c
						.getColumnIndex(Task.COLUMN_DURATION));
				String time = c.getString(c.getColumnIndex(Task.COLUMN_TIME));

				int generated = c.getInt(c
						.getColumnIndex(Task.COLUMN_GENERATED));
				boolean isGenerated;
				if (generated == 1) {
					isGenerated = true;
				} else {
					isGenerated = false;
				}

				int checked = c.getInt(c.getColumnIndex(Task.COLUMN_CHECKED));
				boolean isChecked;
				if (checked == 1) {
					isChecked = true;
				} else {
					isChecked = false;
				}

				int status = c.getInt(c.getColumnIndex(Task.COLUMN_STATUS));
				boolean isFinished;
				if (status == 1) {
					isFinished = true;
				} else {
					isFinished = false;
				}

				daily.add(new Task(id, name, desc, difficulty, duration, time,
						1, isGenerated, isChecked, isFinished));
			} while (c.moveToNext());
		}

		c.close();
		db.close();

		return daily;
	}

	public ArrayList<Task> getTodoList() {

		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(Task.TABLE_NAME, null, Task.COLUMN_TYPE + " = ?",
				new String[] { "2" }, null, null, null);

		ArrayList<Task> todo = new ArrayList<Task>();

		if (c.moveToFirst()) {
			do {

				int id = c.getInt(c.getColumnIndex(Task.COLUMN_ID));
				String name = c.getString(c.getColumnIndex(Task.COLUMN_NAME));
				String desc = c.getString(c.getColumnIndex(Task.COLUMN_DESC));
				int difficulty = c.getInt(c
						.getColumnIndex(Task.COLUMN_DIFFICULTY));
				String duration = c.getString(c
						.getColumnIndex(Task.COLUMN_DURATION));
				String time = c.getString(c.getColumnIndex(Task.COLUMN_TIME));

				int checked = c.getInt(c.getColumnIndex(Task.COLUMN_CHECKED));
				boolean isChecked;
				if (checked == 1) {
					isChecked = true;
				} else {
					isChecked = false;
				}

				int status = c.getInt(c.getColumnIndex(Task.COLUMN_STATUS));
				boolean isFinished;
				if (status == 1) {
					isFinished = true;
				} else {
					isFinished = false;
				}

				Date date = null;

				try {
					date = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH)
							.parse(c.getString(c
									.getColumnIndex(Task.COLUMN_DATE)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				todo.add(new Task(id, name, desc, difficulty, duration, date,
						time, 2, false, isChecked, isFinished));
			} while (c.moveToNext());
		}

		c.close();
		db.close();

		return todo;
	}

	public void setChecked(int id, boolean isChecked) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(Task.COLUMN_CHECKED, isChecked);

		db.update(Task.TABLE_NAME, values, Task.COLUMN_ID + "= ?",
				new String[] { String.valueOf(id) });
		db.close();
	}
}
