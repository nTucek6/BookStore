package com.example.bookstore.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.Adapters.BookAdapter;
import com.example.bookstore.Classes.Book;
import com.example.bookstore.Classes.User;
import com.example.bookstore.LoginActivity;
import com.example.bookstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private DatabaseReference userTable;

    private TextView tvName,tvSurname;
    private Button logoutBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvName);
        tvSurname = view.findViewById(R.id.tvSurname);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userTable = FirebaseDatabase.getInstance().getReference("users");
        String key = mUser.getUid();
        ReadUserFromDatabase(key);

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