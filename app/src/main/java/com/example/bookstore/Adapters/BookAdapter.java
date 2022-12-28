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
import com.example.bookstore.Classes.Book;
import com.example.bookstore.Interfaces.SelectArticleListener;
import com.example.bookstore.R;


import java.util.List;

public class BookAdapter extends
        RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> booksList;
    private Context context;
    private SelectArticleListener listener;

    public  BookAdapter(List<Book> saveBooks, Context context, SelectArticleListener listener)
    {
        this.booksList = saveBooks;
        this.context = context;
        this.listener =  listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_info, parent, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        String imageURL = booksList.get(position).getImageURL();
        if(!imageURL.isEmpty())
        {
            Glide.with(context).load(imageURL).into(holder.imageViewBook);
        }
        holder.tvBookName.setText(booksList.get(position).getName());

        holder.bookCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onBookClicked(booksList.get(holder.getAdapterPosition()));
            }
        });
    }
    @Override
    public int getItemCount() {
        return booksList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvBookName;
        ImageView imageViewBook;
        CardView bookCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            imageViewBook = itemView.findViewById(R.id.ArticleImage);
            bookCardView = itemView.findViewById(R.id.bookCard);
        }
    }

}