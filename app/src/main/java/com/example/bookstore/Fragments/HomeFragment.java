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
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookstore.Adapters.BookAdapter;
import com.example.bookstore.Adapters.ComicAdapter;
import com.example.bookstore.Adapters.ViewPagerHomeAdapter;
import com.example.bookstore.Classes.Book;
import com.example.bookstore.Classes.Comic;
import com.example.bookstore.Fragments.HomeFragments.AllBooksFragment;
import com.example.bookstore.Fragments.HomeFragments.AllComicsFragment;
import com.example.bookstore.Fragments.HomeFragments.FeaturedFragment;
import com.example.bookstore.Interfaces.SelectArticleListener;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment //implements SelectArticleListener
{

    private View rootView;
    private ViewPager2 viewPager2;
    private ViewPagerHomeAdapter viewPagerHomeAdapter;
    private TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_home, container, false);

        viewPager2 = rootView.findViewById(R.id.viewPagerHome);
        viewPager2.setUserInputEnabled(false);
        viewPagerHomeAdapter = new ViewPagerHomeAdapter(getActivity());

        viewPagerHomeAdapter.add(new FeaturedFragment(),getString(R.string.btnFeatured));
        viewPagerHomeAdapter.add(new AllBooksFragment(),getString(R.string.btnAllBooks));
        viewPagerHomeAdapter.add(new AllComicsFragment(),getString(R.string.btnAllComics));

        viewPager2.setAdapter(viewPagerHomeAdapter);

        tabLayout = rootView.findViewById(R.id.homeTabLayout);
        new TabLayoutMediator(
                tabLayout,
                viewPager2,
               new TabLayoutMediator.TabConfigurationStrategy()
               {
                   @Override
                   public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    tab.setText(viewPagerHomeAdapter.getPageTitle(position));
                   }
               }
        ).attach();


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);


            }
        });


        return rootView;
    }
}