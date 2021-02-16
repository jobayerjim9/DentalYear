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
import com.dy.dentalyear.model.local.NotesModel;

import java.util.Calendar;
import java.util.Date;

import static com.dy.dentalyear.controller.helpers.Utils.getMonthForInt;
import static com.dy.dentalyear.model.constant.AppConstants.NOTE_TYPE;

public class WritingActivity extends AppCompatActivity {
    ActivityWritingBinding binding;
    NotesModel notesModel=new NotesModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_writing);
        binding.setData(notesModel);
        initView();

    }

    private void initView() {
        int type=getIntent().getIntExtra(NOTE_TYPE,0);
        Log.d("typeCheck",type+"");
        binding.getData().setType(type);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Calendar calendar = Calendar.getInstance();
        binding.setDateWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(WritingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateStr;
                        if (month<9) {
                            dateStr=year+"0"+(month+1)+""+dayOfMonth;
                        }
                        else {
                            dateStr=year+""+(month+1)+""+dayOfMonth;
                        }

                        String placeHolder=getMonthForInt(month).substring(0,3)+" "+dayOfMonth+","+year;
                        Log.d("dateCheck",dateStr);
                        binding.getData().setDate(dateStr);
                        binding.setDateWriting.setText(placeHolder);

                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        binding.saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.getData().getDate().trim().isEmpty()) {
                    Toast.makeText(WritingActivity.this, "Please Select A Date", Toast.LENGTH_SHORT).show();
                }
                else if (binding.getData().getTitle().trim().isEmpty()) {
                    Toast.makeText(WritingActivity.this, "Please Enter A Title", Toast.LENGTH_SHORT).show();
                } else if (binding.getData().getDesc().trim().isEmpty()) {
                    Toast.makeText(WritingActivity.this, "Please Enter Your Note/Goal", Toast.LENGTH_SHORT).show();
                }
                else {
                    createNote(binding.getData());
                }
            }
        });

    }

    private void createNote(NotesModel data) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(WritingActivity.this);
        databaseAccess.open();
        if (databaseAccess.createNote(data)) {
            Toast.makeText(this, "Note Saved Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(this, "Couldn't Save! Try Again!!", Toast.LENGTH_SHORT).show();
        }

    }
}