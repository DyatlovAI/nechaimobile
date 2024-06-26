package com.example.nechaimobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class card extends AppCompatActivity {

    private String userId;
    private boolean ignoreChange = false;
    private EditText edCardNumber, edExpiryDate, edCVC;
    private Button btnAddCard;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        // Получение userID из интента
        Intent intent = getIntent();
        userId = intent.getStringExtra("userID");


        databaseReference = FirebaseDatabase.getInstance().getReference("cards");

        edCardNumber = findViewById(R.id.edCardNumber);
        edExpiryDate = findViewById(R.id.edExpiryDate);
        edCVC = findViewById(R.id.edCVC);
        btnAddCard = findViewById(R.id.add_card);

        edCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ignoreChange) {
                    ignoreChange = false;
                    return;
                }

                String cardNumber = s.toString().replaceAll("\\s", "");

                if (cardNumber.length() < 16) {
                    edCardNumber.setError("Введите 16 цифр");
                } else if (s.length() < 16) {
                    edCardNumber.setError("Введите минимум 4 цифры");
                } else {
                    edCardNumber.setError(null);
                }

                if (cardNumber.length() > 16) {
                    ignoreChange = true;
                    s.delete(16, cardNumber.length());
                }

                // Ограничение в 5 символов (максимум 4 цифры + символ "/")
                if (s.length() > 16) {
                    ignoreChange = true;
                    s.delete(5, s.length());
                }
            }

        });
        edCVC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ignoreChange) {
                    ignoreChange = false;
                    return;
                }

                String cardNumber = s.toString().replaceAll("\\s", "");

                if (cardNumber.length() < 3) {
                    edCardNumber.setError("Введите 3 цифр");
                } else if (s.length() < 3) {
                    edCardNumber.setError("Введите минимум 3 цифры");
                } else {
                    edCardNumber.setError(null);
                }

                if (cardNumber.length() > 3) {
                    ignoreChange = true;
                    s.delete(3, cardNumber.length());
                }

                // Ограничение в 5 символов (максимум 4 цифры + символ "/")
                if (s.length() > 3) {
                    ignoreChange = true;
                    s.delete(3, s.length());
                }
            }

        });

        // Установка TextWatcher для форматирования ввода даты в формате MM/YY
        edExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2 && !s.toString().contains("/")) {
                    edExpiryDate.setText(s + "/");
                    edExpiryDate.setSelection(s.length() + 1);
                }
                if (s.length() < 4) {
                    edExpiryDate.setError("Введите минимум 4 цифры");
                } else {
                    String monthStr = s.subSequence(0, 2).toString();
                    int month = Integer.parseInt(monthStr);
                    if (month < 1 || month > 12) {
                        edExpiryDate.setError("Месяц должен быть от 01 до 12");
                    } else {
                        edExpiryDate.setError(null);
                    }

                    if (s.length() >= 5) {
                        String yearStr = s.subSequence(3, 5).toString();
                        int year = Integer.parseInt(yearStr);
                        if (year < 25 || year > 30) {
                            edExpiryDate.setError("Год должен быть от 25 до 30");
                        } else {
                            edExpiryDate.setError(null);
                        }
                    }
                }

                // Ограничение в 5 символов (максимум 4 цифры + символ "/")
                if (s.length() > 5) {
                    ignoreChange = true;
                    s.delete(5, s.length());
                }
            }
        });

        // Установка OnClickListener для кнопки добавления карты
        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCardToDatabase();
            }
        });

        // Обработка нажатия кнопки назад
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addCardToDatabase() {
        String cardNumber = edCardNumber.getText().toString().trim();
        String expiryDate = edExpiryDate.getText().toString().trim();
        String cvc = edCVC.getText().toString().trim();

        if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvc.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверяем, существует ли карта с таким userId
        Query query = databaseReference.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Если найдена карта с таким userId, удаляем ее
                    snapshot.getRef().removeValue();
                }

                // Добавляем новую карту
                Card_user cardUser = new Card_user(userId, cardNumber, expiryDate, cvc);
                databaseReference.push().setValue(cardUser);

                Toast.makeText(card.this, "Карта добавлена", Toast.LENGTH_SHORT).show();
                // После добавления новой карты
                Intent returnIntent = new Intent();
                returnIntent.putExtra("userID", userId); // Передаем обновленный userID обратно в предыдущую активность
                setResult(RESULT_OK, returnIntent);
                finish(); // Закрываем текущую активность
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибки запроса к базе данных
                Toast.makeText(card.this, "Ошибка добавления карты", Toast.LENGTH_SHORT).show();
            }
        });
    }
}