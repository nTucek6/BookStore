package com.example.bookstore.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookstore.Classes.Book;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;


public class BookInfoFragment extends Fragment {

    private Book book;

    private TextView tvBookName,tvBookGenre,tvBookAuthor;
    private Button btnBack;
    private ImageView ivBookImage;


    public BookInfoFragment(Book book)
    {
        this.book = book;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_info, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        //Toast.makeText(getActivity(),book.getName(),Toast.LENGTH_SHORT).show();
        tvBookName = view.findViewById(R.id.tvBookName);
        btnBack = view.findViewById(R.id.btnBack);
        ivBookImage = view.findViewById(R.id.ivBookImage);
        tvBookGenre = view.findViewById(R.id.tvBookGenre);
        tvBookAuthor = view.findViewById(R.id.tvBookAuthor);


        Glide.with(getActivity()).load(book.getImageURL()).into(ivBookImage);
        tvBookName.setText(book.getName());
        tvBookGenre.setText(book.getGenres());
        tvBookAuthor.setText(book.getAuthor());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).navigationBar(new HomeFragment());

            }
        });

    }

}