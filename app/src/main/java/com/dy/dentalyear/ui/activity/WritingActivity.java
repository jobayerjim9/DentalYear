package com.dy.dentalyear.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.localdb.DatabaseAccess;
import com.dy.dentalyear.databinding.ActivityWritingBinding;
import com.dy.dentalyear.model.constant.AppConstants;
import com.dy.dentalyear.model.local.NotesModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.dy.dentalyear.controller.helpers.Utils.getMonthForInt;
import static com.dy.dentalyear.model.constant.AppConstants.NOTE_EDITABLE;
import static com.dy.dentalyear.model.constant.AppConstants.NOTE_TYPE;

public class WritingActivity extends AppCompatActivity {
    ActivityWritingBinding binding;
    private NotesModel notesModel = new NotesModel();
    private boolean editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_writing);

        initView();

    }

    private void initView() {
        int type = getIntent().getIntExtra(NOTE_TYPE, 0);
        editable = getIntent().getBooleanExtra(NOTE_EDITABLE, false);
        Log.d("typeCheck", type + "");
        if (editable) {
            notesModel = (NotesModel) getIntent().getSerializableExtra(AppConstants.NOTE_PARCELABLE);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            try {
                Date date = format.parse(notesModel.getDate());
                String placeHolder = getMonthForInt(date.getMonth()).substring(0, 3) + " " + date.getDate() + "," + (date.getYear() + 1900);
                binding.setDateWriting.setText(placeHolder);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        binding.setData(notesModel);
        binding.getData().setType(type);

        binding.back.setOnClickListener(v -> finish());
        Calendar calendar = Calendar.getInstance();
        binding.setDateWriting.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(WritingActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String dateStr;
                    if (month < 9) {
                        dateStr = year + "0" + (month + 1) + "" + dayOfMonth;
                    } else {
                        dateStr = year + "" + (month + 1) + "" + dayOfMonth;
                    }

                    String placeHolder = getMonthForInt(month).substring(0, 3) + " " + dayOfMonth + "," + year;
                    Log.d("dateCheck", dateStr);
                    binding.getData().setDate(dateStr);
                    binding.setDateWriting.setText(placeHolder);

                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
        binding.saveNote.setOnClickListener(v -> {
            if (binding.getData().getDate().trim().isEmpty()) {
                Toast.makeText(WritingActivity.this, "Please Select A Date", Toast.LENGTH_SHORT).show();
            } else if (binding.getData().getTitle().trim().isEmpty()) {
                Toast.makeText(WritingActivity.this, "Please Enter A Title", Toast.LENGTH_SHORT).show();
            } else if (binding.getData().getDesc().trim().isEmpty()) {
                Toast.makeText(WritingActivity.this, "Please Enter Your Note/Goal", Toast.LENGTH_SHORT).show();
            } else {
                if (editable) {
                    updateNote(binding.getData());
                } else {
                    createNote(binding.getData());
                }

            }
        });

    }

    private void updateNote(NotesModel data) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(WritingActivity.this);
        databaseAccess.open();
        if (databaseAccess.updateNote(data)) {
            Toast.makeText(this, "Note Saved Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Couldn't Save! Try Again!!", Toast.LENGTH_SHORT).show();
        }
        databaseAccess.close();

    }

    private void createNote(NotesModel data) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(WritingActivity.this);
        databaseAccess.open();
        if (databaseAccess.createNote(data)) {
            Toast.makeText(this, "Note Saved Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Couldn't Save! Try Again!!", Toast.LENGTH_SHORT).show();
        }
        databaseAccess.close();


    }
}