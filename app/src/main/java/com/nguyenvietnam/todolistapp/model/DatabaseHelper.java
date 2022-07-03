package com.nguyenvietnam.todolistapp.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public DatabaseHelper(@Nullable Context context) {
        super(context, "TaskManagement.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String s = "CREATE TABLE task (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " title TEXT," +
                " date TEXT," +
                " time TEXT," +
                " status INTEGER)";
        db.execSQL(s);
    }

    public List<Task> getAllTask() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM task";
        Cursor rs = db.rawQuery(sql, null);
        List<Task> list = new ArrayList<>();
        while(rs!=null && rs.moveToNext()) {
            Task task = new Task(rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4));
            task.setId(rs.getInt(0));
            list.add(task);
        }
        return list;
    }

    public void addTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO task (title, date, time, status) VALUES (" +
                "?,?,?,?)";
        String[] selectionArgs = {task.title, task.date, task.time, task.getStatus()+""};
        db.execSQL(sql, selectionArgs);
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE task " +
                "SET title = ?," +
                " date = ?," +
                " time = ?," +
                " status = ?" +
                " WHERE id = ?";
        String[] selectionArgs = {task.title, task.date, task.time, task.getStatus()+"", task.getId()+""};
        db.execSQL(sql, selectionArgs);
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM task WHERE id = ?";
        String[] selectionArgs = {task.getId()+""};
        db.execSQL(sql, selectionArgs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
