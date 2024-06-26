package com.example.nechaimobile.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nechaimobile.Models.Order_Aut;
import com.example.nechaimobile.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order_Aut> orders;

    public OrderAdapter(List<Order_Aut> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order_Aut order = orders.get(position);
        holder.automobileName.setText("Модель машины: " +order.getName_aut());
        holder.orderAutNum.setText("Номер машины: " +order.getNumber_aut());
        holder.orderDate.setText("Дата заказа: " +order.getOrderDate());
        holder.orderPrice.setText(String.valueOf("Цена: "+order.getPrice()+"₽"));
        holder.orderTime.setText(String.valueOf("Время аренды: "+order.getTime()+"мин"));

        // Загрузка изображения автомобиля
        Glide.with(holder.itemView.getContext())
                .load(order.getPhoto_aut())
                .placeholder(R.drawable.car1)
                .into(holder.autPhoto);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView automobileName,orderAutNum, orderDate, orderPrice, orderTime;
        ImageView autPhoto;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            autPhoto = itemView.findViewById(R.id.aut_photo);
            automobileName = itemView.findViewById(R.id.automobile_name);
            orderAutNum=itemView.findViewById(R.id.automobile_number);
            orderDate = itemView.findViewById(R.id.order_date);
            orderPrice = itemView.findViewById(R.id.order_price);
            orderTime = itemView.findViewById(R.id.order_time);
        }
    }
}
