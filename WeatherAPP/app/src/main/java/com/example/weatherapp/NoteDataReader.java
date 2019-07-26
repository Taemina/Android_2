package com.example.weatherapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.Closeable;

public class NoteDataReader implements Closeable {
    private Cursor cursor;
    private final SQLiteDatabase database;

    private final String[] notesAllColumn = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NOTE,
            DatabaseHelper.COLUMN_NOTE_TITLE
    };

    public NoteDataReader(SQLiteDatabase database) {
        this.database = database;
    }

    // Подготовить к чтению таблицу
    public void open() {
        query();
        cursor.moveToFirst();
    }

    public void close() {
        cursor.close();
    }

    // Перечитать таблицу
    public void refresh() {
        int position = cursor.getPosition();
        query();
        cursor.moveToPosition(position);
    }

    // создание запроса
    private void query() {
        cursor = database.query(DatabaseHelper.TABLE_NOTES,
                notesAllColumn, null, null,
                null, null, null);
    }

    // прочитать данные по определенной позиции
    public Note getPosition(int position) {
        cursor.moveToPosition(position);
        return cursorToNote();
    }

    // получить количество строк в таблице
    public int getCount() {
        return cursor.getCount();
    }

    // преобразователь курсора в объект
    private Note cursorToNote() {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setWeather(cursor.getString(1));
        note.setNameCity(cursor.getString(2));
        return note;
    }
}
