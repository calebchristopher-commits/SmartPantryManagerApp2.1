package com.example.smartpantrymanager.ui;
import android.content.*;import android.os.*;import android.widget.*;import androidx.appcompat.app.AppCompatActivity;import com.example.smartpantrymanager.R;
public class SettingsActivity extends AppCompatActivity{@Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_settings);Switch s=findViewById(R.id.switchExpiry);android.content.SharedPreferences p=getSharedPreferences("settings",0);s.setChecked(p.getBoolean("expiry",true));s.setOnCheckedChangeListener((v,c)->p.edit().putBoolean("expiry",c).apply());}}
