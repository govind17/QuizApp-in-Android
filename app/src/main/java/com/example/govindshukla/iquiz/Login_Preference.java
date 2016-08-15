package com.example.govindshukla.iquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by GovindShukla on 31-07-2016.
 */
public class Login_Preference extends AppCompatActivity {
    Context c;
    Login_Preference(Context c1){
        c=c1;
    }
    public void setbool(String key,boolean value){
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor edit=sp.edit();
        edit.putBoolean(key,value);
        edit.commit();
    }
    public void setString(String key,String value){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor edit=sp.edit();
        edit.putString(key,value);
        edit.commit();
    }
    public boolean getbool(String key){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(c);
        boolean l=sp.getBoolean(key,false);
        return l;
    }
    public String getString(String key){
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(c);
        String str=sp.getString(key,"");
        return str;
    }
    public void logout(){
        this.setbool("Loggedin",false);
    }
}
