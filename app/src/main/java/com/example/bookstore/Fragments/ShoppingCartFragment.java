package com.example.bookstore.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.Adapters.ProductAdapter;
import com.example.bookstore.Classes.Product;
import com.example.bookstore.Classes.Products;
import com.example.bookstore.Classes.ShoppingCart;
import com.example.bookstore.Interfaces.DeleteArticleListener;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ShoppingCartFragment extends Fragment implements DeleteArticleListener {

    private View rootView;

    private final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference shoppingCartTable;
    private DatabaseReference booksTable;
    private DatabaseReference comicsTable;
    private DatabaseReference productsBookTable;
    private DatabaseReference productsComicsTable;
    private List<ShoppingCart> shoppingCartList = new ArrayList<>();
    private List<Product> listsProduct = new ArrayList<>();
    private List<Products> listsProducts = new ArrayList<>();

    private RecyclerView productRecyclerView;
    private LinearLayoutManager layoutManager;
    private ProductAdapter productAdapter;
    private LinearLayout priceDetailsLayout;
    private TextView tvItemsCount,tvToPay;
    private Button btnContinue;
    private LinearLayout llCartEmpty;

    @Override
    public void onResume()
    {
        super.onResume();
      if(productAdapter != null)
        {
            productAdapter.notifyDataSetChanged();
        }

      /* if(productRecyclerView != null && shoppingCartList.size() > 0)
        {
            SetUpRecyclerView();
            SetUpInfo();
        }*/

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingCartTable = FirebaseDatabase.getInstance().getReference("shoppingCart");
        booksTable = FirebaseDatabase.getInstance().getReference("books");
        comicsTable = FirebaseDatabase.getInstance().getReference("comics");
        productsBookTable = FirebaseDatabase.getInstance().getReference("productsBooks");
        productsComicsTable = FirebaseDatabase.getInstance().getReference("productsComics");
        GetUserCart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_shopping_cart_fragment, container, false);
        productRecyclerView = rootView.findViewById(R.id.rvCartItems);
        priceDetailsLayout = rootView.findViewById(R.id.priceDetailsLayout);
        tvItemsCount = rootView.findViewById(R.id.tvItemsCount);
        tvToPay = rootView.findViewById(R.id.tvToPay);
        btnContinue = rootView.findViewById(R.id.btnContinue);
        llCartEmpty = rootView.findViewById(R.id.llCartEmpty);
        llCartEmpty.setVisibility(View.INVISIBLE);
        priceDetailsLayout.setVisibility(View.INVISIBLE);

        if(shoppingCartList.size() > 0)
        {
            SetUpRecyclerView();
            SetUpInfo();
            priceDetailsLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            llCartEmpty.setVisibility(View.VISIBLE);
        }

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).ProceedToPayment(listsProducts,shoppingCartList,listsProduct); //listsProducts
            }
        });
        return rootView;
    }

    private void GetUserCart()
    {
        Query query = shoppingCartTable.orderByChild("userUID").equalTo(userUID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                shoppingCartList = new ArrayList<>();
                listsProduct = new ArrayList<>();
                listsProducts = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    ShoppingCart shoppingCart =data.getValue(ShoppingCart.class);

                    shoppingCartList.add(shoppingCart);
                }
                if(shoppingCartList.size() > 0)
                {
                   // GetArticles();
                    GetBooksOrComics();
                    GetProducts();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

  /*  private void GetArticles()
    {
        for (ShoppingCart item: shoppingCartList)
        {
            if(item.getProductType().equals("book"))
            {
                GetBooksOrComics(item.getProductId(),booksTable);
                GetProducts(item.getProductId(),productsBookTable);
            }
            else if (item.getProductType().equals("comic"))
            {
                GetBooksOrComics(item.getProductId(),comicsTable);
                GetProducts(item.getProductId(),productsComicsTable);
            }
        }
    }

    private void GetBooksOrComics(String articleKey, DatabaseReference reference)
    {
        Query query = reference.orderByKey().equalTo(articleKey);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Product product = new Product();
                    product = data.getValue(Product.class);
                    product.setKey(data.getKey());
                    listsProduct.add(product);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void GetProducts(String productId, DatabaseReference reference) {
        Query query = reference.orderByChild("productId").equalTo(productId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Products product = new Products();
                    product = data.getValue(Products.class);
                    //product.setKey(data.getKey());
                    listsProducts.add(product);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    } */

    private void GetBooksOrComics()
    {
        booksTable.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(),"Failed!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    for(DataSnapshot ds : task.getResult().getChildren())
                    {
                        Product book = new Product();
                        book = ds.getValue(Product.class);
                        book.setKey(ds.getKey());
                        for (ShoppingCart cart: shoppingCartList)
                        {
                            if(cart.getProductId().equals(book.getKey()))
                            {
                                listsProduct.add(book);
                            }
                        }
                    }


                }
            }
        });

        comicsTable.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(),"Failed!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    for(DataSnapshot ds : task.getResult().getChildren())
                    {
                        Product comic = new Product();
                        comic = ds.getValue(Product.class);
                        comic.setKey(ds.getKey());
                        for (ShoppingCart cart: shoppingCartList)
                        {
                            if(cart.getProductId().equals(comic.getKey()))
                            {
                                listsProduct.add(comic);
                            }
                        }
                    }
                }
            }
        });

    }

    public void GetProducts()
    {
        productsBookTable.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(),"Failed!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    for(DataSnapshot ds : task.getResult().getChildren())
                    {
                        Products book = new Products();
                        book = ds.getValue(Products.class);
                        for (ShoppingCart cart: shoppingCartList)
                        {
                            if(cart.getProductId().equals(book.getProductId()))
                            {
                                listsProducts.add(book);
                            }
                        }
                    }
                }
            }
        });

        productsComicsTable.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(),"Failed!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    for(DataSnapshot ds : task.getResult().getChildren())
                    {
                        Products comic = new Products();
                        comic = ds.getValue(Products.class);
                        for (ShoppingCart cart: shoppingCartList)
                        {
                            if(cart.getProductId().equals(comic.getProductId()))
                            {
                                listsProducts.add(comic);
                            }
                        }
                    }
                }
            }
        });
    }


    private void SetUpRecyclerView()
    {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        productRecyclerView.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(listsProduct, getActivity(),shoppingCartList,listsProducts,ShoppingCartFragment.this);
        productRecyclerView.setAdapter(productAdapter);
    }

    @Override
    public void onArticleClicked(Product product, int position) {

        new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme)
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //Query query = shoppingCartTable.orderByChild("productId").equalTo(product.getKey());
                        Query query = shoppingCartTable.orderByChild("userUID").equalTo(userUID);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data: snapshot.getChildren()) {

                                    ShoppingCart cart = new ShoppingCart();
                                    cart = data.getValue(ShoppingCart.class);

                                    if(cart.getUserUID().equals(userUID) && product.getKey().equals(cart.getProductId()))
                                    {
                                        shoppingCartTable.child(data.getKey()).removeValue();
                                    }
                                }
                                listsProduct.remove(position);
                                RemoveFromCartAndProducts(product.getKey());
                                SetUpRecyclerView();
                                if(listsProduct.size() == 0)
                                {
                                    priceDetailsLayout.setVisibility(View.INVISIBLE);
                                    llCartEmpty.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    SetUpInfo();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    private void SetUpInfo()
    {
        float price = 0;
        int quantity = 0;

        for (ShoppingCart item : shoppingCartList)
        {
            quantity += item.getQuantityToBuy();

            for (Products product : listsProducts)
            {
                if(product.getProductId().equals(item.getProductId()))
                {
                    price+= product.getPrice()*item.getQuantityToBuy();
                }
            }

        }

        tvItemsCount.setText(getString(R.string.Items)+"("+quantity+")");
        tvToPay.setText(getString(R.string.ToPay)+": "+price + " EUR");
    }

    private void RemoveFromCartAndProducts(String key)
    {
        for (int i = 0; i < shoppingCartList.size();i++)
        {
            if(shoppingCartList.get(i).getProductId().equals(key))
            {
                shoppingCartList.remove(i);
            }
        }

        for (int i = 0; i < listsProducts.size();i++)
        {
            if(listsProducts.get(i).getProductId().equals(key))
            {
                listsProducts.remove(i);
            }
        }
    }

}