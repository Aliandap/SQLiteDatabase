package com.market.sqlitedatabase;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Home extends AppCompatActivity
{
    EditText admno,name,tel, userinput;
    Spinner course;
    Button edit;
    //array to store spiner items
    String courses[]={"Android","PHP","Web Development","Ms Applications"};
    SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        edit= (Button) findViewById(R.id.edit);
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
            showmessage("Success", "Record saved!!",R.drawable.info);
            //clear edit text
            admno.setText("");
            name.setText("");
            tel.setText("");
        }
    }
    public void search(View v)
    {
        //get the admission No typed
        String admissionno=admno.getText().toString();
        //check if admissionno has been entered
        if(admissionno.equals(""))
        {
            //int icon=;
            showmessage("Error","Enter Admission Number",
                    R.drawable.error);
        }
        else
        {
            Cursor c=mydb.rawQuery
                    ("SELECT * FROM students WHERE Admno='"+admno.getText()+"'",null);
            if(c.moveToFirst())
            {

               String n= c.getString(1);
                String t=c.getString(3);
                String co=c.getString(2);
                String a=c.getString(0);
                showmessage("Results","Admno "+a+"\nName "+n+"\nTel "+t+"\nCourse "+co,R.drawable.info);





            }
            else
            {
                showmessage("Error","No Record Found",R.drawable.error);
            }

        }

    }
    public void edit(View v)
    {
        if(edit.getText().equals("Edit")) {
            //get prompts.xml view
            LayoutInflater li = LayoutInflater.from(getApplicationContext());
            View viewprompt = li.inflate(R.layout.prompt, null);
            AlertDialog.Builder myalert = new AlertDialog.Builder(this);
            myalert.setView(viewprompt);
            //find edittext
            userinput = (EditText) viewprompt.findViewById(R.id.userinput);

            myalert.setTitle("Input");
            myalert.setIcon(R.drawable.info);
            myalert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            myalert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Code
//                String adm=userinput.getText().toString();
//                //check if admno is black
//                if(adm.equals(""))
//                {
//                    showmessage("Error","Enter Admno",R.drawable.error);
//                }
//
//
//                else
//                {
                    Cursor c = mydb.rawQuery
                            ("SELECT * FROM students WHERE Admno='" + userinput.getText() + "'", null);
                    if (c.moveToFirst()) {

                        admno.setText(c.getString(0));
                        name.setText(c.getString(1));
                        tel.setText(c.getString(3));
                        //change button to update
                        edit.setText("Update");
                    } else {
                        showmessage("Error", "No Record Found", R.drawable.error);
                    }

                    //  }


                }
            });
            myalert.show();
        }
        else
        {
            Cursor c = mydb.rawQuery
                    ("SELECT * FROM students WHERE Admno='" + userinput.getText() + "'", null);
            if(c.moveToFirst())
            {
                mydb.execSQL
                        ("UPDATE students SET Name=' "+name.getText()+"',Course='"+course.getSelectedItem()+"',Phoneno=' "+tel.getText()+"' WHERE  Admno='"+admno.getText()+"'");
                showmessage("Success","Record Updated",R.drawable.info);
                edit.setText("Edit");
            }
        }



    }
    public void viewall(View v)
    {
        Cursor c=mydb.rawQuery("Select * from students",null);
        //check if no record in the table
        if(c.getCount()==0)
        {
            showmessage("No Record","No record Found!!",R.drawable.info);
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
            showmessage("Student Details",buffer.toString(),R.drawable.info);
        }


    }
    public void showmessage(String title,String message,int icon)
    {
        AlertDialog.Builder myalert=new AlertDialog.Builder(this);
        myalert.setTitle(title);
        myalert.setMessage(message);
      //  myalert.setMessage("Your Db has..",+message + "hhhh");
        myalert.setIcon(icon);
        myalert.setCancelable(false);
        myalert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        myalert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // Code
            }
        });

        myalert.show();

    }











}















