package com.example.kevin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kevin.adapters.ChatListAdapter;
import com.example.kevin.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatsListActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ChatListAdapter adapter;
    private final List<User> users = new ArrayList<>();
    private RecyclerView rvChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rvChats = findViewById(R.id.rvChats);
        adapter = new ChatListAdapter(users, this::openChat);
        rvChats.setLayoutManager(new LinearLayoutManager(this));
        rvChats.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        String myUid = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        db.collection("users").get()
                .addOnSuccessListener(snaps -> {
                    users.clear();
                    for (DocumentSnapshot doc : snaps) {
                        User user = doc.toObject(User.class);
                        if (user == null || user.getUid().equals(myUid)) continue;
                        users.add(user);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error cargando usuarios", Toast.LENGTH_SHORT).show());
    }

    private void openChat(User user) {
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("contactUid", user.getUid());
        i.putExtra("contactName", user.getName());
        i.putExtra("contactPhotoUrl", user.getPhotoUrl());
        startActivity(i);
    }
}
