package com.example.qlsv.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.example.qlsv.CRU_StudentFragment;
import com.example.qlsv.R;
import com.example.qlsv.StudentListFragment;
import com.example.qlsv.models.Student;

import java.util.List;

public class StudentListAdapter extends BaseAdapter {
    List<Student> students;
    Context ctx;
    int count = 0;

    public StudentListAdapter(List<Student> students, Context ctx) {
        this.students = students;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.student_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.studentId = convertView.findViewById(R.id.studentId);
            viewHolder.studentName = convertView.findViewById(R.id.studentName);
            viewHolder.studentEmail = convertView.findViewById(R.id.studentEmail);
            viewHolder.delete = convertView.findViewById(R.id.delete);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        Student student = students.get(position);
        viewHolder.studentName.setText(student.name);
        viewHolder.studentId.setText(String.valueOf(student.id));
        viewHolder.studentEmail.setText(student.email);
        viewHolder.studentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("Student", student.name);

                Bundle bundle = new Bundle();
                bundle.putInt("studentId", student.id);

                Log.v("bundle", bundle.toString());

                NavHostFragment.findNavController(StudentListFragment.fragment)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase DB;
                String DBpath = ctx.getFilesDir() + "/" + "QLSV_DB";
                try {
                    DB = SQLiteDatabase.openDatabase(DBpath, null, SQLiteDatabase.CREATE_IF_NECESSARY);

                    String[] whereArgs = {String.valueOf(student.id)};
                    DB.delete("students", "id = ?", whereArgs);

                    DB.close();
                    Toast toast = Toast.makeText(ctx, "\nSUCCESS delete success", Toast.LENGTH_LONG);
                    toast.show();
                } catch (SQLException e) {
                    Log.v("ERRORRRRRRRRRRRRR", e.getMessage());
                    Toast toast = Toast.makeText(ctx, "\nERROR " + e.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView studentId;
        TextView studentName;
        TextView studentEmail;
        ImageButton delete;
    }
}
