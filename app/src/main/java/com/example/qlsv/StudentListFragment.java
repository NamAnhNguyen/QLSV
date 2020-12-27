package com.example.qlsv;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.qlsv.adapters.StudentListAdapter;
import com.example.qlsv.models.Student;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

import io.bloco.faker.Faker;

public class StudentListFragment extends Fragment {
    ListView listView;
    ArrayList<Student> students;
    SQLiteDatabase DB;
    public static Fragment fragment;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragment = this;

        listView = (ListView) view.findViewById(R.id.studentList);
        students = new ArrayList<Student>();
        String DBpath = getActivity().getApplication().getFilesDir() + "/" + "QLSV_DB";
        try {
            DB = SQLiteDatabase.openDatabase(DBpath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            Cursor cursor = DB.rawQuery("select * from students", null);
            cursor.moveToPosition(-1);

            while (cursor.moveToNext()) {
                students.add(new Student(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("dob")),
                        cursor.getString(cursor.getColumnIndex("email")),
                        cursor.getString(cursor.getColumnIndex("address"))
                ));
            }

            DB.close();
            Toast toast = Toast.makeText(getContext(), "\nSUCCESS access", Toast.LENGTH_LONG);
            toast.show();
        } catch (SQLException e) {
            Log.v("ERRORRRRRRRRRRRRR", e.getMessage());
            Toast toast = Toast.makeText(getContext(), "\nERROR " + e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }

        StudentListAdapter adapter = new StudentListAdapter(students, getContext());
        listView.setAdapter(adapter);

    }
}