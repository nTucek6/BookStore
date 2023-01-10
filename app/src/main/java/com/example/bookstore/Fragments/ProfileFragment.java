package com.example.bookstore.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.Classes.User;
import com.example.bookstore.LoginActivity;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private View rootView;

    private DatabaseReference userTable;

    private TextView tvName,tvSurname;
    private Button logoutBtn,btnOrders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = rootView.findViewById(R.id.tvName);
        tvSurname = rootView.findViewById(R.id.tvSurname);
        logoutBtn = rootView.findViewById(R.id.logoutBtn);
        btnOrders = rootView.findViewById(R.id.btnOrders);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userTable = FirebaseDatabase.getInstance().getReference("users");
        String key = mUser.getUid();
        ReadUserFromDatabase(key);

        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).OrdersFragment();
            }
        });



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.LogoutDialog))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mAuth.signOut();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .show();
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
                    User user =data.getValue(User.class);
                    tvName.setText(user.getName());
                    tvSurname.setText(user.getSurname());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
            }
        });
    }
}