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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizDetails extends AppCompatActivity {
    private TextView tv,tv1,tv2,tv3,tv4,tv5,logtv;
    private ImageButton ib;
    private int f=1;
    private String name;
    private SQLiteDatabase db=null;
    private ArrayList<String> qlist=new ArrayList<String>();
    private Login_Preference l;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_details);
        i=getIntent();
        l=new Login_Preference(getApplicationContext());
        tv=(TextView)findViewById(R.id.nametxt);
        name=l.getString("NAME");
        tv.setText("HELLO ! "+name);
        logtv=(TextView)findViewById(R.id.logtxt);
        tv1=(TextView)findViewById(R.id.dtv1);
        tv2=(TextView)findViewById(R.id.dtv2);
        tv3=(TextView)findViewById(R.id.dtv3);
        tv4=(TextView)findViewById(R.id.dtv4);
        tv5=(TextView)findViewById(R.id.dtv5);
        ib=(ImageButton)findViewById(R.id.imageButton);
        tv1.setText("1.Each question will be visible for specific amount of time.");
        tv2.setText("2.Each question carries some points which you can earn by giving the correct answer");
        tv3.setText("3.Once the next button is pressed the contestant can't go back or re-correct the previous answer given or skipped question.");
        tv4.setText("4.Result will be shown once the contestant either quits or finishes the quiz.NOT in case of logout!");
        tv5.setText("5.The points scored will be stored to keep a record of performance analysis.");
        db=openOrCreateDatabase("IQUIZ.db", Context.MODE_PRIVATE,null);
        try{
            Cursor c=db.rawQuery("SELECT QID FROM question_table ORDER BY RANDOM() LIMIT 10",null);
            if(c!=null && c.getCount()!=0){
                if(c.moveToFirst()){
                    do{
                        qlist.add(c.getString(c.getColumnIndex("QID")));
						f++;
                    }while (c.moveToNext());
                    
                }
            }
            else{
                Toast.makeText(QuizDetails.this, "Its embarrassing,no question in the database.Please contact Administrator. ", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(f==10){
                    Intent in=new Intent(getApplicationContext(),PlayActivity.class);
                    in.putStringArrayListExtra("QLIST",qlist);
                    startActivity(in);
                }
                else{
                    Toast.makeText(QuizDetails.this, "Not enough Questions in the Database to Play.PLease contact administrator!", Toast.LENGTH_LONG).show();
                }
            }
        });
        logtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l.logout();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
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
