package com.example.bookstore.Fragments.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.Adapters.ProductAdapter;
import com.example.bookstore.Adapters.SearchAdapter;
import com.example.bookstore.Classes.Product;
import com.example.bookstore.Fragments.ShoppingCartFragment;
import com.example.bookstore.Interfaces.SelectArticleListener;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchFragment extends Fragment implements SelectArticleListener {

    private View rootView;
    private TextView tvCancel;
    private EditText searchArticle;
    private RecyclerView rvSearch;

    private DatabaseReference booksTable;
    private DatabaseReference comicsTable;

    private LinearLayoutManager layoutManager;
    private SearchAdapter searchAdapter;

    private List<Product> productList = new ArrayList<>();

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
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        tvCancel = rootView.findViewById(R.id.tvCancel);
        searchArticle = rootView.findViewById(R.id.etSearch);
        rvSearch = rootView.findViewById(R.id.rvSearch);

        searchArticle.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchArticle.setSingleLine();

        searchArticle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (searchArticle.getText().length() == 0) {
                        productList = new ArrayList<>();
                        SetUpRecyclerView();
                    }
                }
                return false;
            }
        });

        searchArticle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().isEmpty()) {
                    SearchArticle(s.toString());
                } else {
                    productList = new ArrayList<>();
                    SetUpRecyclerView();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).PopBackStack();
            }
        });

        return rootView;
    }

    private void SearchArticle(String search) {
        Query bookQuery = booksTable.orderByChild("name");
        bookQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Product product = new Product();
                    product = data.getValue(Product.class);
                    product.setKey(data.getKey());

                    if (product.getName().toLowerCase().contains(search.toLowerCase())) {
                        productList.add(product);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query comicQuery = comicsTable.orderByChild("name");
        comicQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Product product = new Product();
                    product = data.getValue(Product.class);
                    product.setKey(data.getKey());

                    if (product.getName().toLowerCase().contains(search.toLowerCase())) {
                        productList.add(product);
                    }
                }
                SetUpRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetUpRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSearch.setLayoutManager(layoutManager);
        searchAdapter = new SearchAdapter(productList, SearchFragment.this);
        rvSearch.setAdapter(searchAdapter);
    }


    @Override
    public void onArticleClicked(Product article, String type) {
        ((MainActivity) getActivity()).ArticleInfo(article, type);
    }
}