package com.example.bookstore.Fragments.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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


public class FeaturedFragment extends Fragment implements SelectArticleListener {


    private DatabaseReference booksTable;
    private DatabaseReference comicsTable;
    private RecyclerView bookRecyclerView,comicRecyclerView;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;

    private View rootView;

    private List<Product> listBooks = new ArrayList<>();
    private List<Product> listComics = new ArrayList<>();
    private BookComicAdapter bookAdapter;
    private BookComicAdapter comicAdapter;


   /* @Override
     public void onResume() {
        super.onResume();
        rootView.requestLayout();
    } */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        booksTable = FirebaseDatabase.getInstance().getReference("books");
        comicsTable = FirebaseDatabase.getInstance().getReference("comics");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_featured, container, false);

        bookRecyclerView = rootView.findViewById(R.id.BookRecyclerViewLatest);
        comicRecyclerView = rootView.findViewById(R.id.ComicRecyclerViewLatest);
        progressBar = rootView.findViewById(R.id.progressBar);

        if(listBooks.size() == 0 && listComics.size() == 0)
        {
            ReadFromDatabase();
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
                    for(DataSnapshot ds : task.getResult().getChildren())
                    {
                        Product book = new Product();
                        book = ds.getValue(Product.class);
                        book.setKey(ds.getKey());
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
                    for(DataSnapshot ds : task.getResult().getChildren())
                    {
                        Product comic = new Product();
                        comic = ds.getValue(Product.class);
                        comic.setKey(ds.getKey());
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
    public void onArticleClicked(Product book,String type) {
        ((MainActivity) getActivity()).ArticleInfo(book,type);
    }

    private void SetUpBookRecycleView()
    {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        bookRecyclerView.setLayoutManager(layoutManager);
        bookAdapter = new BookComicAdapter(listBooks, getActivity(), FeaturedFragment.this,"book");
        bookRecyclerView.setAdapter(bookAdapter);
    }

    private void SetUpComicRecycleView()
    {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        comicRecyclerView.setLayoutManager(layoutManager);
        comicAdapter = new BookComicAdapter(listComics, getActivity(),FeaturedFragment.this,"comic");
        comicRecyclerView.setAdapter(comicAdapter);
    }
}