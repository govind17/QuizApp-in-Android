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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {
    private EditText et;
    private TextView qtv;
    private Spinner sp;
    private ArrayAdapter<String> qidlist;
    private SQLiteDatabase db=null;
    int qseldone=0;
    Login_Preference l;
    String qid;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button addbt,dltbt,updtbt,vbt;
        TextView tv,logtv;
        logtv=(TextView)findViewById(R.id.logtxt);
        l=new Login_Preference(getApplicationContext());
        db=openOrCreateDatabase("IQUIZ.db", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS question_table(QID VARCHAR PRIMARY KEY,QUES_DETAILS VARCHAR,OPTA VARCHAR,OPTB VARCHAR,OPTC VARCHAR,OPTD VARCHAR,ANS VARCHAR,POINTS INTEGER,DURATION INTEGER);");
        tv=(TextView)findViewById(R.id.adnametxt);
        qtv=(TextView)findViewById(R.id.qtxtv);
        addbt=(Button)findViewById(R.id.addbtn);
        dltbt=(Button)findViewById(R.id.dltbtn);
        updtbt=(Button)findViewById(R.id.updbtn);
        vbt=(Button)findViewById(R.id.viewbtn);
        tv.setText("HELLO ! "+l.getString("NAME"));
        sp=(Spinner)findViewById(R.id.qidspinner);
        fetchqid();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                qid=qidlist.getItem(i);
                qseldone=1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                            i = new Intent(getApplicationContext(), AddQuestion_Activity.class);
                            startActivity(i);
            }
        });
        dltbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qseldone==1) {
                    try {
                        Cursor c = db.rawQuery("SELECT * FROM question_table WHERE QID='"+qid+"'",null);
                        if(c!=null && c.getCount()!=0) {
                            db.execSQL("DELETE FROM question_table WHERE QID='" + qid + "'");
                            fetchqid();
                            Toast.makeText(AdminActivity.this, "Deletion success!", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(AdminActivity.this, "QID doesn't exists!", Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(AdminActivity.this, "Select QID!", Toast.LENGTH_LONG).show();
            }
        });
        vbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qseldone==1){
                    Cursor c=db.rawQuery("SELECT * FROM question_table WHERE QID='"+qid+"'",null);
                    String qidstr=null,qd=null,opa=null,opb=null,opc=null,opd=null,answ=null,pts=null,dur=null,tvstr;
                    if(c!=null && c.getCount()!=0) {
                        if (c.moveToFirst()) {
                            do {
                                qidstr = c.getString(c.getColumnIndex("QID"));
                                qd = c.getString(c.getColumnIndex("QUES_DETAILS"));
                                opa = c.getString(c.getColumnIndex("OPTA"));
                                opb = c.getString(c.getColumnIndex("OPTB"));
                                opc = c.getString(c.getColumnIndex("OPTC"));
                                opd = c.getString(c.getColumnIndex("OPTD"));
                                answ = c.getString(c.getColumnIndex("ANS"));
                                pts = c.getString(c.getColumnIndex("POINTS"));
                                dur = c.getString(c.getColumnIndex("DURATION"));
                            } while (c.moveToNext());
                        }
                        tvstr = "Question ID :" + qidstr + "\nQuestion Details :" + qd + "\nA." + opa + "\nB." + opb + "\nC." + opc + "\nD." + opd + "\nAnswer :" + answ + "\nTotal Points:" + pts + "\nDuration: " + dur + ":00";
                        qtv.setText(tvstr);
                    }
                    else
                        Toast.makeText(AdminActivity.this, "QID doesn't exists!", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(AdminActivity.this, "Select QUID!", Toast.LENGTH_LONG).show();
            }
        });
        updtbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(qseldone==0)
                    Toast.makeText(AdminActivity.this, "Enter QUID!", Toast.LENGTH_LONG).show();
                else {
                    Cursor c=db.rawQuery("SELECT QID FROM question_table WHERE QID='"+qid+"'",null);
                    if(c!=null && c.getCount()!=0) {
                        i = new Intent(getApplicationContext(), UpdatequestionActivity.class);
                        i.putExtra("QID", qid);
                        startActivity(i);
                    }
                    else
                        Toast.makeText(AdminActivity.this, "QID doesn't exists!", Toast.LENGTH_LONG).show();
                }
            }
        });
        logtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                l.logout();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
    }
    public void fetchqid(){
        qidlist = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        qidlist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try{
            Cursor c=db.rawQuery("SELECT QID FROM question_table",null);
            if(c!=null && c.getCount()!=0){
                if(c.moveToFirst()){
                    do{
                        qidlist.add(c.getString(c.getColumnIndex("QID")));
                    }while (c.moveToNext());
                }
                sp.setAdapter(qidlist);
            }
            else
                Toast.makeText(AdminActivity.this, "No Question in database!Please add questions first.", Toast.LENGTH_LONG).show();
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
