package com.yang.bebe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.Scanner;

public class LaunchActivity extends AppCompatActivity {

 //   private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

    //    daoSession = ((App) getApplication()).getDaoSession();
         }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    public void goToSettings(MenuItem item){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void feedClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tab", 0);
        startActivity(intent);
    }

    public void heartrateClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tab", 1);
        startActivity(intent);
    }

    public void historyClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tab", 2);
        startActivity(intent);
    }

    public void thermometerClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tab", 3);
        startActivity(intent);
    }

}
