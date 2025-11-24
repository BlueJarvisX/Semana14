package com.example.kevin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kevin.adapters.MessageAdapter;
import com.example.kevin.models.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    EditText editMessage;
    ImageButton btnSend, btnSendImage;
    RecyclerView recycler;
    MessageAdapter adapter;

    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseStorage storage;

    String myUid;

    private static final int IMAGE_PICK = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);
        btnSendImage = findViewById(R.id.btnSendImage);
        recycler = findViewById(R.id.recyclerMessages);

        auth = FirebaseAuth.getInstance();
        myUid = auth.getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        adapter = new MessageAdapter(this, myUid);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        listenMessages();

        btnSend.setOnClickListener(v -> sendText());
        btnSendImage.setOnClickListener(v -> pickImage());
    }

    private void sendText() {
        String text = editMessage.getText().toString().trim();
        if (text.isEmpty()) return;

        Map<String, Object> msg = new HashMap<>();
        msg.put("senderUid", myUid);
        msg.put("senderName", "Yo");
        msg.put("text", text);
        msg.put("imageUrl", "");
        msg.put("timestamp", Timestamp.now().toDate().getTime());

        db.collection("messages").add(msg);
        editMessage.setText("");
    }

    private void pickImage() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            uploadImage(uri);
        }
    }

    private void uploadImage(Uri uri) {
        StorageReference ref = storage.getReference().child("chat_images/" + System.currentTimeMillis() + ".jpg");

        ref.putFile(uri)
                .addOnSuccessListener(task -> ref.getDownloadUrl().addOnSuccessListener(url -> sendImage(url.toString())));
    }

    private void sendImage(String url) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("senderUid", myUid);
        msg.put("senderName", "Yo");
        msg.put("text", "");
        msg.put("imageUrl", url);
        msg.put("timestamp", Timestamp.now().toDate().getTime());

        db.collection("messages").add(msg);
    }

    private void listenMessages() {
        db.collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snap, e) -> {
                    if (snap == null) return;

                    adapter.messageList.clear();

                    for (DocumentSnapshot doc : snap.getDocuments()) {
                        Message m = doc.toObject(Message.class);
                        adapter.messageList.add(m);
                    }

                    adapter.notifyDataSetChanged();
                    recycler.scrollToPosition(adapter.getItemCount() - 1);
                });
    }
}
