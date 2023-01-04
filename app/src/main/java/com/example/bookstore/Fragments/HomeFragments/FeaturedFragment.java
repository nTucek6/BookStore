package com.example.bookstore.Fragments.HomeFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookstore.Adapters.BookAdapter;
import com.example.bookstore.Adapters.ComicAdapter;
import com.example.bookstore.Classes.Book;
import com.example.bookstore.Classes.Comic;
import com.example.bookstore.Fragments.HomeFragment;
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


public class FeaturedFragment extends Fragment implements SelectArticleListener {


    private DatabaseReference booksTable;
    private DatabaseReference comicsTable;
    private RecyclerView bookRecyclerView,comicRecyclerView;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;

    private View rootView;

    private List<Book> listBooks = new ArrayList<>();
    private List<Comic> listComics = new ArrayList<>();
    private BookAdapter bookAdapter;
    private ComicAdapter comicAdapter;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        booksTable = FirebaseDatabase.getInstance().getReference("books");
        comicsTable = FirebaseDatabase.getInstance().getReference("comics");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
        rootView =  inflater.inflate(R.layout.fragment_featured, container, false);

        bookRecyclerView = rootView.findViewById(R.id.BookRecyclerViewLatest);
        comicRecyclerView = rootView.findViewById(R.id.ComicRecyclerViewLatest);
        progressBar = rootView.findViewById(R.id.progressBar);

       if(listBooks.size() > 0)
        {
            SetUpBookRecycleView();
            SetUpComicRecycleView();
            progressBar.setVisibility(View.INVISIBLE);
        }
        else
        {
            ReadFromDatabase();
            //progressBar.setVisibility(View.INVISIBLE);

        }
        return rootView;
    }

    private void ReadFromDatabase()
    {
        booksTable.limitToLast(6).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(),"Failed!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Book book = new Book();
                    for(DataSnapshot ds : task.getResult().getChildren())
                    {
                        book.setKey(ds.getKey());
                        book = ds.getValue(Book.class);
                        listBooks.add(book);
                    }

                    if(listBooks.size()>0)
                    {
                        SetUpBookRecycleView();
                    }

                }
            }
        });

        comicsTable.limitToLast(6).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(),"Failed!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Comic comic = new Comic();
                    for(DataSnapshot ds : task.getResult().getChildren())
                    {
                        comic.setKey(ds.getKey());
                        comic = ds.getValue(Comic.class);
                        listComics.add(comic);
                    }
                    if(listComics.size() > 0)
                    {
                        SetUpComicRecycleView();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onBookClicked(Book book) {
        ((MainActivity) getActivity()).BookInfo(book);
    }

    @Override
    public void onComicClicked(Comic comic) {
        ((MainActivity) getActivity()).ComicInfo(comic);
    }

    private void SetUpBookRecycleView()
    {
        //Log.e("WhatActivity",getActivity().toString());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        bookRecyclerView.setLayoutManager(layoutManager);
        bookAdapter = new BookAdapter(listBooks, getActivity(), FeaturedFragment.this);
        bookRecyclerView.setAdapter(bookAdapter);
    }

    private void SetUpComicRecycleView()
    {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        comicRecyclerView.setLayoutManager(layoutManager);
        comicAdapter = new ComicAdapter(listComics, getActivity(),FeaturedFragment.this);
        comicRecyclerView.setAdapter(comicAdapter);
    }
}