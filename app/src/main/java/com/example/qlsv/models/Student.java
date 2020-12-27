package com.example.qlsv.models;

import java.sql.Timestamp;
import java.util.Date;

public class Student {
    public int id;
    public String name;
    public String dob;
    public String email;
    public String address;

    public Student(String name, String dob, String email, String address) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.address = address;
    }

    public Student(int id, String name, String dob, String email, String address) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.address = address;
    }
}
