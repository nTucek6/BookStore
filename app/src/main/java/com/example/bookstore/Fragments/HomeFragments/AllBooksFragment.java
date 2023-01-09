package com.example.bookstore.Fragments.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookstore.Adapters.BookComicAdapter;
import com.example.bookstore.Classes.Product;
import com.example.bookstore.Interfaces.SelectArticleListener;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class AllBooksFragment extends Fragment implements SelectArticleListener {


    private View rootView;
    private RecyclerView recyclerViewBook;

    private int LoadMore = 10;
    private int page = 1;

    private DatabaseReference booksTable;

    private List<Product> listBooks = new ArrayList<>();
    private BookComicAdapter bookAdapter;
    private LinearLayoutManager layoutManager;


  /*  @Override
    public void onResume() {
        super.onResume();
        rootView.requestLayout();
    } */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        booksTable = FirebaseDatabase.getInstance().getReference("books");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_books, container, false);
        recyclerViewBook = rootView.findViewById(R.id.AllBooksRecyclerView);

        if(listBooks.size() == 0)
        {
            ReadFromDatabase();
        }

        return rootView;
    }



    private void ReadFromDatabase() {
        //
        booksTable.limitToLast(page*LoadMore).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                } else {
                    Product book = new Product();
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        book = ds.getValue(Product.class);
                        book.setKey(ds.getKey());
                        listBooks.add(book);
                    }

                    if (listBooks.size() > 0) {
                        SetUpBookRecycleView();
                    }

                }
            }
        });
    }


    private void SetUpBookRecycleView()
    {
        //Log.e("WhatActivity",getActivity().toString());
        layoutManager = new GridLayoutManager(getActivity(), 3);

        recyclerViewBook.setLayoutManager(layoutManager);
        bookAdapter = new BookComicAdapter(listBooks, getActivity(), AllBooksFragment.this,"book");
        recyclerViewBook.setAdapter(bookAdapter);
    }

    @Override
    public void onArticleClicked(Product book,String type) {
        ((MainActivity) getActivity()).ArticleInfo(book,"book");
    }

}