package com.example.bookstore.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookstore.Classes.Comic;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;


public class ComicInfoFragment extends Fragment {

   private View rootView;

   private Comic comic;

    private TextView tvComicName,tvComicGenre,tvComicAuthor;
    //private Button btnBack;
    private ImageView ivComicImage,ivBack;

    public ComicInfoFragment(Comic comic)
    {
        this.comic = comic;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_comic_info, container, false);

        ivBack = rootView.findViewById(R.id.ivBack);

        ivComicImage = rootView.findViewById(R.id.ivComicImage);
        tvComicName = rootView.findViewById(R.id.tvComicName);
        tvComicGenre = rootView.findViewById(R.id.tvComicGenre);
        tvComicAuthor = rootView.findViewById(R.id.tvComicAuthor);

        Glide.with(getActivity()).load(comic.getImageURL()).into(ivComicImage);

        tvComicName.setText(comic.getName());
        tvComicGenre.setText(comic.getGenres());
        tvComicAuthor.setText(comic.getAuthor());

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).navigationBar("Home");
                ((MainActivity)getActivity()).DestroyArticleInfo();

            }
        });

        return rootView;
    }
}