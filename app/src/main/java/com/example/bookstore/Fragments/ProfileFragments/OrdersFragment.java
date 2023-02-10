package com.example.bookstore.Fragments.ProfileFragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.Adapters.OrdersAdapter;
import com.example.bookstore.Adapters.ProductAdapter;
import com.example.bookstore.Classes.Order;
import com.example.bookstore.Classes.ProductOrderInfo;
import com.example.bookstore.Fragments.ShoppingCartFragment;
import com.example.bookstore.Interfaces.SelectOrderListener;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class OrdersFragment extends Fragment implements SelectOrderListener {

    private View rootView;

    private final String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private RecyclerView ordersRecyclerView;
    private LinearLayoutManager layoutManager;
    private OrdersAdapter ordersAdapter;
    private ImageView ivBack;
    private LinearLayout llOrdersEmpty;


    private List<Order> orderList = new ArrayList<>();

    private DatabaseReference ordersTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersTable = FirebaseDatabase.getInstance().getReference("orders");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_orders, container, false);

        ordersRecyclerView = rootView.findViewById(R.id.rvOrdersInfo);
        ivBack = rootView.findViewById(R.id.ivBack);
        llOrdersEmpty = rootView.findViewById(R.id.llOrdersEmpty);


        GetUserOrders();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).PopBackStack();
            }
        });

        return rootView;
    }

    private void GetUserOrders()
    {
        Query query = ordersTable.orderByChild("userUID").equalTo(userUID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren())
                {
                    Order order = new Order();
                    order = data.getValue(Order.class);
                    order.setOrderKey(data.getKey());
                    orderList.add(order);
                }
                if(orderList.size()>0)
                {
                    llOrdersEmpty.setVisibility(View.INVISIBLE);
                    SetUpRecyclerView();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void SetUpRecyclerView()
    {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        ordersRecyclerView.setLayoutManager(layoutManager);
        ordersAdapter = new OrdersAdapter(orderList,OrdersFragment.this);
        ordersRecyclerView.setAdapter(ordersAdapter);
    }

    @Override
    public void onArticleClicked(Order order) {
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_Dialog);
        dialog.setTitle("Order info");
        dialog.setContentView(R.layout.dialog_order);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        String articles = "";
        int size = order.getArticle().size();
        int count = 1;
        for (ProductOrderInfo product: order.getArticle())
        {
            if(count == size)
            {
                articles += product.getArticleName();
            }
            else
            {
                articles += product.getArticleName()+", ";
            }
            count++;
        }

        ((TextView)dialog.findViewById(R.id.tvProductNames)).setText(articles);
        ((TextView)dialog.findViewById(R.id.tvOrderKey)).setText(order.getOrderKey());
        ((TextView)dialog.findViewById(R.id.tvDate)).setText(order.getOrderDate());
        ((TextView)dialog.findViewById(R.id.tvTotalPrice)).setText(String.valueOf(order.getTotalPrice())+" " + order.getCurrency());
        ((TextView)dialog.findViewById(R.id.tvStatus)).setText(order.getStatus());
        ((TextView)dialog.findViewById(R.id.tvAddress)).setText(order.getAddress());
        ((TextView)dialog.findViewById(R.id.tvCity)).setText(order.getCity());
        ((TextView)dialog.findViewById(R.id.tvPaymentType)).setText(order.getOrderPaymentType());

        dialog.show();

    }
}