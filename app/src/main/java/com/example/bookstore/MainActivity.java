package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.bookstore.Classes.Product;
import com.example.bookstore.Fragments.BookInfoFragment;
import com.example.bookstore.Fragments.ComicInfoFragment;
import com.example.bookstore.Fragments.HomeFragment;
import com.example.bookstore.Fragments.ProfileFragment;
import com.example.bookstore.Fragments.ShoppingCartFragment;
import com.example.bookstore.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity  {

    ActivityMainBinding binding;

    private long pressedTime;

    private HomeFragment homeFragment;
    private ShoppingCartFragment shoppingCartFragment;
    private ProfileFragment profileFragment;

    @Override
    public void onBackPressed()
    {
        if(getSupportFragmentManager().findFragmentById(R.id.frame_layout) == homeFragment || getSupportFragmentManager().findFragmentById(R.id.frame_layout) == profileFragment || getSupportFragmentManager().findFragmentById(R.id.frame_layout) == shoppingCartFragment)
        {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.ExitDialog), Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        }
        else
        {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Removes top bar

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        homeFragment = new HomeFragment();
        shoppingCartFragment = new ShoppingCartFragment();
        profileFragment = new ProfileFragment();

        SetFragments();

        binding.NavigationBar.setOnItemSelectedListener(item ->
        {
            switch(item.getItemId())
            {
                case R.id.home:
                   // navigationBar(new HomeFragment());
                    navigationBar("Home");
                    break;
                case R.id.shoppingCart:
                    navigationBar("ShoppingCart" );
                    break;
                case R.id.user:
                   // navigationBar(new ProfileFragment());
                    navigationBar("Profile");
                    break;
            }
            return true;
        });
    }

    private void SetFragments()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, homeFragment);
        fragmentTransaction.add(R.id.frame_layout,shoppingCartFragment);
        fragmentTransaction.add(R.id.frame_layout, profileFragment);
        fragmentTransaction.detach(profileFragment);
        fragmentTransaction.detach(shoppingCartFragment);
        fragmentTransaction.commit();
    }

    public void navigationBar(String fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right);
        if(Objects.equals(fragment, "Home"))
        {
            fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
            fragmentTransaction.attach(homeFragment);
        }
        else if(Objects.equals(fragment,"ShoppingCart"))
        {
            fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
            fragmentTransaction.attach(shoppingCartFragment);
        }
        else if(Objects.equals(fragment, "Profile"))
        {
            fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
            fragmentTransaction.attach(profileFragment);
        }
        fragmentTransaction.commit();
    }

    public void ArticleInfo(Product article,String type)
    {
        if(type.equals("book"))
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
            fragmentTransaction.add(R.id.frame_layout,new BookInfoFragment(article));
            fragmentTransaction.commit();
        }
        else if(type.equals("comic"))
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,R.anim.enter_left_to_right,R.anim.exit_left_to_right);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.detach(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
            fragmentTransaction.add(R.id.frame_layout,new ComicInfoFragment(article));
            fragmentTransaction.commit();
        }
    }

    public void DestroyArticleInfo()
    {
        getSupportFragmentManager().popBackStack();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.frame_layout));
        fragmentTransaction.attach(homeFragment);
        fragmentTransaction.commit();
    }

  /*  private void ReadFromDatabase()
    {
        booksTable.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    // Log.e("firebase", "Error getting data", task.getException());
                    Toast.makeText(MainActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
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
                    bookRecyclerView = findViewById(R.id.BookRecyclerView);
                    layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,true);
                    bookRecyclerView.setLayoutManager(layoutManager);
                    // adapter = new StudentAdapter(studentList,subjectList);
                    bookAdapter = new BookAdapter(listBooks);
                    bookRecyclerView.setAdapter(bookAdapter);
                }
            }
        });
    }

    private void AddToDatabase(Book items)
    {
        booksTable.push().setValue(items).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            Toast.makeText(MainActivity.this,"Success!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
            }
        });
    } */


}