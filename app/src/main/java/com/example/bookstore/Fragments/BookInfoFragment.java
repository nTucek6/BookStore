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


public class BookInfoFragment extends Fragment {

    private View rootView;
    private Product book;

    private TextView tvBookName,tvBookGenre,tvBookAuthor,tvCopies,tvBookDescription,tvBookQuantity,tvBookPrice;
    private Button btnMinus,btnPlus;
    private ImageView ivBookImage,ivBack;

    private int numberCopies = 1;

    private DatabaseReference productsBooksTable;
    private Products productsBooks = new Products();

    public BookInfoFragment(Product book)
    {
        this.book = book;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productsBooksTable = FirebaseDatabase.getInstance().getReference("productsBooks");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_book_info, container, false);

        //Toast.makeText(getActivity(),book.getName(),Toast.LENGTH_SHORT).show();
        tvBookName = rootView.findViewById(R.id.tvBookName);
        //btnBack = view.findViewById(R.id.btnBack);
        ivBack = rootView.findViewById(R.id.ivBack);
        ivBookImage = rootView.findViewById(R.id.ivBookImage);
        tvBookGenre = rootView.findViewById(R.id.tvBookGenre);
        tvBookAuthor = rootView.findViewById(R.id.tvBookAuthor);
        tvBookDescription = rootView.findViewById(R.id.tvBookDescription);
        btnMinus = rootView.findViewById(R.id.btnMinus);
        btnPlus = rootView.findViewById(R.id.btnPlus);
        tvCopies = rootView.findViewById(R.id.tvCopies);
        tvBookQuantity = rootView.findViewById(R.id.tvBookQuantity);
        tvBookPrice = rootView.findViewById(R.id.tvBookPrice);

        Glide.with(getActivity()).load(book.getImageURL()).into(ivBookImage);
        tvBookName.setText(book.getName());
        tvBookGenre.setText(book.getGenres());
        tvBookAuthor.setText(book.getAuthor());
        tvBookDescription.setText(book.getDescription());

        ReadProductFromDatabase(book.getKey());

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
        //Toast.makeText(getActivity(), key, Toast.LENGTH_SHORT).show();
        Query query = productsBooksTable.orderByChild("productId").equalTo(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    productsBooks =data.getValue(Products.class);
                }
                tvBookQuantity.setText(String.valueOf(productsBooks.getQuantity()));
                tvBookPrice.setText(String.valueOf(productsBooks.getPrice()) + " " + productsBooks.getCurrency());
                //Log.e("Data",String.valueOf(productsBooks.getQuantity()));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
            }
        });
    }



}