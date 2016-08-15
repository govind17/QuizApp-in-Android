package com.example.govindshukla.iquiz;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    private EditText name, eid, pass, conpass;
    private Button bt;
    SQLiteDatabase db = null;
    private static final String dbname = "IQUIZ.db";
    String et1, et2, et3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bt = (Button) findViewById(R.id.subutton);
        name = (EditText) findViewById(R.id.etname);
        pass = (EditText) findViewById(R.id.etpass);
        eid = (EditText) findViewById(R.id.etid);
        conpass = (EditText) findViewById(R.id.cetpass);
        db = openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
        final Intent in = new Intent(this, LoginActivity.class);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et1 = name.getText().toString();
                et2 = eid.getText().toString();
                et3 = pass.getText().toString();
                if (et1.equals("") || et2.equals("") || et3.equals(""))
                    Toast.makeText(SignUp.this, "Nothing should be left blank", Toast.LENGTH_LONG).show();
                else {
                    if (et3.equals(conpass.getText().toString())) {
                        try {
                            Cursor c = db.rawQuery("SELECT * FROM user_table WHERE USERNAME='" + et2 + "'", null);
                            if (c != null && c.getCount() != 0) {
                                Toast.makeText(SignUp.this, "The USERNAME already exists!", Toast.LENGTH_SHORT).show();
                            } else {
                                db.execSQL("INSERT INTO user_table VALUES('" + et2 + "','" + et1 + "','" + et3 + "',0,0,0);");
                                startActivity(in);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else
                        Toast.makeText(SignUp.this, "Passwords doesn't match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

