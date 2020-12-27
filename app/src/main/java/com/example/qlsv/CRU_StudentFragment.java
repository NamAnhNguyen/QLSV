package com.example.qlsv;

import android.app.DatePickerDialog;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.qlsv.models.Student;

import java.io.File;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.bloco.faker.Faker;

public class CRU_StudentFragment extends Fragment {

    public static Fragment fragment;
    SQLiteDatabase DB;
    EditText nameEditText, dobEditText, emailEditText, addressEditText;

    final Calendar calendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = view.findViewById(R.id.studentNameEditText);
        dobEditText = view.findViewById(R.id.studentDobEditText);
        emailEditText = view.findViewById(R.id.studentEmailEditText);
        addressEditText = view.findViewById(R.id.studentAddressEditText);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        int passingStudentId = this.getArguments().getInt("studentId");
        Log.v("studentIDDDDDDD", String.valueOf(passingStudentId));

        if (passingStudentId != -1) {
            File storagePath = getActivity().getApplication().getFilesDir();
            String DBpath = storagePath + "/" + "QLSV_DB";
            try {
                DB = SQLiteDatabase.openDatabase(DBpath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

                DB.rawQuery("select * from students where id = " + String.valueOf(passingStudentId), null);

                Cursor cursor = DB.rawQuery("select * from students", null);
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    Student student = new Student(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("dob")),
                            cursor.getString(cursor.getColumnIndex("email")),
                            cursor.getString(cursor.getColumnIndex("address"))
                    );
                    nameEditText.setText(student.name);
                    dobEditText.setText(student.dob);
                    emailEditText.setText(student.email);
                    addressEditText.setText(student.address);
                }

                DB.close();
                Toast toast = Toast.makeText(getContext(), "\nSUCCESS create DB + students table", Toast.LENGTH_LONG);
                toast.show();
            } catch (SQLException e) {
                Log.v("ERRORRRRRRRRRRRRR", e.getMessage());
                Toast toast = Toast.makeText(getContext(), "\nERROR " + e.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }

        }

        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CRU_StudentFragment.this.getContext(),
                        date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

                dobEditText.setText(android.text.format.DateFormat.format("dd/MM/yyyy", calendar.getTime()));
            }
        });

        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(CRU_StudentFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File storagePath = getActivity().getApplication().getFilesDir();
                String DBpath = storagePath + "/" + "QLSV_DB";
                try {
                    DB = SQLiteDatabase.openDatabase(DBpath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

                    Student student = new Student(
                            nameEditText.getText().toString(),
                            new Timestamp(format.parse(dobEditText.getText().toString()).getTime()).toString(),
                            emailEditText.getText().toString(),
                            addressEditText.getText().toString()
                    );

                    ContentValues rowValues = new ContentValues();
                    rowValues.put("name", student.name);
                    rowValues.put("email", student.email);
                    rowValues.put("dob", student.dob.toString());
                    rowValues.put("address", student.address);

                    if (passingStudentId == -1) {

                        DB.insert("students", null, rowValues);
                        Toast toast = Toast.makeText(getContext(), "\nSUCCESS create student", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        String[] whereArgs = {String.valueOf(passingStudentId)};
                        DB.update("students", rowValues, "id = ?", whereArgs);
                        Toast toast = Toast.makeText(getContext(), "\nSUCCESS edit student", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    DB.close();
                    NavHostFragment.findNavController(CRU_StudentFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);

                } catch (SQLException | ParseException e) {
                    Log.v("ERRORRRRRRRRRRRRR", e.getMessage());
                    Toast toast = Toast.makeText(getContext(), "\nERROR " + e.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}