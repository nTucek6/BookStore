package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.bookstore.Classes.Book;
import com.example.bookstore.Fragments.BookInfoFragment;
import com.example.bookstore.Fragments.HomeFragment;
import com.example.bookstore.Fragments.ProfileFragment;
import com.example.bookstore.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity  {

    ActivityMainBinding binding;

    private long pressedTime;

    @Override
    public void onBackPressed()
    {
    if (pressedTime + 2000 > System.currentTimeMillis()) {
        super.onBackPressed();
        finish();
        } else {
        Toast.makeText(getBaseContext(), getString(R.string.ExitDialog), Toast.LENGTH_SHORT).show();
    }
     pressedTime = System.currentTimeMillis();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Removes top bar
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navigationBar(new HomeFragment());

        binding.NavigationBar.setOnItemSelectedListener(item ->
        {
            switch(item.getItemId())
            {
                case R.id.home:
                    navigationBar(new HomeFragment());
                    break;
                case R.id.user:
                    navigationBar(new ProfileFragment());
                    break;
               /* case R.id.settings:
                    //navigationBar(new );
                    break; */
            }
            return true;
        });

        //booksTable = FirebaseDatabase.getInstance().getReference("books");
        //ReadFromDatabase();

    }

    public void navigationBar(Fragment fragment)
    {
       //FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    public void BookInfo(Book book)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new BookInfoFragment(book));
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