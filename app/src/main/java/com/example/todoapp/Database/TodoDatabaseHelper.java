package com.example.todoapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.todoapp.Models.TodoItem;
import com.example.todoapp.Models.User;

import java.util.ArrayList;
import java.util.List;

public class TodoDatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_TODOS = "todos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DUE_DATE = "due_date";
    private static final String COLUMN_TAGS = "tags";
    private static final String COLUMN_USER = "user";
    private static final String COLUMN_COMPLETED = "completed";

    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        String createTodosTable = "CREATE TABLE " + TABLE_TODOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DUE_DATE + " TEXT, " +
                COLUMN_TAGS + " TEXT, " +
                COLUMN_USER + " TEXT, " +
                COLUMN_COMPLETED + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_USER + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USERNAME + "))";
        db.execSQL(createTodosTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
        onCreate(db);
    }

    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        return db.insert(TABLE_USERS, null, values);
    }

    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME}, COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{username, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void insertTodoItem(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, todoItem.getTitle());
        values.put(COLUMN_DESCRIPTION, todoItem.getDescription());
        values.put(COLUMN_DUE_DATE, todoItem.getDueDate());
        values.put(COLUMN_TAGS, todoItem.getTags());
        values.put(COLUMN_USER, todoItem.getUser());
        values.put(COLUMN_COMPLETED, todoItem.isCompleted() ? 1 : 0);
        db.insert(TABLE_TODOS, null, values);
    }

    public List<TodoItem> getUserTodoItems(String username) {
        List<TodoItem> todoItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODOS, null, COLUMN_USER + "=?", new String[]{username}, null, null, null);
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
            int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int dueDateIndex = cursor.getColumnIndex(COLUMN_DUE_DATE);
            int tagsIndex = cursor.getColumnIndex(COLUMN_TAGS);
            int completedIndex = cursor.getColumnIndex(COLUMN_COMPLETED);

            while (cursor.moveToNext()) {
                int id = idIndex != -1 ? cursor.getInt(idIndex) : -1;
                String title = titleIndex != -1 ? cursor.getString(titleIndex) : null;
                String description = descriptionIndex != -1 ? cursor.getString(descriptionIndex) : null;
                String dueDate = dueDateIndex != -1 ? cursor.getString(dueDateIndex) : null;
                String tags = tagsIndex != -1 ? cursor.getString(tagsIndex) : null;
                boolean isCompleted = completedIndex != -1 && cursor.getInt(completedIndex) == 1;

                todoItems.add(new TodoItem(id, title, description, dueDate, tags, username, isCompleted));
            }
            cursor.close();
        }
        return todoItems;
    }
    public void deleteTodoItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        if (doesTodoItemExist(id)) {
            db.delete(TABLE_TODOS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        }
    }

    public boolean doesTodoItemExist(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODOS, new String[]{COLUMN_ID}, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void updateTodoItem(TodoItem todoItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, todoItem.getTitle());
        values.put(COLUMN_DESCRIPTION, todoItem.getDescription());
        values.put(COLUMN_DUE_DATE, todoItem.getDueDate());
        values.put(COLUMN_TAGS, todoItem.getTags());
        values.put(COLUMN_USER, todoItem.getUser());
        values.put(COLUMN_COMPLETED, todoItem.isCompleted() ? 1 : 0);
        db.update(TABLE_TODOS, values, COLUMN_ID + "=?", new String[]{String.valueOf(todoItem.getId())});

    }

}
