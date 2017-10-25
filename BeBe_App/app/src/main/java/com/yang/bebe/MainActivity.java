package com.yang.bebe;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.yang.bebe.DB.BabiesDBHelper;
import com.yang.bebe.DB.BabyInfoActivity;
import com.yang.bebe.DB.BpmDBHelper;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment temperatureFragment;
    private Fragment babyconditionFragment;
    private Fragment cameraFragement;
    private Fragment pictureFragment;
    public static BabiesDBHelper dbHelper;
    public static SQLiteDatabase db;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureFragment = new TemperatureFragment();
        babyconditionFragment = new BabyConditionFragment();
        dbHelper = new BabiesDBHelper(this);

        /**
         * 기본 화면 설정
         */
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, babyconditionFragment);
        // transaction.addToBackStack(null);
        transaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();



            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_header_view = navigationView.getHeaderView(0);

        TextView nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.babynameinfo);
        db = dbHelper.getReadableDatabase();
        String sql = "select baby_name from babies ";
        Cursor c = db.rawQuery(sql, null); // 배열지정
        c.moveToFirst();
//        Log.d("ccccc",c.getString(0));
        if( c != null && c.moveToFirst() ) {
            nav_header_id_text.setText(c.getString(0));
        }
       // View header=navigationView.getHeaderView(0);
       // name = (TextView)header.findViewById(R.id.babynameinfo);
        switchTabs(getFocus());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            return true;
        }
        else  if (id == R.id.Baby_info) {
            Intent intent = new Intent(this, BabyInfoActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void switchTabs(int tab){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch(tab){
            case 0:
                transaction.replace(R.id.container, babyconditionFragment);
                break;
            case 1:
                transaction.replace(R.id.container, temperatureFragment);
                break;
            case 2:
       //         transaction.replace(R.id.container, cameraFragement );
                break;
            case 3:
//                transaction.replace(R.id.container, pictureFragment);
                break;
            default:

                break;
        }
    }
    /**
     * MainActivity가 implement한
     * NavigationView.OnNavigationItemSelectedListener의 구현체
     * @see NavigationView.OnNavigationItemSelectedListener
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bpm) {
            transaction.replace(R.id.container, babyconditionFragment);
        } else if (id == R.id.nav_tem) {
            transaction.replace(R.id.container, temperatureFragment);
        } else if (id == R.id.nav_camera) {
            transaction.replace(R.id.container, cameraFragement );
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        /**
         *
         */
        // transaction.addToBackStack(null);

        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      //  name = (TextView)header.findViewById(R.id.username);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                String alertTitle = "Auto Scheduler";
                String buttonMessage = "어플을 종료하시겠습니까?";
                String buttonYes = "Yes";
                String buttonNo = "No";

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(alertTitle)
                        .setMessage(buttonMessage)
                        .setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                moveTaskToBack(true);
                                finish();
                            }
                        })
                        .setNegativeButton(buttonNo, null)
                        .show();
        }
        return true;

    }

    public int getFocus(){
        Intent intent = getIntent();
        int focus = intent.getIntExtra("tab", 1);
        return focus;
    }

}
