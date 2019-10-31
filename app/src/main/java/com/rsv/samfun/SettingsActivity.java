package com.rsv.samfun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Button  clearp = findViewById(R.id.clear);
        clearp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear_prefs();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clear_prefs(){
        mPrefs.edit().clear().apply();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(),"Clear Successful",Toast.LENGTH_SHORT);
                View view = toast.getView();
                view.setBackgroundColor(Color.TRANSPARENT);

                TextView text = view.findViewById(android.R.id.message);
                text.setShadowLayer((float) 0.75, 2, 4, Color.BLACK);
                text.setTextColor(Color.WHITE);
                toast.show();
            }
        });
    }
}
