package com.example.bookstore.Fragments.ProfileFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.Classes.User;
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


public class UserFragment extends Fragment {

    private View rootView;
    private TextView tvName,tvSurname;
    private ImageView ivBack;

    private FirebaseUser mUser;
    private DatabaseReference userTable;

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

         tvName = rootView.findViewById(R.id.tvName);
         tvSurname = rootView.findViewById(R.id.tvSurname);
         ivBack = rootView.findViewById(R.id.ivBack);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).PopBackStack();
            }
        });

        String key = mUser.getUid();
        ReadUserFromDatabase(key);




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