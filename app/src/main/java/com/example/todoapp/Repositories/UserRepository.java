package com.example.todoapp.Repositories;

import android.content.Context;

import com.example.todoapp.Database.TodoDatabaseHelper;
import com.example.todoapp.Models.User;
import com.example.todoapp.Utilities.EncryptionUtil;

public class UserRepository {
    private TodoDatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new TodoDatabaseHelper(context);
    }

    public boolean addUser(User user) {
        user.setPassword(EncryptionUtil.encryptPassword(user.getPassword()));
        return dbHelper.insertUser(user) != -1;
    }

    public boolean validateUser(String username, String password) {
        String encryptedPassword = EncryptionUtil.encryptPassword(password);
        return dbHelper.checkUserCredentials(username, encryptedPassword);
    }
}
