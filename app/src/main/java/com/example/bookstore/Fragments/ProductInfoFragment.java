package com.example.bookstore.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookstore.Classes.Product;
import com.example.bookstore.Classes.Products;
import com.example.bookstore.Classes.ShoppingCart;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProductInfoFragment extends Fragment {

    private View rootView;

    private Product product;

    private final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private TextView tvProductName,tvProductGenre,tvProductAuthor,tvCopies,tvProductDescription,tvProductQuantity,tvProductPrice;
    private ImageView ivProductImage,ivBack;
    private Button btnMinus,btnPlus,btnAddToCartProduct;

    private DatabaseReference productsTable,shoppingCartTable;
    private Products productsArticle;
    private String Type;

    private int numberCopies = 1;

    public ProductInfoFragment(Product product, String type)
    {
        this.product = product;
        this.Type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shoppingCartTable = FirebaseDatabase.getInstance().getReference("shoppingCart");

        if(!Type.equals("all"))
        {
        if(Type.equals("book"))
        {
            productsTable = FirebaseDatabase.getInstance().getReference("productsBooks");
        }
        else if(Type.equals("comic"))
        {
            productsTable = FirebaseDatabase.getInstance().getReference("productsComics");
        }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_product_info, container, false);

        ivBack = rootView.findViewById(R.id.ivBack);

        ivProductImage = rootView.findViewById(R.id.ivProductImage);
        tvProductName = rootView.findViewById(R.id.tvProductName);
        tvProductGenre = rootView.findViewById(R.id.tvProductGenre);
        tvProductAuthor = rootView.findViewById(R.id.tvProductAuthor);
        tvProductDescription = rootView.findViewById(R.id.tvProductDescription);
        btnMinus = rootView.findViewById(R.id.btnMinus);
        btnPlus = rootView.findViewById(R.id.btnPlus);
        tvCopies = rootView.findViewById(R.id.tvCopies);
        //tvProductQuantity = rootView.findViewById(R.id.tvProductQuantity);
        tvProductPrice = rootView.findViewById(R.id.tvProductPrice);
        btnAddToCartProduct = rootView.findViewById(R.id.btnAddToCartProduct);

        Glide.with(getActivity()).load(product.getImageURL()).into(ivProductImage);

        tvProductName.setText(product.getName());
        tvProductGenre.setText(product.getGenres());
        tvProductAuthor.setText(product.getAuthor());
        tvProductDescription.setText(product.getDescription());

        if(!Type.equals("all"))
        {
            ReadProductFromDatabase(product.getKey());
        }
        else
        {
            SearchProductFromDatabase(product.getKey());
        }


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).DestroyArticleInfo();
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(tvCopies.getText().toString()) != 1)
                {
                    numberCopies -= 1;
                    tvCopies.setText(String.valueOf(numberCopies));
                }
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberCopies +=1;
                tvCopies.setText(String.valueOf(numberCopies));
            }
        });

        btnAddToCartProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCart cart = new ShoppingCart();
                cart.setProductId(product.getKey());
                cart.setProductType(Type);
                cart.setQuantityToBuy(Integer.parseInt(tvCopies.getText().toString()));
                cart.setUserUID(userUID);

                CheckUserCart(cart);

                //AddToDatabase(cart);
            }
        });


        return rootView;
    }

    private void ReadProductFromDatabase(String key) {
        Query query = productsTable.orderByChild("productId").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    productsArticle =data.getValue(Products.class);
                }
               // tvProductQuantity.setText(String.valueOf(productsArticle.getQuantity()));
                tvProductPrice.setText(String.valueOf(productsArticle.getPrice()) + " " + productsArticle.getCurrency());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void AddToDatabase(ShoppingCart cart)
    {
        shoppingCartTable.push().setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(),"Success!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Failed!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CheckUserCart(ShoppingCart item)
    {
        Query query = shoppingCartTable.orderByChild("userUID").equalTo(userUID);
        final int[] count = {0};
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    ShoppingCart cart = new ShoppingCart();
                    cart = data.getValue(ShoppingCart.class);
                    if(cart.getProductId().equals(product.getKey()))
                    {
                        count[0]++;
                    }
                }
                if(count[0] == 0)
                {
                    AddToDatabase(item);
                }
                else
                {
                    Toast.makeText(getActivity(),"Already in cart!",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SearchProductFromDatabase(String key) {
        Query bookQuery = FirebaseDatabase.getInstance().getReference("productsBooks").orderByChild("productId").equalTo(key);
        bookQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    productsArticle =data.getValue(Products.class);
                }
                if(productsArticle != null)
                {
                    Type = "book";
                    //tvProductQuantity.setText(String.valueOf(productsArticle.getQuantity()));
                    tvProductPrice.setText(String.valueOf(productsArticle.getPrice()) + " " + productsArticle.getCurrency());
                }
                else
                {
                    GetComicSearch(key);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void GetComicSearch(String key)
    {
        Query comicQuery = FirebaseDatabase.getInstance().getReference("productsComics").orderByChild("productId").equalTo(key);
        comicQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    productsArticle =data.getValue(Products.class);
                }

                Type = "comic";
                //tvProductQuantity.setText(String.valueOf(productsArticle.getQuantity()));
                tvProductPrice.setText(String.valueOf(productsArticle.getPrice()) + " " + productsArticle.getCurrency());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }




}