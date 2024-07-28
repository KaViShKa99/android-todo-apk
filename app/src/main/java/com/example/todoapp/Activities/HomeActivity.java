package com.example.todoapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.Adapters.TodoAdapter;
import com.example.todoapp.Models.TodoItem;
import com.example.todoapp.R;
import com.example.todoapp.Repositories.TodoRepository;

import java.util.List;
import java.util.Locale;

public class HomeActivity  extends BasedActivity {

    private ListView lvTodos;
    private TextView tvEmptyMessage,note;
    private String username;
    private TodoRepository todoRepository;
    private TodoAdapter todoAdapter;
    private List<TodoItem> todoItems;
    private ImageButton btnFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lvTodos = findViewById(R.id.lvTodos);
        ImageButton addTodos = findViewById(R.id.addTodos);
        todoRepository = new TodoRepository(this);
        username = getIntent().getStringExtra("username");
        ImageButton btnBack = findViewById(R.id.btnBack);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        btnFilter = findViewById(R.id.btnFilter);
        note = findViewById(R.id.user_note);

        String welcomeMessage = String.format("Hi, %s", username);
        note.setText(welcomeMessage);

        loadTodoItems();

        addTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTodoActivity();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortOptionsDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTodoItems();
    }

    private void goTodoActivity(){
        Intent intent = new Intent(HomeActivity.this, TodoActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void loadTodoItems() {
        todoItems = todoRepository.getTodoItems(username);
        if (todoItems.isEmpty()) {
            btnFilter.setVisibility(View.GONE);
            lvTodos.setVisibility(View.GONE);
            tvEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            btnFilter.setVisibility(View.VISIBLE);
            lvTodos.setVisibility(View.VISIBLE);
            tvEmptyMessage.setVisibility(View.GONE);
        }
        todoAdapter = new TodoAdapter(this, todoItems, todoRepository);
        lvTodos.setAdapter(todoAdapter);
    }

//    private void loadTodoItems() {
//        todoItems = todoRepository.getTodoItems(username);
//        todoAdapter = new TodoAdapter(this, todoItems,todoRepository);
//        lvTodos.setAdapter(todoAdapter);
//    }

    private void sortTodoItemsByDueDate(boolean ascending) {
        todoItems.sort((todo1, todo2) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            try {
                if (ascending) {
                    return sdf.parse(todo1.getDueDate()).compareTo(sdf.parse(todo2.getDueDate()));
                } else {
                    return sdf.parse(todo2.getDueDate()).compareTo(sdf.parse(todo1.getDueDate()));
                }
            } catch (ParseException | java.text.ParseException e) {
                Toast.makeText(this, "Sort is not working", Toast.LENGTH_SHORT).show();
            }
            return 0;
        });

        todoAdapter.notifyDataSetChanged();
    }


    private void showSortOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By")
                .setItems(R.array.sort_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            sortTodoItemsByDueDate(true); // Ascending
                        } else if (which == 1) {
                            sortTodoItemsByDueDate(false); // Descending
                        }
                    }
                });
        builder.create().show();
    }
}
