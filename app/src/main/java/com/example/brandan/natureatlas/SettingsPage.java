package com.example.brandan.natureatlas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;

public class SettingsPage extends AppCompatActivity {
    CheckBox dm, keepMeSignedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        final Spinner mapTypes = (Spinner)findViewById(R.id.mapTypes);

        dm = (CheckBox)findViewById(R.id.draftMode);
        keepMeSignedIn = (CheckBox)findViewById(R.id.kmsi);

        mapTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Object choice = mapTypes.getItemAtPosition(i);
                SharedPreferences.Editor e = getSharedPreferences("natureShared", MODE_PRIVATE).edit();
                e.putString("mapMode", choice.toString());
                e.putBoolean("kmsi", keepMeSignedIn.isChecked());
                e.putBoolean("draftMode", dm.isChecked());
                e.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    public void ChangePassword(View view)
    {
        Intent i = new Intent(this, ChangePassword.class);
        startActivity(i);
    }
    public void Apply(View view)
    {
        SharedPreferences.Editor e = getSharedPreferences("natureShared", MODE_PRIVATE).edit();
        e.putBoolean("kmsi", keepMeSignedIn.isChecked());
        e.putBoolean("draftMode", dm.isChecked());
        e.apply();
        Intent intent = new Intent(this,AtlasMap.class);
        startActivity(intent);
    }

}
