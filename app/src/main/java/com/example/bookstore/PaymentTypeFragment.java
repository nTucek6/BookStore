package com.example.bookstore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bookstore.Classes.CardData;
import com.example.bookstore.Classes.Order;
import com.example.bookstore.Classes.Product;
import com.example.bookstore.Classes.ProductOrderInfo;
import com.example.bookstore.Classes.Products;
import com.example.bookstore.Classes.ShoppingCart;
import com.example.bookstore.Classes.User;
import com.example.bookstore.Fragments.PaymentType.CardFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PaymentTypeFragment extends Fragment {

    View rootView;

    private Button btnPlaceOrder;
    private RadioGroup radio_group;
    private RadioButton radio_paymentType;
    private ImageView ivBack;
    private List<Products> productsList;
    private List<ShoppingCart> shoppingCartList;
    private List<Product> productList;

    private DatabaseReference orderTable;
    private DatabaseReference userTable;
    private DatabaseReference shoppingCartTable;
    private DatabaseReference cardTable;
    private final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private User user;

    public PaymentTypeFragment(List<Products> productsList, List<ShoppingCart> shoppingCartList, List<Product> productList) {
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
        cardTable = FirebaseDatabase.getInstance().getReference("card");
        ReadUserFromDatabase(userUID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_payment_type, container, false);

        btnPlaceOrder = rootView.findViewById(R.id.btnPlaceOrder);
        radio_group = rootView.findViewById(R.id.radio_group);
        ivBack = rootView.findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).PopBackStack();
            }
        });


        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = radio_group.getCheckedRadioButtonId();
                radio_paymentType = rootView.findViewById(radioId);

                if (getChildFragmentManager().findFragmentById(R.id.paymentFrame) != null) {
                    getChildFragmentManager().beginTransaction().remove(getChildFragmentManager().findFragmentById(R.id.paymentFrame)).commit();
                }

                if (radioId == R.id.radio_Card) {
                    getChildFragmentManager().beginTransaction().replace(R.id.paymentFrame, new CardFragment()).commit();
                }

            }
        });

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radio_paymentType != null) {
                    if (radio_paymentType.isChecked()) {
                        if (radio_paymentType.equals(rootView.findViewById(R.id.radio_onDelivery))) {
                            FinishOrder("On delivery");
                        } else if (radio_paymentType.equals(rootView.findViewById(R.id.radio_Card))) {
                            CardFragment cardFragment = (CardFragment) getChildFragmentManager().findFragmentById(R.id.paymentFrame);

                            if (cardFragment.CheckInput()) {
                                CardData cardData = cardFragment.GetData();
                                GetCard(cardData);
                            }
                        }
                    }
                } else {
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
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    user = data.getValue(User.class);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GetCard(CardData cardData) {
        Query query = cardTable.orderByChild("cardNumber").equalTo(cardData.getCardNumber());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CardData card = data.getValue(CardData.class);

                        if (!card.getExpireDate().equals(cardData.getExpireDate()) || !card.getCvv().equals(cardData.getCvv()) || !card.getCardHolderName().equals(cardData.getCardHolderName())) {
                            Toast.makeText(getActivity(), "Pogrešni podatci kartice!", Toast.LENGTH_SHORT).show();
                        } else {
                            float price = GetPrice();

                            if (card.getBalance() > price) {
                                card.setBalance((card.getBalance() - price));

                                Map<String, Object> cardValues = card.toMap();
                                cardTable.child(data.getKey()).updateChildren(cardValues);
                                FinishOrder("Card");
                            } else {
                                Toast.makeText(getActivity(), "Transaction declined!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Card does not exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void FinishOrder(String orderPaymentType) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = fmt.format(new Date());

        List<ProductOrderInfo> orderInfoList = new ArrayList<>();

        for (Product product : productList) {
            ProductOrderInfo productOrderInfo = new ProductOrderInfo();
            productOrderInfo.setArticleKey(product.getKey());
            productOrderInfo.setArticleName(product.getName());

            for (ShoppingCart cart : shoppingCartList) {
                if (cart.getProductId().equals(product.getKey())) {
                    productOrderInfo.setArticleQuantity(cart.getQuantityToBuy());
                }
            }
            orderInfoList.add(productOrderInfo);
        }

        float price = Float.parseFloat(String.format("%.2f", GetPrice()).replace(",", "."));

        Order order = new Order();
        order.setUserUID(userUID);
        order.setStatus("Submitted");
        order.setOrderDate(date);
        order.setArticle(orderInfoList);
        order.setTotalPrice(price);
        order.setCurrency("EUR");
        order.setAddress(user.getAddress());
        order.setCity(user.getCity());
        order.setOrderPaymentType(orderPaymentType);

        orderTable.push().setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                RemoveFromCart();
                ((MainActivity) getActivity()).DestroyPaymentFragmentInfo();
                ((MainActivity) getActivity()).binding.NavigationBar.setSelectedItemId(R.id.home);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private float GetPrice() {
        float price = 0;

        for (ShoppingCart item : shoppingCartList) {
            for (Products product : productsList) {
                if (product.getProductId().equals(item.getProductId())) {
                    price += product.getPrice() * item.getQuantityToBuy();
                }
            }
        }
        return price;
    }

    private void RemoveFromCart() {
        Query query = shoppingCartTable.orderByChild("userUID").equalTo(userUID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {

                    ShoppingCart cart = new ShoppingCart();
                    cart = data.getValue(ShoppingCart.class);
                    if (cart.getUserUID().equals(userUID)) {
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