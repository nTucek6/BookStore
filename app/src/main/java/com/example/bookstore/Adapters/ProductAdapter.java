package com.example.bookstore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore.Classes.Product;
import com.example.bookstore.Classes.Products;
import com.example.bookstore.Classes.ShoppingCart;
import com.example.bookstore.Interfaces.DeleteArticleListener;
import com.example.bookstore.Interfaces.SelectArticleListener;
import com.example.bookstore.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;
    private List<ShoppingCart> shoppingCartList;
    private List<Products> products;
    private DeleteArticleListener listener;

    public ProductAdapter(List<Product> productList, Context context, List<ShoppingCart> shoppingCartList, List<Products> products, DeleteArticleListener listener)
    {
        this.productList = productList;
        this.context = context;
        this.shoppingCartList = shoppingCartList;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppingcart_info, parent, false);

        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        String imageURL = productList.get(position).getImageURL();
        if(!imageURL.isEmpty())
        {
            Glide.with(context).load(imageURL).into(holder.imageViewBook);
        }
        holder.tvBookName.setText(productList.get(position).getName());

        for (ShoppingCart item: shoppingCartList) {
            if(productList.get(position).getKey().equals(item.getProductId()))
            {
               holder.productQuantity.setText(context.getString(R.string.QuantityToBuy) +" "+String.valueOf(item.getQuantityToBuy()));
            }
        }

        for (Products item: products) {
            if(productList.get(position).getKey().equals(item.getProductId()))
            {
                holder.productPrice.setText(context.getString(R.string.tvPrice)+" " + String.valueOf(item.getPrice())+" " +item.getCurrency());
            }
        }

        holder.btnRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onArticleClicked(productList.get(holder.getAdapterPosition()),holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvBookName,productPrice,productQuantity;
        ImageView imageViewBook;
        Button btnRemoveFromCart;
       // CardView bookCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.productName);
            imageViewBook = itemView.findViewById(R.id.productImage);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            btnRemoveFromCart = itemView.findViewById(R.id.btnRemoveFromCart);
        }
    }

}
