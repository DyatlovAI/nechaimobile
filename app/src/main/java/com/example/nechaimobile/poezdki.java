package com.example.nechaimobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nechaimobile.Adapter.OrderAdapter;
import com.example.nechaimobile.Models.Order_Aut;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class poezdki extends AppCompatActivity {
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order_Aut> ordersList;
    private DatabaseReference ordersReference;
    private String userID;
    private Button back;
    private TextView noOrdersText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poezdki);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        back = findViewById(R.id.back);
        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersList = new ArrayList<>();
        orderAdapter = new OrderAdapter(ordersList);
        ordersRecyclerView.setAdapter(orderAdapter);
        noOrdersText = findViewById(R.id.no_orders_text);

        String userId = userID;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        ordersReference = FirebaseDatabase.getInstance().getReference().child("orders");

        ordersReference.orderByChild("userID").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order_Aut order = orderSnapshot.getValue(Order_Aut.class);
                    ordersList.add(order);
                }

                if (ordersList.isEmpty()) {
                    ordersRecyclerView.setVisibility(View.GONE);
                    noOrdersText.setVisibility(View.VISIBLE);
                } else {
                    ordersRecyclerView.setVisibility(View.VISIBLE);
                    noOrdersText.setVisibility(View.GONE);
                }

                orderAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }
}