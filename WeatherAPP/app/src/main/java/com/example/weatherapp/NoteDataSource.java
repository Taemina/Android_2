package com.example.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.io.Closeable;
public class NoteDataSource implements Closeable{
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private NoteDataReader noteDataReader;

    public NoteDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Открывает базу данных
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        // создать читателя и открыть его
        noteDataReader = new NoteDataReader(database);
        noteDataReader.open();
    }

    // Закрыть базу данных
    public void close() {
        noteDataReader.close();
        dbHelper.close();
    }

    // Добавить новую запись
    public Note addNote(String nameCity, String weather) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOTE, weather);
        values.put(DatabaseHelper.COLUMN_NOTE_TITLE, nameCity);
        // Добавление записи
        long insertId = database.insert(DatabaseHelper.TABLE_NOTES, null,
                values);
        Note newNote = new Note();
        newNote.setNameCity(nameCity);
        newNote.setWeather(weather);
        newNote.setId(insertId);
        return newNote;
    }

    // вернуть читателя
    public NoteDataReader getNoteDataReader() {
        return noteDataReader;
    }
}