package com.example.kevin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kevin.databinding.ActivityProfileBinding;
import com.example.kevin.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        loadProfile();

        binding.btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void loadProfile() {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    User user = doc.toObject(User.class);
                    if (user != null) {
                        binding.edtName.setText(user.getName());
                        binding.txtEmail.setText(user.getEmail());
                        binding.imgProfile.setImageURI(null); // opcional: usar Glide/Picasso si tienes photoUrl
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar perfil", Toast.LENGTH_SHORT).show());
    }

    private void saveProfile() {
        String name = binding.edtName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Ingresa un nombre válido", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(uid).update("name", name)
                .addOnSuccessListener(a -> Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show());
    }
}
