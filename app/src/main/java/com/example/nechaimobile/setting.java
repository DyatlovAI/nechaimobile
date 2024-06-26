package com.example.nechaimobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class setting extends AppCompatActivity {

    private String userID;
    private Button back;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private TextView tvEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        databaseReference = FirebaseDatabase.getInstance().getReference("cards");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("documents");
        tvEmail = findViewById(R.id.email);
        LinearLayout logoutLayout = findViewById(R.id.logout);
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        // Получение текущего пользователя Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            // Установка почты в TextView
            tvEmail.setText(userEmail);
        }

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadCards();
        loaddr();
    }

    private void loadCards() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Card_user card = snapshot.getValue(Card_user.class);
                    if (card != null && userID.equals(card.getUserId())) {
                        String cardNumber = card.getNumber();
                        String lastFourDigits = cardNumber.length() > 4 ? cardNumber.substring(cardNumber.length() - 4) : cardNumber;

                        TextView cardNumberTextView = findViewById(R.id.card);
                        cardNumberTextView.setText(lastFourDigits);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void loaddr() {
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean userFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserDocument user = snapshot.getValue(UserDocument.class);
                    if (user != null && userID.equals(user.getUserId())) {
                        userFound = true;
                        String birthDate = user.getBirthDate();

                        // Найдем TextView для отображения даты рождения
                        TextView tvBirthDate = findViewById(R.id.drk);
                        Log.d("TAG", "Birth Date retrieved from Firebase: " + birthDate);
                        tvBirthDate.setText(birthDate);



                    }
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


    public void card(View view) {
        Intent intent = new Intent(this, card.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
    public void brthd(View view) {
        Intent intent = new Intent(this, dr.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
    public void logout() {
        // Очистка данных пользователя в SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userID");
        editor.apply();

        // Возвращение к экрану авторизации
        Intent intent = new Intent(setting.this, Login.class);
        startActivity(intent);
        finish(); // Закрытие текущей активности
    }
}
