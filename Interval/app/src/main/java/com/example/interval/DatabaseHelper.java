package com.example.interval;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "interval_app.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

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
                "type TEXT DEFAULT 'template'," + // NEW
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_SESSIONS_TABLE);
    }
    public void insertSession(int userId, String title, int focusTime, int restTime, String date, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(
                "INSERT INTO sessions (user_id, title, focus_time, rest_time, date, type) VALUES (?, ?, ?, ?, ?, ?)",
                new Object[]{userId, title, focusTime, restTime, date, type}
        );
        db.close();
    }
    public SessionModel getLatestSession(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM sessions WHERE user_id = ? AND type = 'template' ORDER BY id DESC LIMIT 1",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            int focus    = cursor.getInt(cursor.getColumnIndexOrThrow("focus_time"));
            int rest     = cursor.getInt(cursor.getColumnIndexOrThrow("rest_time"));
            cursor.close();
            db.close();
            return new SessionModel(title, focus, rest);
        }

        cursor.close();
        db.close();
        return null;
    }
    public Cursor getSessionHistory(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM sessions WHERE user_id = ? AND type = 'run' ORDER BY id DESC",
                new String[]{String.valueOf(userId)}
        );
    }
    public void updateSessionTitle(int sessionId, String newTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE sessions SET title = ? WHERE id = ?",
                new Object[]{newTitle, sessionId});
        db.close();
    }
    public void deleteSession(int sessionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM sessions WHERE id = ?", new Object[]{sessionId});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sessions");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}