package com.example.bookstore.Fragments.ProfileFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.Classes.User;
import com.example.bookstore.MainActivity;
import com.example.bookstore.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;


public class UserFragment extends Fragment {

    private View rootView;
    private ImageView ivBack;
    private Button btnUpdate;

    private String tableKey;

    private FirebaseUser mUser;
    private DatabaseReference userTable;

    TextInputEditText etName,etSurname,etEmail,etAddress,etCity;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userTable = FirebaseDatabase.getInstance().getReference("users");



        etName = rootView.findViewById(R.id.etName);
        etSurname = rootView.findViewById(R.id.etSurname);
        etEmail = rootView.findViewById(R.id.etEmail);
        etAddress = rootView.findViewById(R.id.etAddress);
        etCity = rootView.findViewById(R.id.etCity);
        btnUpdate = rootView.findViewById(R.id.btnUpdate);

         ivBack = rootView.findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).PopBackStack();
            }
        });

        String userKey = mUser.getUid();
        ReadUserFromDatabase(userKey);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserInfo();
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
                    etName.setText(user.getName());
                    etSurname.setText(user.getSurname());
                    etEmail.setText(mUser.getEmail());
                    etAddress.setText(user.getAddress());
                    etCity.setText(user.getCity());

                    tableKey = data.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateUserInfo()
    {
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String address = etAddress.getText().toString();
        String city = etCity.getText().toString();

        User user = new User(mUser.getUid(),name,surname,address,city);
        Map<String, Object> userValues = user.toMap();
        userTable.child(tableKey).updateChildren(userValues);
    }

}