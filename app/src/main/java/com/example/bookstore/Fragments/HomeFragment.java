package com.example.bookstore.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookstore.Adapters.BookAdapter;
import com.example.bookstore.Classes.Book;
import com.example.bookstore.Interfaces.SelectBookListener;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements SelectBookListener {

    private DatabaseReference booksTable;
    private RecyclerView bookRecyclerView;
    private LinearLayoutManager layoutManager;

    List<Book> listBooks = new ArrayList<>();
    BookAdapter bookAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        booksTable = FirebaseDatabase.getInstance().getReference("books");
        bookRecyclerView = view.findViewById(R.id.BookRecyclerViewLatest);
        ReadFromDatabase();
    }

    private void ReadFromDatabase()
    {
        //limitToLast(4)
        booksTable.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    // Log.e("firebase", "Error getting data", task.getException());
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
                    layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
                    bookRecyclerView.setLayoutManager(layoutManager);
                    bookAdapter = new BookAdapter(listBooks, getActivity(),HomeFragment.this);
                    bookRecyclerView.setAdapter(bookAdapter);
                }
            }
        });
    }

    @Override
    public void onBookClicked(Book book) {
       // Toast.makeText(getActivity(),book.getName(),Toast.LENGTH_SHORT).show();

        ((MainActivity) getActivity()).BookInfo(book);
    }
}