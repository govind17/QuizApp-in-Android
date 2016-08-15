package com.example.govindshukla.iquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Set;

public class LoginActivity extends AppCompatActivity {
    private TextView tv;
    private Button bt;
    private EditText name,pass;
    private SQLiteDatabase db=null;
    private String et1,et2,namestr;
    private Intent i,in;
    private Login_Preference l;
    private static final String dbname="IQUIZ.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        l=new Login_Preference(getApplicationContext());
        name=(EditText)findViewById(R.id.editText);
        pass=(EditText)findViewById(R.id.editText2);
        tv=(TextView)findViewById(R.id.textView3);
        bt=(Button)findViewById(R.id.button);
        checkSavedPreferences();
        db=openOrCreateDatabase(dbname, Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS user_table(USERNAME VARCHAR PRIMARY KEY,NAME VARCHAR,PASSWORD VARCHAR,PREVBEST INTEGER,AVG REAL,COUNT INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS admin_table(ADMINNAME VARCHAR PRIMARY KEY,NAME VARCHAR,PASSWORD VARCHAR);");
        try {
            Cursor c = db.rawQuery("SELECT * FROM admin_table",null);
            if ( c==null || c.getCount() == 0) {
                db.execSQL("INSERT INTO admin_table VALUES('"+"admin"+"','"+"name"+"','"+"password"+"');");
                db.execSQL("INSERT INTO admin_table VALUES('"+"govindshukla17@gmail.com"+"','"+"Govind Shukla"+"','"+"12345"+"');");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            et1 = name.getText().toString();
                            et2 = pass.getText().toString();
                            int f=0;
                            if (!et1.equals("") && !et2.equals("")) {
                                try {
                                    Cursor c = db.rawQuery("SELECT * FROM admin_table WHERE ADMINNAME='" + et1 + "' AND PASSWORD='" + et2 + "'", null);
                                    if (c != null && c.getCount() != 0) {
                                        if (c.moveToFirst()){
                                            do {
                                                namestr = c.getString(c.getColumnIndex("NAME"));
                                            } while (c.moveToNext());
                                        }
                                        Toast.makeText(getApplicationContext(),"Welcome! "+namestr,Toast.LENGTH_LONG).show();
                                        Intent in = new Intent(getApplicationContext(), AdminActivity.class);
                                        f=1;
                                        l.setString("NAME",namestr);
                                        l.setbool("Loggedin",true);
                                        l.setbool("Admin",true);
                                        l.setString("Username",et1);
                                        startActivity(in);
                                    }
                                     c = db.rawQuery("SELECT * FROM user_table WHERE USERNAME='" + et1 + "' AND PASSWORD='" + et2 + "'", null);
                                    if (c != null && c.getCount() != 0) {
                                        if (c.moveToFirst()){
                                            do {
                                                namestr = c.getString(c.getColumnIndex("USERNAME"));

                                            } while (c.moveToNext());
                                        }
                                        Intent in = new Intent(getApplicationContext(), QuizDetails.class);
                                        f=1;
                                        l.setString("NAME",namestr);
                                        l.setbool("Loggedin",true);
                                        l.setbool("Admin",false);
                                        l.setString("Username",et1);
                                        startActivity(in);
                                    }
                                    if(f==0)
                                        Toast.makeText(getApplicationContext(),"Invalid Username or password",Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Username or Password cannot be blank!",Toast.LENGTH_LONG).show();
                            }
                        }
        });
    }
    public void text_Select(View v){
        in=new Intent(getApplicationContext(),SignUp.class);
        startActivity(in);
    }
    public void checkSavedPreferences(){
        if(l.getbool("Loggedin")){
            if(l.getbool("Admin")){
                i=new Intent(getApplicationContext(),AdminActivity.class);
                startActivity(i);
            }
            else{
                i=new Intent(getApplicationContext(),QuizDetails.class);
                startActivity(i);
            }
        }
    }
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit!")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }
}
