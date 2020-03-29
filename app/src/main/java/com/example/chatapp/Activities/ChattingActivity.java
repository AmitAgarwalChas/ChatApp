package com.example.chatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Adapters.ChattingAdapter;
import com.example.chatapp.Chat;
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

import java.util.ArrayList;
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
    RecyclerView prevMessages;
    ChattingAdapter chattingAdapter;
    ArrayList<Chat> chats;

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
        assert fuser != null;
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
                displayMessages(fuser.getUid(), userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void displayMessages(final String myId, final String userId) {

        prevMessages.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chats = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReciever().equals(myId) && chat.getSender().equals(userId) ||
                            chat.getReciever().equals(userId) && chat.getSender().equals(myId)){
                        chats.add(chat);
                    }

                    chattingAdapter = new ChattingAdapter(getApplicationContext(), chats);
                    prevMessages.setAdapter(chattingAdapter);
                }
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
        prevMessages = findViewById(R.id.display_messages);
    }
}
