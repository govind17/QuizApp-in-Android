package com.example.govindshukla.iquiz;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddQuestion_Activity extends AppCompatActivity {
    private Button sbtn;
    private EditText qidet,qdetails,a,b,c,d;
    private RadioButton ra,rb,rc,rd,onerb,tworb,tenrb,twentyrb;
    String qid,qd,opa,opb,opc,opd,ans,t,p;
    int dur,pnt;
    int f=0,df=0,pf=0;
    Intent i;
    SQLiteDatabase db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addquestion);
        sbtn=(Button)findViewById(R.id.subtn);
        qidet=(EditText)findViewById(R.id.qidett);
        qdetails=(EditText)findViewById(R.id.qdet);
        a=(EditText)findViewById(R.id.etA);
        b=(EditText)findViewById(R.id.etB);
        c=(EditText)findViewById(R.id.etC);
        d=(EditText)findViewById(R.id.etD);
        ra=(RadioButton)findViewById(R.id.opta);
        rb=(RadioButton)findViewById(R.id.optb);
        rc=(RadioButton)findViewById(R.id.optc);
        rd=(RadioButton)findViewById(R.id.optd);
        onerb=(RadioButton)findViewById(R.id.onerButton);
        tworb=(RadioButton)findViewById(R.id.tworButton);
        tenrb=(RadioButton)findViewById(R.id.tenrButton);
        twentyrb=(RadioButton)findViewById(R.id.twentyrButton);
        db=openOrCreateDatabase("IQUIZ.db", Context.MODE_PRIVATE,null);
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qid=qidet.getText().toString();
                qd=qdetails.getText().toString();
                opa=a.getText().toString();
                opb=b.getText().toString();
                opc=c.getText().toString();
                opd=d.getText().toString();
                if(qid.equals("")|| qd.equals("") || opa.equals("") || opb.equals("") || opc.equals("") || opd.equals("") )
                    Toast.makeText(AddQuestion_Activity.this, "Nothing should be left BLANK!", Toast.LENGTH_LONG).show();
                else if(df==0 || pf==0)
                    Toast.makeText(AddQuestion_Activity.this, "Points or Duration not selected!", Toast.LENGTH_LONG).show();
                else if(f==0)
                    Toast.makeText(AddQuestion_Activity.this, "Select Answer for the given question!", Toast.LENGTH_LONG).show();
                else{
                    try {
                        Cursor c = db.rawQuery("SELECT * FROM question_table WHERE QID='"+qid+"'",null);
                        if(c!=null && c.getCount()!=0)
                            Toast.makeText(getApplicationContext(), "The QID already exists!", Toast.LENGTH_LONG).show();
                        else{
                            db.execSQL("INSERT INTO question_table VALUES('"+qid+"','"+qd+"','"+opa+"','"+opb+"','"+opc+"','"+opd+"','"+ans+"','"+pnt+"','"+dur+"');");
                            Toast.makeText(AddQuestion_Activity.this, "Insertion success!", Toast.LENGTH_LONG).show();
                            f=0;
                            i=new Intent(getApplicationContext(),AdminActivity.class);
                            startActivity(i);
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void onRadiobtnSelect(View v){
        boolean ch=((RadioButton)v).isChecked();
        switch (v.getId()){
            case R.id.opta :
                if(ch) {
                    ans = "A";
                    f++;
                    break;
                }
            case R.id.optb :
                if(ch) {
                    ans = "B";
                    f++;
                    break;
                }
            case R.id.optc :
                if(ch) {
                    ans = "C";
                    f++;
                    break;
                }
            case R.id.optd :
                if(ch) {
                    ans = "D";
                    f++;
                    break;
                }
            case R.id.onerButton :
                if(ch){
                    dur=1;
                    df=1;
                    break;
                }
            case R.id.tworButton :
                if(ch){
                    dur=2;
                    df=1;
                    break;
                }
            case R.id.tenrButton :
                if(ch){
                    pnt=10;
                    pf=1;
                }
            case R.id.twentyrButton :
                if(ch){
                    pnt=20;
                    pf=1;
                }
            default :
                break;
        }
    }
}
