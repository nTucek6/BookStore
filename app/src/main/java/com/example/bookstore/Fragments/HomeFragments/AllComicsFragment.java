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

import com.example.bookstore.Adapters.BookAdapter;
import com.example.bookstore.Adapters.ComicAdapter;
import com.example.bookstore.Classes.Book;
import com.example.bookstore.Classes.Comic;
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


public class AllComicsFragment extends Fragment implements SelectArticleListener {


    private View rootView;
    private RecyclerView recyclerViewComic;

    private DatabaseReference comicsTable;

    private List<Comic> listComics = new ArrayList<>();
    private ComicAdapter comicsAdapter;
    private LinearLayoutManager layoutManager;

    private int LoadMore = 10;
    private int page = 1;

   /* @Override
    public void onResume() {
        super.onResume();
        rootView.requestLayout();
    } */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comicsTable = FirebaseDatabase.getInstance().getReference("comics");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_all_comics, container, false);

        recyclerViewComic = rootView.findViewById(R.id.AllComicsRecyclerView);

        if(listComics.size() == 0)
        {
            ReadFromDatabase();
        }

        return rootView;
    }

    private void ReadFromDatabase()
    {
        //limitToLast(6)
        comicsTable.limitToLast(page*LoadMore).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(),"Failed!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Comic comic = new Comic();
                    for(DataSnapshot ds : task.getResult().getChildren())
                    {
                        comic = ds.getValue(Comic.class);
                        comic.setKey(ds.getKey());
                        listComics.add(comic);
                    }
                    if(listComics.size() > 0)
                    {
                        SetUpComicRecycleView();

                    }
                }
            }
        });
    }

    private void SetUpComicRecycleView()
    {
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerViewComic.setLayoutManager(layoutManager);
        comicsAdapter = new ComicAdapter(listComics, getActivity(),AllComicsFragment.this);
        recyclerViewComic.setAdapter(comicsAdapter);
    }


    @Override
    public void onBookClicked(Book book) {

    }

    @Override
    public void onComicClicked(Comic comic) {
        ((MainActivity) getActivity()).ComicInfo(comic);
    }
}