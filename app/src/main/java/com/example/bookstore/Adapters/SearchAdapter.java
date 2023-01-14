package com.example.bookstore.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.Classes.Product;
import com.example.bookstore.Interfaces.SelectArticleListener;
import com.example.bookstore.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {


    private List<Product> productList;
    SelectArticleListener listener;

    public SearchAdapter(List<Product> productList, SelectArticleListener listener)
    {
        this.productList = productList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_info, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvProductName.setText(productList.get(position).getName());

        holder.tvProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onArticleClicked(productList.get(holder.getAdapterPosition()),"all");
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);

        }
    }

}
