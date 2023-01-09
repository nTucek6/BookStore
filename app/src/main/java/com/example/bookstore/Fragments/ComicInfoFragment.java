package com.example.bookstore.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookstore.Classes.Product;
import com.example.bookstore.Classes.Products;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ComicInfoFragment extends Fragment {

   private View rootView;

   private Product comic;

    private TextView tvComicName,tvComicGenre,tvComicAuthor,tvCopies,tvComicDescription,tvComicQuantity,tvComicPrice;
    private ImageView ivComicImage,ivBack;
    private Button btnMinus,btnPlus;

    private DatabaseReference productsComicsTable;
    private Products productsComic;

    private int numberCopies = 1;

    public ComicInfoFragment(Product comic)
    {
        this.comic = comic;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productsComicsTable = FirebaseDatabase.getInstance().getReference("productsComics");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_comic_info, container, false);

        ivBack = rootView.findViewById(R.id.ivBack);

        ivComicImage = rootView.findViewById(R.id.ivComicImage);
        tvComicName = rootView.findViewById(R.id.tvComicName);
        tvComicGenre = rootView.findViewById(R.id.tvComicGenre);
        tvComicAuthor = rootView.findViewById(R.id.tvComicAuthor);
        tvComicDescription = rootView.findViewById(R.id.tvComicDescription);
        btnMinus = rootView.findViewById(R.id.btnMinus);
        btnPlus = rootView.findViewById(R.id.btnPlus);
        tvCopies = rootView.findViewById(R.id.tvCopies);
        tvComicQuantity = rootView.findViewById(R.id.tvComicQuantity);
        tvComicPrice = rootView.findViewById(R.id.tvComicPrice);

        Glide.with(getActivity()).load(comic.getImageURL()).into(ivComicImage);

        tvComicName.setText(comic.getName());
        tvComicGenre.setText(comic.getGenres());
        tvComicAuthor.setText(comic.getAuthor());
        tvComicDescription.setText(comic.getDescription());

        ReadProductFromDatabase(comic.getKey());

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).DestroyArticleInfo();
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(tvCopies.getText().toString()) != 1)
                {
                    numberCopies -= 1;
                    tvCopies.setText(String.valueOf(numberCopies));
                }
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberCopies +=1;
                tvCopies.setText(String.valueOf(numberCopies));
            }
        });

        return rootView;
    }

    private void ReadProductFromDatabase(String key) {
        Query query = productsComicsTable.orderByChild("productId").equalTo(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    productsComic =data.getValue(Products.class);
                }
                tvComicQuantity.setText(String.valueOf(productsComic.getQuantity()));
                tvComicPrice.setText(String.valueOf(productsComic.getPrice()) + " " + productsComic.getCurrency());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

}