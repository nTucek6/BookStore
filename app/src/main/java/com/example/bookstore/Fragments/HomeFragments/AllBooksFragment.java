package com.example.bookstore.Fragments.HomeFragments;

import android.app.DownloadManager;
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


public class AllBooksFragment extends Fragment implements SelectArticleListener {


    private View rootView;
    private RecyclerView recyclerViewBook;

    private LinearLayout LLLoading;
    //private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    private TabLayout tabLayoutFilter;

    private int LoadMore = 15;
    private int page = 1;

    private int sortTab = 0;

    private long articleCount;

    private DatabaseReference booksTable;

    private List<Product> listBooks = new ArrayList<>();
    private BookComicAdapter bookAdapter;
    private LinearLayoutManager layoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        booksTable = FirebaseDatabase.getInstance().getReference("books");
        GetArticleCount();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_all_books, container, false);
        recyclerViewBook = rootView.findViewById(R.id.AllBooksRecyclerView);
        //loadingPB = rootView.findViewById(R.id.idPBLoading);
        nestedSV =  rootView.findViewById(R.id.idNestedSV);
        LLLoading = rootView.findViewById(R.id.idLLLoading);
        tabLayoutFilter = rootView.findViewById(R.id.tabLayoutFilter);

        if(listBooks.size() == 0)
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
                Log.i("tab",String.valueOf(tab.getPosition()));
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

    private void ReadFromDatabase() {
        booksTable.limitToLast(page*LoadMore).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                } else {
                    if(task.getResult().getChildrenCount() == articleCount)
                    {
                        // loadingPB.setVisibility(View.INVISIBLE);
                        LLLoading.setVisibility(View.INVISIBLE);
                    }
                    listBooks = new ArrayList<>();
                    Product book = new Product();
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        book = ds.getValue(Product.class);
                        book.setKey(ds.getKey());
                        listBooks.add(book);
                    }
                    if (listBooks.size() > 0) {
                        if(sortTab == 0)
                        {
                            SortByPublished();
                        }
                        else if(sortTab == 1)
                        {
                            SortByName();
                        }
                        SetUpBookRecycleView();
                    }
                }
            }
        });
        }

    public void GetArticleCount()
    {
        booksTable.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

    private void SetUpBookRecycleView()
    {
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerViewBook.setLayoutManager(layoutManager);
        bookAdapter = new BookComicAdapter(listBooks, getActivity(), AllBooksFragment.this,"book");
        recyclerViewBook.setAdapter(bookAdapter);
    }

    @Override
    public void onArticleClicked(Product book,String type) {
        ((MainActivity) getActivity()).ArticleInfo(book,"book");
    }

    private void SortArticle(int position)
    {
        if(position == 0)
        {
            sortTab = 0;
            SortByPublished();
            SetUpBookRecycleView();
        }
        else if(position == 1)
        {
            sortTab = 1;
            SortByName();
            SetUpBookRecycleView();
        }
    }

    private void SortByName()
    {
        Collections.sort(listBooks, new Comparator<Product>(){
            public int compare(Product obj1, Product obj2) {
                // ## Ascending order
                return obj1.getName().compareToIgnoreCase(obj2.getName()); // To compare string values
            }
        });
    }

    private void SortByPublished()
    {
        Collections.sort(listBooks, new Comparator<Product>() {
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


}