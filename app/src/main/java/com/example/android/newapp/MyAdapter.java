package com.example.android.newapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by alhajery on 7/29/17.
 */

public class MyAdapter extends BaseAdapter {

    private Employee [] emp ;
    Context context;



    public MyAdapter(Employee [] x, Context c){

       emp = x ;


     context = c ;

    }
    public int getCount() {
        return emp.length;
    }

    @Override
    public Employee getItem(int i) {

        return emp[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_item , viewGroup, false);

        TextView name = v.findViewById(R.id.n);
        TextView a = v.findViewById(R.id.a);

        name.setText(getItem(i).getName().toString());
        a.setText(getItem(i).getAge().toString());

        return v ;
    }
}
