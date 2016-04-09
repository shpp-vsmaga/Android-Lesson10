package com.shpp.sv.fragmentsnotepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class NotesDbHelper extends SQLiteOpenHelper {

    /*Database info*/
    private static final String DATABASE_NAME = "NotesDatabase.db";
    private static final int DATABASE_VERSION = 1;

    /*Table names*/
    private static final String TABLE_NOTES = "notes";

    /*Notes table columns*/
    private static final String KEY_NOTE_ID = "_id";
    private static final String KEY_NOTE_TEXT = "text";

    private static final String LOG_TAG = "svcom";
    private static  NotesDbHelper savedInstance;
    private ArrayList<String> dbArray;

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fillArray();
    }

    public static synchronized NotesDbHelper getInstance(Context context) {

        if (savedInstance == null) {
            savedInstance = new NotesDbHelper(context.getApplicationContext());
        }
        return savedInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES +
                "(" +
                KEY_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NOTE_TEXT + " TEXT" +
                ")";

        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        }
        onCreate(db);
    }

    public int addNote(String text){
        int id = -1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOTE_TEXT, text);
        id = (int) db.insert(TABLE_NOTES, null, values);
        dbArray.add(text);
        return id - 1;
    }

    public String getNote(int id){
        SQLiteDatabase db = getReadableDatabase();
        String noteText = "";
        Cursor cursor = db.query(TABLE_NOTES,
                new String[]{KEY_NOTE_TEXT},
                KEY_NOTE_ID + " = ?",
                new String[]{Integer.toString(id + 1)},
                null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            noteText = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TEXT));
        }
        return noteText;
    }

    public void updateNote(int id, String newText){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOTE_TEXT, newText);
        String where = KEY_NOTE_ID + " = " + (id + 1);

        db.update(TABLE_NOTES, values, where, null);
        updateNotesArray(id, newText);
    }

    public void deleteNote(int id){
        SQLiteDatabase db = getWritableDatabase();
        String where = KEY_NOTE_ID + " = " + (id + 1);
        db.delete(TABLE_NOTES, where, null);

        fixDBid(id);
        dbArray.remove(id);

    }

    private void fixDBid(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String UPDATE = String.format("UPDATE %s set %s = (%s - 1) WHERE %s > %s",
                TABLE_NOTES,
                KEY_NOTE_ID,
                KEY_NOTE_ID,
                KEY_NOTE_ID, id);
        db.execSQL(UPDATE);

        String UPDATE_MAX_ROWID = "UPDATE SQLITE_SEQUENCE set seq = (seq - 1) WHERE name = "
                + '"' + TABLE_NOTES + '"';
        db.execSQL(UPDATE_MAX_ROWID);
    }

    private void fillArray(){
        SQLiteDatabase db = getReadableDatabase();
        dbArray = new ArrayList<>();

        String QUERY = "SELECT * FROM " + TABLE_NOTES;
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                dbArray.add(cursor.getString(cursor.getColumnIndex(KEY_NOTE_TEXT)));
            }
            cursor.close();
        }
    }

    public ArrayList<String> getDbArray(){
        return dbArray;
    }

    private void updateNotesArray(int id, String newText){
        dbArray.set(id, newText);
    }

}
