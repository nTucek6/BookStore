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
import com.example.bookstore.Classes.Comic;
import com.example.bookstore.Interfaces.SelectArticleListener;
import com.example.bookstore.R;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder>
{

    private List<Comic> comicsList;
    private Context context;
    private SelectArticleListener listener;

    public  ComicAdapter(List<Comic> saveComics, Context context, SelectArticleListener listener)
    {
        this.comicsList = saveComics;
        this.context = context;
        this.listener =  listener;
    }


    @NonNull
    @Override
    public ComicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comic_info, parent, false);
        return new ComicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicAdapter.ViewHolder holder, int position) {

        String imageURL = comicsList.get(position).getImageURL();
        if(!imageURL.isEmpty())
        {
            Glide.with(context).load(imageURL).into(holder.imageViewComic);
        }
        holder.tvComicName.setText(comicsList.get(position).getName());

        holder.comicCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onComicClicked(comicsList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return comicsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvComicName;
        ImageView imageViewComic;
        CardView comicCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComicName = itemView.findViewById(R.id.tvComicName);
            imageViewComic = itemView.findViewById(R.id.ArticleImage);
            comicCardView = itemView.findViewById(R.id.comicCard);
        }
    }


}
