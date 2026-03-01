package com.example.interval;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "interval_app.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Enable foreign keys
        db.execSQL("PRAGMA foreign_keys=ON");

        // Users table
        String CREATE_USERS_TABLE = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password_hash TEXT NOT NULL" +
                ")";

        // Sessions table
        String CREATE_SESSIONS_TABLE = "CREATE TABLE sessions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "title TEXT NOT NULL," +
                "focus_time INTEGER," +
                "rest_time INTEGER," +
                "date TEXT," +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_SESSIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sessions");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}