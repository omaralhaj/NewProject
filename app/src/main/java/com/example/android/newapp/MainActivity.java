package com.example.android.newapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    ListView listView ;

    MyAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     listView = (ListView) findViewById(R.id.list);

        Employee emp [] = new Employee[2];

        emp[0] = new Employee("Omar" , "20");
        emp[1] = new Employee("Ahmad" , "30");



        adapter = new MyAdapter(emp,this);
        listView.setAdapter(adapter);

    }







}
