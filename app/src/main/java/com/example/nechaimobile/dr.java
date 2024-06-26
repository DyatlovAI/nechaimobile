package com.example.nechaimobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class dr extends AppCompatActivity {

    private String userId;
    private boolean ignoreChange = false;
    private EditText edFullName, edPassportSeries, edPassportNumber;
    private TextView textViewDate;
    private LinearLayout dateLayout;
    private Button btnAddData;
    private DatabaseReference databaseReference;
    private String birthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userID");

        databaseReference = FirebaseDatabase.getInstance().getReference("documents");

        edFullName = findViewById(R.id.edFullName);
        edPassportSeries = findViewById(R.id.edPassportSeries);
        edPassportNumber = findViewById(R.id.edPassportNumber);
        btnAddData = findViewById(R.id.add_data);
        textViewDate = findViewById(R.id.textViewDate);
        dateLayout = findViewById(R.id.dateLayout);

        // Загрузка данных пользователя, если они существуют
        loadUserData();

        edPassportSeries.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (ignoreChange) {
                    ignoreChange = false;
                    return;
                }

                if (s.length() > 4) {
                    ignoreChange = true;
                    s.delete(4, s.length());
                }
            }
        });

        edPassportNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (ignoreChange) {
                    ignoreChange = false;
                    return;
                }

                if (s.length() > 6) {
                    ignoreChange = true;
                    s.delete(6, s.length());
                }
            }
        });

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrUpdateData();
            }
        });

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                textViewDate.setText(birthDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void loadUserData() {
        DatabaseReference userRef = databaseReference.child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserDocument user = dataSnapshot.getValue(UserDocument.class);
                    if (user != null) {
                        edFullName.setText(user.getFullName());
                        edPassportSeries.setText(user.getPassportSeries());
                        edPassportNumber.setText(user.getPassportNumber());
                        textViewDate.setText(user.getBirthDate());
                        birthDate = user.getBirthDate();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при загрузке данных из базы данных
                Toast.makeText(dr.this, "Ошибка загрузки данных: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addOrUpdateData() {
        String fullName = edFullName.getText().toString().trim();
        String passportSeries = edPassportSeries.getText().toString().trim();
        String passportNumber = edPassportNumber.getText().toString().trim();

        if (fullName.isEmpty() || passportSeries.isEmpty() || passportNumber.isEmpty() || birthDate == null || birthDate.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }
        Query query = databaseReference.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Если найдена карта с таким userId, удаляем ее
                    snapshot.getRef().removeValue();
                }

                // Добавляем новую карту
                UserDocument userDocument = new UserDocument(userId, fullName, passportSeries, passportNumber, birthDate);

                databaseReference.push().setValue(userDocument );

                Toast.makeText(dr.this, "Карта добавлена", Toast.LENGTH_SHORT).show();
                // После добавления новой карты
                Intent returnIntent = new Intent();
                returnIntent.putExtra("userID", userId);
                setResult(RESULT_OK, returnIntent);
                finish();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при чтении данных из базы данных
                Toast.makeText(dr.this, "Ошибка чтения данных: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
