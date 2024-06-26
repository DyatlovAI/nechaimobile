package com.example.nechaimobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText edEmail, edPassword;
    private Button btn_login;
    private FirebaseAuth mAuth;

    private TextView register_txt, email_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btn_login = findViewById(R.id.btn_login);
        register_txt = findViewById(R.id.reg);
        email_txt = findViewById(R.id.vosst);

        // Проверяем сохранённый идентификатор пользователя
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedUserID = sharedPreferences.getString("userID", null);
        Log.d("SavedUserID", "UserID: " + savedUserID);
        if (savedUserID != null) {
            // Пользователь уже авторизован, переходим к главной активности
            Log.d("LoginScreen", "User already logged in, redirecting to MainActivity");
            Intent intent = new Intent(Login.this, main.class);
            startActivity(intent);
            finish();
        } else {

            Log.d("LoginScreen", "Initializing login screen");
            initializeLoginScreen();
        }
    }

    private void initializeLoginScreen() {
        register_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        email_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, pass1.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edEmail.getText().toString().isEmpty() || edPassword.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Сохраняем идентификатор пользователя
                                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("userID", mAuth.getCurrentUser().getUid());
                                        editor.apply();

                                        Log.d("LoginScreen", "User logged in successfully, redirecting to MainActivity");
                                        Intent intent = new Intent(Login.this, main.class);
                                        startActivity(intent);
                                        finish(); // Закрываем текущую активность
                                    } else {
                                        Toast.makeText(Login.this, "You have errors", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}