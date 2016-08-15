package com.example.govindshukla.iquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayActivity extends AppCompatActivity {
    private Button abt,bbt,cbt,dbt;
    private TextView ques,qno,time,pnts;
    private String answ,name;
    private int score=0,numq=0,pts,tf=0;
    private String qd,opta,optb,optc,optd,ans;
    private int points,dur;
    private ArrayList<String> qlist;
    private Intent i;
    private CounterClass timer;
    private Login_Preference l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        l=new Login_Preference(getApplicationContext());
        Button quitbt,nextbt;
        TextView nametxt,logtxt;
        nametxt=(TextView)findViewById(R.id.nametext);
        name=l.getString("NAME");
        nametxt.setText(name);
        logtxt=(TextView)findViewById(R.id.txtlgout);
        logtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l.logout();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
        ques=(TextView)findViewById(R.id.questxt);
        qno=(TextView)findViewById(R.id.qnum);
        time=(TextView)findViewById(R.id.timetxt);
        pnts=(TextView)findViewById(R.id.pointstxt);
        abt=(Button)findViewById(R.id.optionA);
        bbt=(Button)findViewById(R.id.optionB);
        cbt=(Button)findViewById(R.id.optionC);
        dbt=(Button)findViewById(R.id.optionD);
        quitbt=(Button)findViewById(R.id.Quitbutton);
        nextbt=(Button)findViewById(R.id.nxtbutton);
        i=getIntent();
        qlist=i.getStringArrayListExtra("QLIST");
        setlayout();
        quitbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=new Intent(getApplicationContext(),ResultActivity.class);
                i.putExtra("NAME",name);
                i.putExtra("SCORE",""+score);
                startActivity(i);
            }
        });
        nextbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check("");
            }
        });
        logtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l.logout();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
    }

    public void onbtnansclick(View v){
        if(abt==v){
            check("A");
        }
        if(bbt==v){
            check("B");
        }
        if(cbt==v){
            check("C");
        }
        if(dbt==v){
            check("D");
        }
    }
    public void setlayout(){
        qno.setText("Q."+(numq+1)+":");
        setquestion(qlist.get(numq));
        ques.setText(qd);
        abt.setText(opta);
        bbt.setText(optb);
        cbt.setText(optc);
        dbt.setText(optd);
        pnts.setText(""+points);
        time.setText("0"+dur+":00");
        timer = new CounterClass(dur*60000, 1000);
        timer.start();
        tf=0;
        answ=ans;
        pts=points;
        numq++;
    }
    public void setquestion(String qid){
        SQLiteDatabase db=openOrCreateDatabase("IQUIZ.db", Context.MODE_PRIVATE,null);
        try{
            Cursor c = db.rawQuery("SELECT * from question_table WHERE QID='" + qid + "'", null);
            if (c.moveToFirst()) {
                do{
                    qd = c.getString(c.getColumnIndex("QUES_DETAILS"));
                    opta = c.getString(c.getColumnIndex("OPTA"));
                    optb = c.getString(c.getColumnIndex("OPTB"));
                    optc = c.getString(c.getColumnIndex("OPTC"));
                    optd = c.getString(c.getColumnIndex("OPTD"));
                    ans = c.getString(c.getColumnIndex("ANS"));
                    points = Integer.parseInt(c.getString(c.getColumnIndex("POINTS")));
                    dur = Integer.parseInt(c.getString(c.getColumnIndex("DURATION")));
                } while (c.moveToNext());
            }
        }catch(Exception e){
                e.printStackTrace();
        }
    }
    public void check(String givenans) {
        tf=1;
        timer.cancel();
        if(answ.equals(givenans)) {
            score += pts;
            Toast.makeText(PlayActivity.this, "Correct Answer!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(PlayActivity.this, "Wrong Answer!", Toast.LENGTH_SHORT).show();
        if(numq!=10)
            setlayout();
        else{
            i=new Intent(getApplicationContext(),ResultActivity.class);
            i.putExtra("NAME",name);
            i.putExtra("SCORE",""+score);
            startActivity(i);
        }
    }
    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }
        @Override
        public void onFinish() {
            if(tf!=1) {
                setlayout();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub

            long millis = millisUntilFinished;
            String hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(millis)));
            time.setText(hms);
        }
    }
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit Game!")
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
