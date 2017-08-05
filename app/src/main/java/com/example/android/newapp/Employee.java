package com.example.android.newapp;

/**
 * Created by alhajery on 7/29/17.
 */

public class Employee {

    private String name ;

    private String age ;

    public Employee(String name , String age) {
        this.name = name;
        this.age = age ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){

        this.name = name ;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

}
