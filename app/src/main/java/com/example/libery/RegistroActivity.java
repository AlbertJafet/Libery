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

import com.example.libery.databinding.ActivityRegistroBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        dialogo = new ProgressDialog(this);//ProgressDialog
        dialogo.setTitle("Por favor espere");
        dialogo.setCanceledOnTouchOutside(false);

        binding.backbtn.setOnClickListener(new View.OnClickListener() {//Regresar
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.RegistrarButton.setOnClickListener(new View.OnClickListener() {//Registro
            @Override
            public void onClick(View v) {
                valideData();
            }
        });
    }

    private String name = "", email = "", password = "";//FUNCION DE VALIDACION

    private void valideData() {
        name = binding.EdName.getText().toString().trim();
        email = binding.EdEmail.getText().toString().trim();
        password = binding.EdPassword.getText().toString().trim();
        String ConfirmaPassword = binding.EdPasswordConfirma.getText().toString().trim();

        //Validacion de datos IF
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Ingrese su nombre", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Correo Invalido", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingrese su contraseña", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ConfirmaPassword)) {
            Toast.makeText(this, "Confirmar contraseña", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(ConfirmaPassword)) {
            Toast.makeText(this, "Verifique la contraseña", Toast.LENGTH_SHORT).show();
        } else {
            crearNuevoUsuario();
        }
    }

    private void crearNuevoUsuario() { //NUEVO USUARIO
        dialogo.setMessage("Creando Usuario");
        dialogo.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {//Nuevo Usuario
                UpdateUsuario();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialogo.dismiss();
                Toast.makeText(RegistroActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdateUsuario() {//ACTUALIZACION DE DATOS DB
        dialogo.setMessage("Actualizando informacion del usuario");

        long timestap = System.currentTimeMillis();

        String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>(); //Configuracion de daros en RealTime
        hashMap.put("uid", uid);
        hashMap.put("correo", email);
        hashMap.put("nombre", name);
        hashMap.put("ImagenPerfil", "");
        hashMap.put("TipodeUsuario", "user");
        hashMap.put("timestap", timestap);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios");//REALTIME DB
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) { //Datos agregado en db
                        dialogo.dismiss();
                        Toast.makeText(RegistroActivity.this, "Usuario Creado", Toast.LENGTH_SHORT).show();
                        //InicioUsuarioMenu
                        startActivity(new Intent(RegistroActivity.this, InicioUserActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {//Falla en los datos db
            @Override
            public void onFailure(Exception e) {
                dialogo.dismiss();
                Toast.makeText(RegistroActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}