package com.example.nechaimobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nechaimobile.Models.Automobile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class import_automobile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etNumber, etName, etKorobka, etLatitude, etLongitude;
    private ImageView ivPhotoAut;
    private Button btnSelectImage, btnSubmit;
    private DatabaseReference databaseReference;
    private Uri photoAutUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_aut);

        etNumber = findViewById(R.id.etNumber);
        etName = findViewById(R.id.etName);
        etKorobka = findViewById(R.id.etKorobka);
        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        ivPhotoAut = findViewById(R.id.ivPhotoAut);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSubmit = findViewById(R.id.btnSubmit);

        databaseReference = FirebaseDatabase.getInstance().getReference("automobiles");

        btnSelectImage.setOnClickListener(v -> openFileChooser());

        btnSubmit.setOnClickListener(v -> saveAutomobileToFirebase());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photoAutUri = data.getData();
            ivPhotoAut.setImageURI(photoAutUri);
        }
    }

    private void saveAutomobileToFirebase() {
        String number = etNumber.getText().toString();
        String name = etName.getText().toString();
        String korobka = etKorobka.getText().toString();
        String latitude = etLatitude.getText().toString();
        String longitude = etLongitude.getText().toString();
        String photoAut = photoAutUri != null ? photoAutUri.toString() : "";

        if (!number.isEmpty() && !name.isEmpty() && !korobka.isEmpty() && !latitude.isEmpty() && !longitude.isEmpty()) {
            Automobile automobile = new Automobile(number, name, korobka, latitude, longitude, photoAut,"Эконом","");
            databaseReference.child(number).setValue(automobile)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(import_automobile.this, "Automobile added successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(import_automobile.this, "Error adding automobile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(import_automobile.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }}
