package com.example.libery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.libery.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        dialogo = new ProgressDialog(this);//ProgressDialog
        dialogo.setTitle("Por favor espere");
        dialogo.setCanceledOnTouchOutside(false);

        binding.LoginButton.setOnClickListener(new View.OnClickListener() {//BOTON LOGIN
            @Override
            public void onClick(View v) {
                valideData();
            }
        });

        binding.backbtnLogin.setOnClickListener(new View.OnClickListener() {//Regresar
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private String email = "", password = "";//FUNCION DE VALIDACION

    private void valideData() {
        email = binding.EdEmailLogin.getText().toString().trim();
        password = binding.EdPasswordLogin.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {     //VALIDACION DE DATOS IF
            Toast.makeText(this, "Correo Invalido", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
        } else {
            LoginUser();
        }
    }

    private void LoginUser() {//LOGIN CON FIREBASE AUTH
        dialogo.setMessage("Usuario");
        dialogo.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        ValidacionUsuario();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialogo.dismiss();
                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ValidacionUsuario() {//FUNCION ADMIN O USUARIO COMUN
        dialogo.setMessage("Validando Usuario");
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios");
        ref.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dialogo.dismiss();

                        String TipodeUsuario = "" + snapshot.child("TipodeUsuario").getValue();
                        //checar usuario
                        if (TipodeUsuario.equals("user")) {//Usuario
                            startActivity(new Intent(LoginActivity.this, InicioUserActivity.class));
                            finish();

                        } else if (TipodeUsuario.equals("admin")) {//Administrador
                            startActivity(new Intent(LoginActivity.this, InicioAdminActivity.class));
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}