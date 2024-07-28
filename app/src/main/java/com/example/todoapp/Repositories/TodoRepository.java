package com.example.todoapp.Repositories;

import android.content.Context;

import com.example.todoapp.Database.TodoDatabaseHelper;
import com.example.todoapp.Models.TodoItem;

import java.util.List;

public class TodoRepository {
    private final TodoDatabaseHelper dbHelper;

    public TodoRepository(Context context) {
        dbHelper = new TodoDatabaseHelper(context);
    }

    public void addTodoItem(TodoItem todoItem) {
        dbHelper.insertTodoItem(todoItem);
    }

    public List<TodoItem> getTodoItems(String username) {
        return dbHelper.getUserTodoItems(username);
    }
    public void deleteTodoItem(Integer id){
        dbHelper.deleteTodoItem(id);
    }
    public void updateTodoItem(TodoItem todoItem){
        dbHelper.updateTodoItem(todoItem);
    }


}
