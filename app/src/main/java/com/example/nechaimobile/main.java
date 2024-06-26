package com.example.nechaimobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nechaimobile.Models.Automobile;
import com.example.nechaimobile.Models.Order_Aut;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class main extends AppCompatActivity {

    private String AutClass = "Эконом";
    private String Autkpp = "МКПП";
    private String Autnum;
    private int  totalPrice;
    private int selectedTime;
    private String userID;
    private MapView mapView;
    private Button btn_spisok;
    private Button btn_radar;
    private Button poisk;
    private Button menu;
    private DatabaseReference databaseReference;
    private List<Automobile> automobiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("4c9e498b-ddd0-4aff-b94d-b5b7e4075a5c"); // Замените на свой API ключ от Yandex MapKit
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userID = sharedPreferences.getString("userID", null);
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main.this, profile.class);
                intent.putExtra("userID", userID);
                startActivity(intent);

            }
        });
        poisk = findViewById(R.id.poisk);
        poisk.setOnClickListener(v -> {
                Dialog poiskdialog = new Dialog(this);
                poiskdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                poiskdialog.setContentView(R.layout.activity_poisk);
                Button close=poiskdialog.findViewById(R.id.search_close_btn);
                Button poisk=poiskdialog.findViewById(R.id.dalee);
                EditText number=poiskdialog.findViewById(R.id.number_poisk);
                close.setOnClickListener(v1 -> {
                    poiskdialog.dismiss();
                });
            poisk.setOnClickListener(v1 -> {
                Autnum = number.getText().toString().trim();
                if (!Autnum.isEmpty()) {

                    boolean found = false;
                    for (Automobile automobile : automobiles) {
                        if (Autnum.equals(automobile.getNumber())) {
                            found = true;
                            showCarProfileDialog(automobile);
                            poiskdialog.dismiss();
                        }
                    }
                    if (!found) {
                        Toast.makeText(main.this, "Машина с номером " + Autnum + " не найдена", Toast.LENGTH_SHORT).show();
                    }
                }
            });
                poiskdialog.show();
                poiskdialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                poiskdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                poiskdialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                poiskdialog.getWindow().setGravity(Gravity.BOTTOM);
        });
        mapView = findViewById(R.id.mapview);
        mapView.getMap().setNightModeEnabled(true);
        mapView.getMap().move(
                new CameraPosition(new Point(55.788608, 49.124142), 12.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("automobiles");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                automobiles.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Automobile automobile = dataSnapshot.getValue(Automobile.class);
                    automobiles.add(automobile);
                    Point point = new Point(Double.parseDouble(automobile.getLatitude()), Double.parseDouble(automobile.getLongitude()));
                    PlacemarkMapObject placemark = mapView.getMap().getMapObjects().addPlacemark(point);
                    placemark.setIcon(ImageProvider.fromResource(main.this, R.drawable.car1));
                    placemark.setIconStyle(new IconStyle().setScale(1f));
                    placemark.setUserData(automobile);
                    placemark.addTapListener((mapObject, point1) -> {
                        Automobile clickedAutomobile = (Automobile) mapObject.getUserData();
                        showCarProfileDialog(clickedAutomobile);
                        return true;
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Обработка ошибки при чтении данных из Firebase
            }
        });

        btn_radar = findViewById(R.id.radar);
        btn_radar.setOnClickListener(v -> {
            Dialog radarDialog = new Dialog(this);
            radarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            radarDialog.setContentView(R.layout.filter);

            Switch autClassSwitch = radarDialog.findViewById(R.id.komfortSwitch);
            Switch autKppSwitch = radarDialog.findViewById(R.id.akppClass);

            autKppSwitch.setChecked("АКПП".equals(Autkpp));
            autKppSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Autkpp = isChecked ? "АКПП" : "МКПП";
            });

            autClassSwitch.setChecked("Комфорт".equals(AutClass));
            autClassSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                AutClass = isChecked ? "Комфорт" : "Эконом";
            });
            Button clear_filt=radarDialog.findViewById(R.id.back);
            clear_filt.setOnClickListener(v1 -> {
                clearPlacemarks();
                autKppSwitch.setChecked(false);
                autClassSwitch.setChecked(false);
                radarDialog.dismiss();
            });
            Button ready_filt=radarDialog.findViewById(R.id.ready);
            ready_filt.setOnClickListener(v1 -> {
                if (!autKppSwitch.isChecked() && !autClassSwitch.isChecked()) {
                    clearPlacemarks();
                } else {
                    updatePlacemarks();
                }
                radarDialog.dismiss();
            });
            radarDialog.show();
            radarDialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            radarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            radarDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            radarDialog.getWindow().setGravity(Gravity.BOTTOM);
        });


    }

    private void clearPlacemarks() {
        mapView.getMap().getMapObjects().clear();
        for (Automobile automobile : automobiles) {
            Point point = new Point(Double.parseDouble(automobile.getLatitude()), Double.parseDouble(automobile.getLongitude()));
            PlacemarkMapObject placemark = mapView.getMap().getMapObjects().addPlacemark(point);
            placemark.setIcon(ImageProvider.fromResource(this, R.drawable.car1));
            placemark.setIconStyle(new IconStyle().setScale(1f));
            placemark.setUserData(automobile);

            placemark.addTapListener((mapObject, point1) -> {
                Automobile clickedAutomobile = (Automobile) mapObject.getUserData();
                showCarProfileDialog(clickedAutomobile);
                return true;
            });
        }
    }

    private void updatePlacemarks() {
        mapView.getMap().getMapObjects().clear();

        for (Automobile automobile : automobiles) {
            if (automobile.getAut_class() != null && automobile.getKorobka() != null) {
                if (automobile.getAut_class().equals(AutClass) && automobile.getKorobka().equals(Autkpp)) {
                    Point point = new Point(Double.parseDouble(automobile.getLatitude()), Double.parseDouble(automobile.getLongitude()));
                    PlacemarkMapObject placemark = mapView.getMap().getMapObjects().addPlacemark(point);
                    placemark.setIcon(ImageProvider.fromResource(this, R.drawable.car1));
                    placemark.setIconStyle(new IconStyle().setScale(1f));
                    placemark.setUserData(automobile);
                    placemark.addTapListener((mapObject, point1) -> {
                        Automobile clickedAutomobile = (Automobile) mapObject.getUserData();
                        showCarProfileDialog(clickedAutomobile);
                        return true;
                    });
                }
            }
        }
    }
    private void showCarProfileDialog(Automobile automobile) {
        Dialog carProfileDialog = new Dialog(this);
        carProfileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        carProfileDialog.setContentView(R.layout.activity_carprofile);
        Button book=carProfileDialog.findViewById(R.id.book_button);
        Button close=carProfileDialog.findViewById(R.id.close_button);
        TextView autNameProfile = carProfileDialog.findViewById(R.id.aut_name_profile);
        TextView korobkaAutProfile = carProfileDialog.findViewById(R.id.korobka_aut_profile);
        TextView priceAutProfile = carProfileDialog.findViewById(R.id.price_aut_profile);
        Spinner timeIntervalSpinner = carProfileDialog.findViewById(R.id.time_interval_spinner);
        TextView resultPrice = carProfileDialog.findViewById(R.id.result_price);
        ImageView car_photo = carProfileDialog.findViewById(R.id.photo_aut_profile);
        autNameProfile.setText(automobile.getName());
        korobkaAutProfile.setText(automobile.getKorobka());
        priceAutProfile.setText(automobile.getPrice()+"₽");
        Glide.with(main.this)
                .load(automobile.getPhoto_aut())
                .into(car_photo);
        timeIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int[] timeIntervals = {30, 60, 120, 180, 240, 300};
                 selectedTime = timeIntervals[position];
                int pricePerMinute = Integer.parseInt(automobile.getPrice());
                 totalPrice = selectedTime * pricePerMinute;
                resultPrice.setText(totalPrice + "₽");
            }
            

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                resultPrice.setText("");
            }
        });
        close.setOnClickListener(v -> {
            carProfileDialog.dismiss();
        });
        book.setOnClickListener(v2 -> {
            String orderId = generateOrderId();
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Order_Aut orderAut = new Order_Aut(orderId,userID,automobile.getNumber(),automobile.getName(),totalPrice,selectedTime,automobile.getPhoto_aut(),automobile.getAut_class(),automobile.getKorobka(),automobile.getLatitude(),automobile.getLongitude(),currentDate);
            DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child("orders");
            orderReference.child(orderId).setValue(orderAut)
                    .addOnSuccessListener(aVoid -> Toast.makeText(main.this, "Заказ успешно оформлен", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(main.this, "Ошибка при оформлении заказа: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
        carProfileDialog.show();
        carProfileDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
       
        carProfileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        carProfileDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        carProfileDialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public static String generateOrderId() {

        long timestamp = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        String OrderId = timestamp + "-" + uuid;

        return OrderId;
    }
    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
}

