package com.rsv.samfun;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    private ActionBarDrawerToggle mtoggle;
    private HashMap<String, String> cookies = new HashMap<>();
    String uname, pword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mtoggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mtoggle);
        mtoggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        String name = getIntent().getStringExtra("header_name");
        uname = getIntent().getStringExtra("uname");
        pword = getIntent().getStringExtra("pword");
        cookies = (HashMap<String, String>) getIntent().getSerializableExtra("hashMap");
        View headerView = navigationView.getHeaderView(0);
        TextView header_name = headerView.findViewById(R.id.header_name);
        header_name.setText(name);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeActivity()).commit();
            getSupportActionBar().setTitle(R.string.app_name);
            navigationView.setCheckedItem(R.id.intro);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                moveTaskToBack(true);
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Press BACK again to exit", Toast.LENGTH_SHORT).show();
                }
            });
            mHandler.postDelayed(mRunnable, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            if (id == R.id.action_settings){
                Intent sett =new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(sett);
                return true;
            } else if (id == R.id.about) {
                Intent abt = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(abt);
                return true;
            }
            return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@Nullable MenuItem item) {
        int id = item.getItemId();
        String name = getIntent().getStringExtra("header_name");
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putString("uname", uname);
        bundle.putString("pword", pword);
        bundle.putSerializable("hashMap",cookies);


        if (id == R.id.intro) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeActivity()).commit();
            getSupportActionBar().setTitle(item.getTitle());
        } else if (id == R.id.view_ass ) {
            ViewAss frg = new ViewAss();
            frg.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frg).commit();
            getSupportActionBar().setTitle(item.getTitle());

        } else if (id == R.id.view_class) {
            View_class frg = new View_class();
            frg.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frg).commit();
            getSupportActionBar().setTitle(item.getTitle());
        } else if (id == R.id.view_weekly) {
            View_weekly frg = new View_weekly();
            frg.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frg).commit();
            getSupportActionBar().setTitle(item.getTitle());
        } else if (id == R.id.view_grade) {
            ViewGrade frg = new ViewGrade();
            frg.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frg).commit();
            getSupportActionBar().setTitle(item.getTitle());

        } else if (id == R.id.dorm) {
            Dorm frg = new Dorm();
            frg.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frg).commit();
            getSupportActionBar().setTitle(item.getTitle());
        } else if (id == R.id.clear) {
            clear frg = new clear();
            frg.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frg).commit();
            getSupportActionBar().setTitle(item.getTitle());
        } else if (id == R.id.spell) {
            Spelling frg = new Spelling();
            frg.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frg).commit();
            getSupportActionBar().setTitle(item.getTitle());
        } else if (id == R.id.logout) {
            this.finish();
            Intent logout=new Intent(this,login.class);
            startActivity(logout);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
