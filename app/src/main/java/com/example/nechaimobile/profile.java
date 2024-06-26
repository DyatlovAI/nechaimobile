package com.example.nechaimobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    private String userID;
    private Button setting;
    private Button back;
    private Button shtrafi;
    private Button poezdki;
    private DatabaseReference databaseReference1;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        progressBar = findViewById(R.id.progressBar);
        databaseReference1 = FirebaseDatabase.getInstance().getReference("documents");
        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this, setting.class);
                intent.putExtra("userID", userID);
                startActivity(intent);

            }
        });
        shtrafi = findViewById(R.id.shtrafi);
        shtrafi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this, shtrafi.class);
                intent.putExtra("userID", userID);
                startActivity(intent);

            }
        });
        poezdki = findViewById(R.id.poezdki);
        poezdki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this, poezdki.class);
                intent.putExtra("userID", userID);
                startActivity(intent);

            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        loadname();
    }
    private void loadname() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean userFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserDocument user = snapshot.getValue(UserDocument.class);
                    if (user != null && userID.equals(user.getUserId())) {
                        userFound = true;
                        String fullName = user.getFullName();

                        // Найдем TextView для отображения даты рождения
                        TextView tvfullName = findViewById(R.id.fullname);

                        tvfullName.setText(fullName);



                    }
                    progressBar.setVisibility(View.GONE);
                }
                if (!userFound) {
                    Log.e("TAG", "User with userID " + userID + " not found in Firebase database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "Failed to read value.", databaseError.toException());
            }
        });
    }
}