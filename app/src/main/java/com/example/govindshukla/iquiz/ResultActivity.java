package com.example.govindshukla.iquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView nametv,scoretv;
    private Intent i;
    private int prevbest,numofplays;
    private float avgbest=0;
    private SQLiteDatabase db=null;
    private Login_Preference l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView sctxt = (TextView) findViewById(R.id.scoretxt);
        TextView newbestxt = (TextView) findViewById(R.id.greettxt);
        TextView pretxt = (TextView) findViewById(R.id.prebesttxt);
        TextView avgtxt = (TextView) findViewById(R.id.avgbesttv);
        nametv=(TextView)findViewById(R.id.nametabletxt);
        scoretv=(TextView)findViewById(R.id.scoretabletxt);
        TextView lgtv = (TextView) findViewById(R.id.logtxt);
        Button qbtn = (Button) findViewById(R.id.qbutton);
        Button pabtn = (Button) findViewById(R.id.pabutton);
        l=new Login_Preference(getApplicationContext());
        i=getIntent();
        db=openOrCreateDatabase("IQUIZ.db", Context.MODE_PRIVATE,null);
        int score = Integer.parseInt(i.getStringExtra("SCORE"));
        sctxt.setText(""+ score);
        try{
            Cursor c=db.rawQuery("SELECT * FROM user_table WHERE USERNAME='"+l.getString("Username")+"'",null);
            if(c!=null && c.getCount()!=0){
                if(c.moveToFirst()){
                    do{
                        prevbest=Integer.parseInt(c.getString(c.getColumnIndex("PREVBEST")));
                        avgbest =Float.parseFloat(c.getString(c.getColumnIndex("AVG")));
                        numofplays=Integer.parseInt(c.getString(c.getColumnIndex("COUNT")));
                    }while (c.moveToNext());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        pretxt.setText(""+prevbest);
        numofplays++;
        avgbest =((numofplays-1)*avgbest+ score)/numofplays;
        avgtxt.setText(""+ avgbest);
        if(score >prevbest){
            newbestxt.setText("Congratulation!NEW BEST.");
            prevbest= score;
        }
        db.execSQL("UPDATE user_table SET PREVBEST='"+prevbest+"',AVG='"+ avgbest +"',COUNT='"+numofplays+"' WHERE USERNAME='"+l.getString("Username")+"'");
        drawtable();
        lgtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l.logout();
                i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
        qbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        pabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(getApplicationContext(),QuizDetails.class);
                startActivity(i);
            }
        });
    }
    public void drawtable(){
        try {
            String names = "", scores = "";
            Cursor c = db.rawQuery("SELECT USERNAME,NAME,PREVBEST FROM user_table ORDER BY PREVBEST DESC LIMIT 10", null);
            if (c != null && c.getCount() != 0) {
                if (c.moveToFirst()) {
                    do {
                        names += c.getString(c.getColumnIndex("NAME")) + "\n";
                        scores += c.getString(c.getColumnIndex("PREVBEST")) + "\n";
                    } while (c.moveToNext());
                }
                nametv.setText(names);
                scoretv.setText(scores);
            }
        }catch (Exception e){
            e.printStackTrace();
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
