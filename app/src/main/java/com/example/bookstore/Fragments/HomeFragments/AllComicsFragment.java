package com.example.bookstore.Fragments.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookstore.Adapters.BookComicAdapter;
import com.example.bookstore.Classes.Product;
import com.example.bookstore.Interfaces.SelectArticleListener;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class AllComicsFragment extends Fragment implements SelectArticleListener {


    private View rootView;
    private RecyclerView recyclerViewComic;

    private LinearLayout LLLoading;
    //private ProgressBar loadingPB;
    private NestedScrollView nestedSV;

    private DatabaseReference comicsTable;

    private List<Product> listComics = new ArrayList<>();
    private BookComicAdapter comicsAdapter;
    private LinearLayoutManager layoutManager;

    private int LoadMore = 15;
    private int page = 1;

    private long articleCount;

   /* @Override
    public void onResume() {
        super.onResume();
        rootView.requestLayout();
    } */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comicsTable = FirebaseDatabase.getInstance().getReference("comics");
        GetArticleCount();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_all_comics, container, false);

        recyclerViewComic = rootView.findViewById(R.id.AllComicsRecyclerView);
        //loadingPB = rootView.findViewById(R.id.idPBLoading);
        nestedSV =  rootView.findViewById(R.id.idNestedSV);
        LLLoading = rootView.findViewById(R.id.idLLLoading);

        if(listComics.size() == 0)
        {
            ReadFromDatabase();
        }

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    //loadingPB.setVisibility(View.VISIBLE);
                    LLLoading.setVisibility(View.VISIBLE);
                    ReadFromDatabase();

                }
            }
        });

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
                    if(task.getResult().getChildrenCount() == articleCount)
                    {
                        //loadingPB.setVisibility(View.INVISIBLE);
                        LLLoading.setVisibility(View.INVISIBLE);
                    }

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
                    }
                }
            }
        });
    }

    private void SetUpComicRecycleView()
    {
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerViewComic.setLayoutManager(layoutManager);
        comicsAdapter = new BookComicAdapter(listComics, getActivity(),AllComicsFragment.this,"comic");
        recyclerViewComic.setAdapter(comicsAdapter);
    }

    public void GetArticleCount()
    {
        comicsTable.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                articleCount = task.getResult().getChildrenCount();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    @Override
    public void onArticleClicked(Product comic,String type) {
        ((MainActivity) getActivity()).ArticleInfo(comic,"comic");
    }
}