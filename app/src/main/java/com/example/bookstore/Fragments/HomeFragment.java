package com.example.bookstore.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bookstore.Adapters.ViewPagerHomeAdapter;
import com.example.bookstore.Fragments.HomeFragments.AllBooksFragment;
import com.example.bookstore.Fragments.HomeFragments.AllComicsFragment;
import com.example.bookstore.Fragments.HomeFragments.FeaturedFragment;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment //implements SelectArticleListener
{

    private View rootView;
    private ViewPager2 viewPager2;
    private ViewPagerHomeAdapter viewPagerHomeAdapter;
    private TabLayout tabLayout;
    private ImageView ivSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_home, container, false);

        viewPager2 = rootView.findViewById(R.id.viewPagerHome);
        ivSearch = rootView.findViewById(R.id.ivSearch);
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

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).SearchFragment();
            }
        });


        return rootView;
    }
}