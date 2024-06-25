package com.example.nechaimobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class main extends AppCompatActivity {

    private MapView mapView;
    private Button btn_spisok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey("4c9e498b-ddd0-4aff-b94d-b5b7e4075a5c"); // Замените на свой API ключ от Yandex MapKit
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapview);
        mapView.getMap().setNightModeEnabled(true);

        // Устанавливаем начальную позицию и масштаб карты
        mapView.getMap().move(
                new CameraPosition(new Point(55.788608, 49.124142), 12.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        // Создаем метку на карте
        Point point = new Point(55.789534, 49.124513);
        PlacemarkMapObject placemark = mapView.getMap().getMapObjects().addPlacemark(point);
        placemark.setIcon(ImageProvider.fromResource(this, R.drawable.car1));
        placemark.setIconStyle(new IconStyle().setScale(1f));

        // Устанавливаем пользовательские данные для метки (если нужно)
        placemark.setUserData("Point1");

        // Устанавливаем обработчик кликов для метки
        placemark.addTapListener((mapObject, point1) -> {
            // Получаем пользовательские данные, которые были установлены для метки
            String userData = (String) mapObject.getUserData();

            // При клике на метку выполняем переход на указанную активность
            Intent intent = new Intent(main.this, carprofile.class);
            intent.putExtra("pointUserData", userData); // Передаем данные в следующую активность
            startActivity(intent);

            return true; // Возвращаем true, чтобы указать, что событие обработано
        });

        btn_spisok = findViewById(R.id.btn_spisok);
        btn_spisok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Действия по нажатию кнопки
            }
        });
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
