package com.example.qlsv;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.qlsv.models.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.bloco.faker.Faker;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Faker faker = new Faker();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ArrayList<Student> students = new ArrayList<Student>();

//        for (int i = 0; i < 1; i++) {
//            students.add(
//                    new Student(faker.name.name(), new Timestamp(new Date().getTime()).toString(), faker.internet.email(), faker.address.streetAddress())
//            );
//        }

        File storagePath = getApplication().getFilesDir();
        String DBpath = storagePath + "/" + "QLSV_DB";
        try {
            DB = SQLiteDatabase.openDatabase(DBpath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            DB.execSQL("create table if not exists students("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "email text,"
                    + "dob string,"
                    + "address text);");

            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);

                ContentValues rowValues = new ContentValues();
                rowValues.put("name", student.name);
                rowValues.put("email", student.email);
                rowValues.put("dob", student.dob.toString());
                rowValues.put("address", student.address);

                DB.insert("students", null, rowValues);
            }

            DB.close();
            Toast toast = Toast.makeText(getApplicationContext(), "\nSUCCESS create DB + students table", Toast.LENGTH_LONG);
            toast.show();
        } catch (SQLException e) {
            Log.v("ERRORRRRRRRRRRRRR", e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), "\nERROR " + e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_createStudent) {
            NavHostFragment.findNavController(StudentListFragment.fragment)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}