package com.example.todoapp.Activities;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todoapp.Models.TodoItem;
import com.example.todoapp.R;
import com.example.todoapp.Repositories.TodoRepository;
import java.util.Locale;

public class TodoActivity extends BasedActivity {


    private TextView etDueDate;
    private EditText etTitle, etDescription, etTags;
    private TodoRepository todoRepository;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDueDate = findViewById(R.id.etDueDate);
        etTags = findViewById(R.id.etTags);
        Button btnAddTodo = findViewById(R.id.btnAddTodo);
        todoRepository = new TodoRepository(this);
        username = getIntent().getStringExtra("username");
        ImageButton btnBack = findViewById(R.id.btnBack);

        etDueDate.setOnClickListener(v -> showDatePickerDialog());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodoItem();
                finish();
            }
        });


    }


    private void addTodoItem() {

        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String dueDate = etDueDate.getText().toString();
        String tags = etTags.getText().toString();

        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty() || tags.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }

        TodoItem todoItem = new TodoItem(title, description, dueDate, tags, username,false);
        todoRepository.addTodoItem(todoItem);
        etTitle.setText("");
        etDescription.setText("");
        etDueDate.setText("");
        etTags.setText("");
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = String.format(Locale.US, "%d/%d/%d", dayOfMonth, month1 + 1, year1);
                    etDueDate.setText(date);
                },
                year, month, day);

        datePickerDialog.show();
    }

}
