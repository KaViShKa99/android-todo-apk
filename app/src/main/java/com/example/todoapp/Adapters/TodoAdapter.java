package com.example.todoapp.Adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.todoapp.Models.TodoItem;
import com.example.todoapp.R;
import com.example.todoapp.Repositories.TodoRepository;

import java.util.List;
import java.util.Locale;

public class TodoAdapter extends ArrayAdapter<TodoItem> {
    private static final String TAG = "adapter";

    private final TodoRepository todoRepository;
    private final Context context;


    public TodoAdapter(Context context, List<TodoItem> todoItems,TodoRepository todoRepository) {
        super(context, 0, todoItems);
        this.todoRepository = todoRepository;
        this.context = context;


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItem todoItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        CheckBox cbCompleted = convertView.findViewById(R.id.cbCompleted);
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvDueDate = convertView.findViewById(R.id.tvDueDate);
        TextView tvTags = convertView.findViewById(R.id.tvTags);
        ImageButton btnDeleteTodo = convertView.findViewById(R.id.btnDeleteTodo);
        ImageButton btnEditTodo = convertView.findViewById(R.id.btnEditTodo);
        tvTitle.setText(todoItem.getTitle());
        tvDescription.setText(todoItem.getDescription());
        tvDueDate.setText(todoItem.getDueDate());
        tvTags.setText(todoItem.getTags());

        cbCompleted.setOnCheckedChangeListener(null);
        cbCompleted.setChecked(todoItem.isCompleted());


        cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todoItem.setCompleted(isChecked);
            todoRepository.updateTodoItem(todoItem);
            notifyDataSetChanged();
        });

        btnDeleteTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoRepository.deleteTodoItem(todoItem.getId());
                remove(todoItem);
                notifyDataSetChanged();
            }
        });

        btnEditTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEditDialog(todoItem);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private void showEditDialog(TodoItem todoItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Todo");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_todo, null);
        EditText etTitle = view.findViewById(R.id.etEditTitle);
        EditText etDescription = view.findViewById(R.id.etEditDescription);
        TextView etDueDate = view.findViewById(R.id.etEditDueDate);
        EditText etTags = view.findViewById(R.id.etEditTags);

        etTitle.setText(todoItem.getTitle());
        etDescription.setText(todoItem.getDescription());
        etDueDate.setText(todoItem.getDueDate());
        etTags.setText(todoItem.getTags());

        etDueDate.setOnClickListener(v -> showDatePickerDialog(etDueDate));


        builder.setView(view);
        builder.setPositiveButton("Save", (dialog, which) -> {
            String newTitle = etTitle.getText().toString().trim();
            String newDescription = etDescription.getText().toString().trim();
            String newTags = etTags.getText().toString().trim();


            if (newTitle.isEmpty() || newDescription.isEmpty() || etDueDate.getText().toString().isEmpty() || newTags.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            todoItem.setTitle(newTitle);
            todoItem.setDescription(newDescription);
            todoItem.setDueDate(etDueDate.getText().toString().trim());
            todoItem.setTags(newTags);

            todoRepository.updateTodoItem(todoItem);
            notifyDataSetChanged();
            Toast.makeText(context, "Todo item updated", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void showDatePickerDialog(TextView etDueDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year1, month1, dayOfMonth) -> {
                    String date = String.format(Locale.US, "%d/%d/%d", dayOfMonth, month1 + 1, year1);
                    etDueDate.setText(date);
                },
                year, month, day);

        datePickerDialog.show();
    }


}
