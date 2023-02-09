package com.example.bookstore.Fragments.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class AllComicsFragment extends Fragment implements SelectArticleListener {


    private View rootView;
    private RecyclerView recyclerViewComic;

    private LinearLayout LLLoading;
    private NestedScrollView nestedSV;
    private TabLayout tabLayoutFilter;

    private DatabaseReference comicsTable;

    private List<Product> listComics = new ArrayList<>();
    private BookComicAdapter comicsAdapter;
    private LinearLayoutManager layoutManager;

    private int LoadMore = 15;
    private int page = 1;

    private int sortTab = 0;

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
        nestedSV =  rootView.findViewById(R.id.idNestedSV);
        LLLoading = rootView.findViewById(R.id.idLLLoading);
        tabLayoutFilter = rootView.findViewById(R.id.tabLayoutFilter);

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

        tabLayoutFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SortArticle(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return rootView;
    }

    private void ReadFromDatabase()
    {
        Query query = null;
        if(sortTab == 0)
        {
            query = comicsTable.limitToLast(page*LoadMore);
        }
        else
        {
            query = comicsTable.orderByChild("name").limitToFirst(page*LoadMore);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                LLLoading.setVisibility(View.INVISIBLE);

                listComics = new ArrayList<>();
                Product comic = new Product();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    comic = ds.getValue(Product.class);
                    comic.setKey(ds.getKey());
                    listComics.add(comic);
                }
                if (listComics.size() > 0) {
                    Collections.reverse(listComics);

                    if(sortTab == 1)
                    {
                        SortByName();
                    }
                    SetUpComicRecycleView();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
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



    private void SortArticle(int position)
    {
        if(position == 0)
        {
            sortTab = 0;
            ReadFromDatabase();
        }
        else if(position == 1)
        {
            sortTab = 1;
            ReadFromDatabase();
        }
    }

    private void SortByName()
    {
        Collections.sort(listComics, new Comparator<Product>(){
            public int compare(Product obj1, Product obj2) {
                // ## Ascending order
                return obj1.getName().compareToIgnoreCase(obj2.getName()); // To compare string values
            }
        });
    }
    private void SortByPublished()
    {
        Collections.sort(listComics, new Comparator<Product>() {
            public int compare(Product obj1, Product obj2) {
                // ## Descending order
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    Date date1 = format.parse(obj1.getPublished());
                    Date date2 = format.parse(obj2.getPublished());
                    return date2.compareTo(date1); // To compare date values
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
    }


    @Override
    public void onArticleClicked(Product comic,String type) {
        ((MainActivity) getActivity()).ArticleInfo(comic,"comic");
    }
}