package com.example.bookstore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bookstore.Classes.Order;
import com.example.bookstore.Classes.Product;
import com.example.bookstore.Classes.ProductOrderInfo;
import com.example.bookstore.Classes.Products;
import com.example.bookstore.Classes.ShoppingCart;
import com.example.bookstore.Classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentTypeFragment extends Fragment {

    View rootView;

    private Button btnPlaceOrder;
    private RadioGroup radio_group;
    private RadioButton radio_paymentType;
    private List<Products> productsList;
    private List<ShoppingCart> shoppingCartList;
    private List<Product> productList;

    private DatabaseReference orderTable;
    private DatabaseReference userTable;
    private DatabaseReference shoppingCartTable;
    private final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private User user;

    public PaymentTypeFragment(List<Products> productsList,List<ShoppingCart> shoppingCartList,List<Product> productList) {
        this.productsList = productsList;
        this.shoppingCartList = shoppingCartList;
        this.productList = productList;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderTable = FirebaseDatabase.getInstance().getReference("orders");
        shoppingCartTable = FirebaseDatabase.getInstance().getReference("shoppingCart");
        userTable = FirebaseDatabase.getInstance().getReference("users");
        ReadUserFromDatabase(userUID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_payment_type, container, false);

        btnPlaceOrder = rootView.findViewById(R.id.btnPlaceOrder);
        radio_group = rootView.findViewById(R.id.radio_group);
        //radio_onDelivery = rootView.findViewById(R.id.radio_onDelivery);



        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = radio_group.getCheckedRadioButtonId();
                radio_paymentType = rootView.findViewById(radioId);
            }
        });


        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radio_paymentType != null)
                {
                    if(radio_paymentType.isChecked())
                    {
                        FinishOrder();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Select payment method!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return rootView;
    }

    private void ReadUserFromDatabase(String key) {
        Query query = userTable.orderByChild("userUID").equalTo(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                     user =data.getValue(User.class);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void FinishOrder()
    {
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
        String date = fmt.format(new Date());

        List<ProductOrderInfo> orderInfoList = new ArrayList<>();

        for (Product product: productList)
        {
        ProductOrderInfo productOrderInfo = new ProductOrderInfo();
        productOrderInfo.setArticleKey(product.getKey());
        productOrderInfo.setArticleName(product.getName());

            for (ShoppingCart cart:shoppingCartList)
            {
                if(cart.getProductId().equals(product.getKey()))
                {
                    productOrderInfo.setArticleQuantity(cart.getQuantityToBuy());
                }
            }
            orderInfoList.add(productOrderInfo);
        }

        Order order = new Order();
        order.setUserUID(userUID);
        order.setStatus("Submitted");
        order.setOrderDate(date);
        order.setArticle(orderInfoList);
        order.setTotalPrice(GetPrice());
        order.setCurrency("EUR");
        order.setAddress(user.getAddress());
        order.setCity(user.getCity());

       // Toast.makeText(getActivity(), date, Toast.LENGTH_SHORT).show();

        orderTable.push().setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(),"Success!",Toast.LENGTH_SHORT).show();
                RemoveFromCart();
                ((MainActivity)getActivity()).DestroyPaymentFragmentInfo();
                ((MainActivity) getActivity()).binding.NavigationBar.setSelectedItemId(R.id.home);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Failed!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private float GetPrice()
    {
        float price = 0;

        for (ShoppingCart item : shoppingCartList)
        {
            for (Products product : productsList)
            {
                if(product.getProductId().equals(item.getProductId()))
                {
                    price+= product.getPrice()*item.getQuantityToBuy();
                }

            }

        }
        return price;
    }


    private void RemoveFromCart()
    {
        Query query = shoppingCartTable.orderByChild("userUID").equalTo(userUID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {

                    ShoppingCart cart = new ShoppingCart();
                    cart = data.getValue(ShoppingCart.class);
                    if(cart.getUserUID().equals(userUID))
                    {
                        shoppingCartTable.child(data.getKey()).removeValue();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}