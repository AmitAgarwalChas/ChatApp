package com.example.chatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.R;
import com.example.chatapp.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChattingActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextInputEditText message;
    ImageView sendButton;
    TextView userName;
    Intent intent;
    FirebaseAuth auth;
    FirebaseUser fuser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        fuser = auth.getCurrentUser();
        intent = getIntent();
        final String userId = intent.getStringExtra("userId");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = message.getText().toString().trim();
                if(!msg.equals("")){
                    sendMessage(fuser.getUid(), userId, msg);
                }
                else{
                    Toast.makeText(getApplicationContext(), "You can't send empty messages",Toast.LENGTH_SHORT)
                            .show();
                }
                message.setText("");
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String senderId, String recieverId, String msg) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", senderId);
        hashMap.put("reciever", recieverId);
        hashMap.put("message", msg);

        dbReference.child("Chats").push().setValue(hashMap);
    }


    private void initViews() {
        toolbar = findViewById(R.id.toolbar_chatting);
        message = findViewById(R.id.message_box);
        sendButton = findViewById(R.id.btn_send);
        userName = findViewById(R.id.display_name);
    }
}
