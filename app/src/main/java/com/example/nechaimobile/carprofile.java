package com.example.nechaimobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class carprofile extends AppCompatActivity {
    TextView number,name,longitude,latitude,korobka;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carprofile);
        number=findViewById(R.id.aut_profile_Number);
        name=findViewById(R.id.aut_profile_Name);
        korobka=findViewById(R.id.aut_profile_Korobka);
        Intent intent = getIntent();
        if (intent != null) {
            number.setText(intent.getStringExtra("number"));
            name.setText(intent.getStringExtra("name"));
            korobka.setText(intent.getStringExtra("korobka"));
        }
    }
}