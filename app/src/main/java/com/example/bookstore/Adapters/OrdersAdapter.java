package com.example.bookstore.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.Classes.Order;
import com.example.bookstore.Classes.ProductOrderInfo;
import com.example.bookstore.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder>
{

    private List<Order> orderList;

    public OrdersAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_info, parent, false);
        return new OrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String articles = "";
        int size = orderList.get(position).getArticle().size();
        Log.e("count", String.valueOf(size));
        int count = 1;
        for (ProductOrderInfo product: orderList.get(position).getArticle())
        {
            if(count == size)
            {
                articles += product.getArticleName();
            }
            else
            {
                articles += product.getArticleName()+", ";
            }
            count++;
        }
        holder.tvOrderKey.setText(orderList.get(position).getOrderKey());
        holder.tvProductNames.setText(articles);
        holder.tvDate.setText(orderList.get(position).getOrderDate());
        holder.tvTotalPrice.setText(String.valueOf(orderList.get(position).getTotalPrice())+" " + orderList.get(position).getCurrency());
        holder.tvStatus.setText(orderList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductNames,tvOrderKey,tvDate,tvTotalPrice,tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductNames = itemView.findViewById(R.id.tvProductNames);
            tvOrderKey = itemView.findViewById(R.id.tvOrderKey);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
