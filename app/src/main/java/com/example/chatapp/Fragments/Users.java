package com.example.chatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatapp.Adapters.UserAdapter;
import com.example.chatapp.R;
import com.example.chatapp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Users extends Fragment {
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    ArrayList<User> userList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.rv_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();

        displayList();
        return view;
    }

    private void displayList() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    User user = dataSnapshot1.getValue(User.class);
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    if(user!=null && firebaseUser!=null) {
                        if (!user.getUserId().equals(firebaseUser.getUid())) {
                            userList.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), userList);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error Occurred...", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
