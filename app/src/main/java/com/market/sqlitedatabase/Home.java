package com.market.sqlitedatabase;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Home extends AppCompatActivity
{
    EditText admno,name,tel;
    Spinner course;
    //array to store spiner items
    String courses[]={"Android","PHP","Web Development","Ms Applications"};
    SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //find edittext
        admno= (EditText) findViewById(R.id.admno);
        name= (EditText) findViewById(R.id.name);
        tel= (EditText) findViewById(R.id.tel);
        //find spinner
        course= (Spinner) findViewById(R.id.course);
        //create array adapter
        ArrayAdapter<String> mydapter=new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,courses);
        course.setAdapter(mydapter);
        //create db if it does not exit or open db if it exist
        mydb=openOrCreateDatabase("school", Context.MODE_PRIVATE,null);
        //createtable

        mydb.execSQL("CREATE TABLE IF NOT EXISTS students(Admno INT,Name VARCHAR,Course VARCHAR,Phoneno VARCHAR);");
    }

    public void save(View v)
    {
       //GET values typed by the use
        String n=name.getText().toString();
        String a=admno.getText().toString();
        String c=course.getSelectedItem().toString();
        String t=tel.getText().toString();
        if(n.equals(""))
        {
            Toast.makeText(this,"Enter Name",Toast.LENGTH_SHORT).show();

        }
        else if(a.equals(""))
        {
            Toast.makeText(this,"Enter Adm no",Toast.LENGTH_SHORT).show();

        }
        else if(c.equals(""))
        {
            Toast.makeText(this," Enter Course",Toast.LENGTH_SHORT).show();

        }
       else {


            //sql statement to insert data in the table
            mydb.execSQL("INSERT INTO students VALUES('" + admno.getText() + "',  '" + name.getText() + "', '" + course.getSelectedItem() + "','" + tel.getText() + "' )");
            showmessage("Success", "Record saved!!");
            //clear edit text
            admno.setText("");
            name.setText("");
            tel.setText("");
        }
    }
    public void search(View v)
    {

    }
    public void edit(View v)
    {

    }
    public void viewall(View v)
    {
        Cursor c=mydb.rawQuery("Select * from students",null);
        //check if no record in the table
        if(c.getCount()==0)
        {
            showmessage("No Record","No record Found!!");
        }
        else
        {
            StringBuffer buffer=new StringBuffer();
            while (c.moveToNext())
            {
                buffer.append("Admno: "+c.getString(0)+"\n");
                buffer.append("Name: "+ c.getString(1)+"\n");
                buffer.append("Course: "+c.getString(2) +"\n");
                buffer.append("Phone No: "+ c.getString(3) +"\n");
                buffer.append("----------------"+"\n");
            }
            showmessage("Student Details",buffer.toString());
        }


    }
    public void showmessage(String title,String message)
    {
        AlertDialog.Builder myalert=new AlertDialog.Builder(this);
        myalert.setTitle(title);
        myalert.setMessage(message);
        myalert.setIcon(R.mipmap.ic_launcher);
        myalert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        myalert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        myalert.show();

    }











}















