package com.example.nechaimobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.nechaimobile.Models.Automobile;
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

import java.util.ArrayList;
import java.util.List;

public class main extends AppCompatActivity {

    private String AutClass = "Эконом";
    private String Autkpp = "МКПП";
    private MapView mapView;
    private Button btn_spisok;
    private Button btn_radar;
    private DatabaseReference databaseReference;
    private List<Automobile> automobiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("4c9e498b-ddd0-4aff-b94d-b5b7e4075a5c"); // Замените на свой API ключ от Yandex MapKit
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);
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
                        Intent intent = new Intent(main.this, carprofile.class);
                        intent.putExtra("number", clickedAutomobile.getNumber());
                        intent.putExtra("name", clickedAutomobile.getName());
                        intent.putExtra("korobka", clickedAutomobile.getKorobka());
                        intent.putExtra("latitude", clickedAutomobile.getLatitude());
                        intent.putExtra("longitude", clickedAutomobile.getLongitude());
                        startActivity(intent);
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

        btn_spisok = findViewById(R.id.btn_spisok);
        btn_spisok.setOnClickListener(v -> {
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
                Intent intent = new Intent(main.this, carprofile.class);
                intent.putExtra("number", clickedAutomobile.getNumber());
                intent.putExtra("name", clickedAutomobile.getName());
                intent.putExtra("korobka", clickedAutomobile.getKorobka());
                intent.putExtra("latitude", clickedAutomobile.getLatitude());
                intent.putExtra("longitude", clickedAutomobile.getLongitude());
                startActivity(intent);
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
                        Intent intent = new Intent(main.this, carprofile.class);
                        intent.putExtra("number", clickedAutomobile.getNumber());
                        intent.putExtra("name", clickedAutomobile.getName());
                        intent.putExtra("korobka", clickedAutomobile.getKorobka());
                        intent.putExtra("latitude", clickedAutomobile.getLatitude());
                        intent.putExtra("longitude", clickedAutomobile.getLongitude());
                        startActivity(intent);
                        return true;
                    });
                }
            }
        }
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

