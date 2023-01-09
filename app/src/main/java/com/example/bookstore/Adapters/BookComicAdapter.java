package com.example.bookstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bookstore.Classes.Product;
import com.example.bookstore.Interfaces.SelectArticleListener;
import com.example.bookstore.R;

import java.util.List;

public class BookComicAdapter extends RecyclerView.Adapter<BookComicAdapter.ViewHolder>
    {
        private List<Product> productList;
        private Context context;
        private SelectArticleListener listener;
        private String Type;

    public BookComicAdapter(List<Product> saveProduct, Context context, SelectArticleListener listener, String type)
        {
            this.productList = saveProduct;
            this.context = context;
            this.listener =  listener;
            this.Type = type;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            String imageURL = productList.get(position).getImageURL();
            if(!imageURL.isEmpty())
            {
                Glide.with(context).load(imageURL).into(holder.imageViewComic);
            }
            holder.tvProductName.setText(productList.get(position).getName());

            holder.comicCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onArticleClicked(productList.get(holder.getAdapterPosition()),Type);
                }
            });
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_info, parent, false);
            return new BookComicAdapter.ViewHolder(view);
        }

        @Override
        public int getItemCount() {
        return productList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvProductName;
            ImageView imageViewComic;
            CardView comicCardView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                imageViewComic = itemView.findViewById(R.id.ArticleImage);
                comicCardView = itemView.findViewById(R.id.productCard);
            }
        }

    }
