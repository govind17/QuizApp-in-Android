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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdatequestionActivity extends AppCompatActivity {
    private RadioButton onerb,tworb,tenrb,twentyrb;
    private EditText qd,opa,opb,opc,opd,ans;
    private Button qdbt,aopbt,bopbt,copbt,dopbt,ansbt,durbt,pntbt,dbt;
    private String qid,dur,pnt;
    private int df=0,pf=0;
    private SQLiteDatabase db=null;
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatequestion);
        db=openOrCreateDatabase("IQUIZ.db", Context.MODE_PRIVATE,null);
        i=getIntent();
        TextView qidtv = (TextView) findViewById(R.id.qidtxt);
        qid=i.getStringExtra("QID");
        qidtv.setText(qid);
        qd=(EditText) findViewById(R.id.qdetxt);
        opa=(EditText) findViewById(R.id.aopetxt);
        opb=(EditText) findViewById(R.id.bopetxt);
        opc=(EditText) findViewById(R.id.copetxt);
        opd=(EditText) findViewById(R.id.dopetxt);
        ans=(EditText) findViewById(R.id.ansetxt);
        onerb=(RadioButton)findViewById(R.id.onerButton);
        tworb=(RadioButton)findViewById(R.id.tworButton);
        tenrb=(RadioButton)findViewById(R.id.tenrButton);
        twentyrb=(RadioButton)findViewById(R.id.twentyrButton);
        qdbt=(Button)findViewById(R.id.qdbutton);
        aopbt=(Button)findViewById(R.id.aopbutton);
        bopbt=(Button)findViewById(R.id.bopbutton);
        copbt=(Button)findViewById(R.id.copbutton);
        dopbt=(Button)findViewById(R.id.dopbutton);
        ansbt=(Button)findViewById(R.id.ansbutton);
        durbt=(Button)findViewById(R.id.durbutton);
        pntbt=(Button)findViewById(R.id.pntsbutton);
        dbt=(Button)findViewById(R.id.donebutton);
        try{
            Cursor c=db.rawQuery("SELECT * FROM question_table WHERE QID='"+qid+"'",null);
            if(c!=null && c.getCount()!=0){
                if(c.moveToFirst()){
                    do{
                        qd.setText(c.getString(c.getColumnIndex("QUES_DETAILS")));
                        opa.setText(c.getString(c.getColumnIndex("OPTA")));
                        opa.setText(c.getString(c.getColumnIndex("OPTA")));
                        opb.setText( c.getString(c.getColumnIndex("OPTB")));
                        opc.setText(c.getString(c.getColumnIndex("OPTC")));
                        opd.setText( c.getString(c.getColumnIndex("OPTD")));
                        ans.setText(c.getString(c.getColumnIndex("ANS")));
                        if(c.getString(c.getColumnIndex("DURATION")).equals("1"))
                            onerb.setChecked(true);
                        else
                            tworb.setChecked(true);
                        if(c.getString(c.getColumnIndex("POINTS")).equals("10"))
                            tenrb.setChecked(true);
                        else
                            twentyrb.setChecked(true);
                    }while(c.moveToNext());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void onradiobtnSelect(View v){
        boolean ch=((RadioButton)v).isChecked();
        switch (v.getId()){
            case R.id.onerButton :
                if(ch){
                    dur="1";
                    df=1;
                    }
                break;
            case R.id.tworButton :
                if(ch){
                    dur="2";
                    df=1;
                }
                break;
            case R.id.tenrButton :
                if(ch){
                    pnt="10";
                    pf=1;
                }
                break;
            case R.id.twentyrButton :
                if(ch){
                    pnt="20";
                    pf=1;
                }
                break;
            default:
                break;
        }
    }
    public void btnClick(View v){
        if(qdbt==v){
            if(qd.getText().toString().equals(""))
                Toast.makeText(UpdatequestionActivity.this, "Question details can't be left blank!", Toast.LENGTH_LONG).show();
            else{
                appendupdatedata("QUES_DETAILS",qd.getText().toString());
            }
        }
        if(aopbt==v){
            if(opa.getText().toString().equals(""))
                Toast.makeText(UpdatequestionActivity.this, "Option A can't be left blank!", Toast.LENGTH_LONG).show();
            else{
                appendupdatedata("OPTA",opa.getText().toString());
            }
        }
        if(bopbt==v){
            if(opb.getText().toString().equals(""))
                Toast.makeText(UpdatequestionActivity.this, "Option B can't be left blank!", Toast.LENGTH_LONG).show();
            else{
                appendupdatedata("OPTB",opb.getText().toString());
            }
        }
        if(copbt==v){
            if(opc.getText().toString().equals(""))
                Toast.makeText(UpdatequestionActivity.this, "Option C can't be left blank!", Toast.LENGTH_LONG).show();
            else{
                appendupdatedata("OPTC",opc.getText().toString());
            }
        }
        if(dopbt==v){
            if(opd.getText().toString().equals(""))
                Toast.makeText(UpdatequestionActivity.this, "Option D can't be left blank!", Toast.LENGTH_LONG).show();
            else{
                appendupdatedata("OPTD",opd.getText().toString());
            }
        }
        if(ansbt==v){
            if(ans.getText().toString().equals(""))
                Toast.makeText(UpdatequestionActivity.this, "ANSWER can't be left blank!", Toast.LENGTH_LONG).show();
            else{
                appendupdatedata("ANS",ans.getText().toString());
            }
        }
        if(durbt==v){
            if(df==0)
                Toast.makeText(UpdatequestionActivity.this, "Select duration!", Toast.LENGTH_LONG).show();
            else {
                appendupdatedata("DURATION", dur);
                df = 0;
            }
        }
        if(pntbt==v){
            if(pf==0)
                Toast.makeText(UpdatequestionActivity.this, "Select points!", Toast.LENGTH_LONG).show();
            else {
                appendupdatedata("POINTS", pnt);
                pf = 0;
            }
        }
        if(dbt==v){
            i=new Intent(getApplicationContext(),AdminActivity.class);
            startActivity(i);
        }
    }
    public void appendupdatedata(String cloumn,String data){
        String sql;
        sql="UPDATE question_table SET "+cloumn+"="+"'"+data+"'"+"WHERE QID='"+qid+"'";
        db.execSQL(sql);
        Toast.makeText(getApplicationContext(),"Updation Success!",Toast.LENGTH_LONG).show();
    }
}
