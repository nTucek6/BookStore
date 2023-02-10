package com.example.bookstore.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.Classes.Order;
import com.example.bookstore.Classes.ProductOrderInfo;
import com.example.bookstore.Interfaces.SelectOrderListener;
import com.example.bookstore.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder>
{

    private List<Order> orderList;
    private SelectOrderListener selectOrderListener;



    public OrdersAdapter(List<Order> orderList,SelectOrderListener selectOrderListener) {
        this.orderList = orderList;
        this.selectOrderListener = selectOrderListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_info, parent, false);
        return new OrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

      /*  String articles = "";
        int size = orderList.get(position).getArticle().size();
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
        holder.tvAddress.setText(orderList.get(position).getAddress());
        holder.tvCity.setText(orderList.get(position).getCity());
        holder.tvPaymentType.setText(orderList.get(position).getOrderPaymentType()); */

        holder.llOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOrderListener.onArticleClicked(orderList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //TextView tvProductNames,tvOrderKey,tvDate,tvTotalPrice,tvStatus,tvAddress,tvCity,tvPaymentType;
        LinearLayout llOrderLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

         /*   tvProductNames = itemView.findViewById(R.id.tvProductNames);
            tvOrderKey = itemView.findViewById(R.id.tvOrderKey);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvPaymentType = itemView.findViewById(R.id.tvPaymentType); */
            llOrderLayout = itemView.findViewById(R.id.llOrderLayout);
        }
    }
}
