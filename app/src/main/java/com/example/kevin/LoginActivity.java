package com.example.kevin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kevin.databinding.ActivityLoginBinding;
import com.example.kevin.models.User;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private final ActivityResultLauncher<IntentSenderRequest> oneTapLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                try {
                    SignInCredential credential = Identity.getSignInClient(this).getSignInCredentialFromIntent(result.getData());
                    String idToken = credential.getGoogleIdToken();
                    if (idToken != null) {
                        firebaseAuthWithGoogle(idToken);
                    } else {
                        Toast.makeText(this, getString(R.string.login_error_token), Toast.LENGTH_SHORT).show();
                    }
                } catch (ApiException e) {
                    Log.e("Login", "Error Google Sign-In: " + e.getMessage());
                    Toast.makeText(this, getString(R.string.login_error_signin), Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() != null) {
            goToMain();
            return;
        }

        String webClientId = getString(R.string.default_web_client_id); // definido en strings.xml

        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(webClientId)
                                .setFilterByAuthorizedAccounts(false)
                                .build()
                )
                .setAutoSelectEnabled(false)
                .build();

        binding.btnGoogleSignIn.setOnClickListener(v -> {
            oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(result -> {
                        IntentSenderRequest intentSenderRequest =
                                new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                        oneTapLauncher.launch(intentSenderRequest);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "No se pudo iniciar One Tap: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(res -> {
                    com.google.firebase.auth.FirebaseUser fUser = auth.getCurrentUser();
                    if (fUser != null) {
                        User u = new User(
                                fUser.getUid(),
                                fUser.getDisplayName(),
                                fUser.getEmail(),
                                (fUser.getPhotoUrl() != null) ? fUser.getPhotoUrl().toString() : ""
                        );

                        db.collection("users").document(u.getUid())
                                .set(u)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firestore", "Usuario guardado correctamente: " + u.getEmail());
                                    goToMain();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirestoreError", "Error al guardar usuario", e);
                                    Toast.makeText(this, "Error al guardar usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Usuario nulo después de autenticación", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LoginError", "signInWithCredential failed", e);
                    Toast.makeText(this, getString(R.string.login_error_auth) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
