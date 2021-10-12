package com.example.libery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.libery.databinding.ActivityCategoryBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CategoryActivity extends AppCompatActivity {
    private ActivityCategoryBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

        Dialog = new ProgressDialog(this);
        Dialog.setTitle("Por favor espere");
        Dialog.setCanceledOnTouchOutside(false);

        binding.backbtnCate.setOnClickListener(new View.OnClickListener() {//Regresar
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.BtnEnvioCategoria.setOnClickListener(new View.OnClickListener() {//Agregar Categoria
            @Override
            public void onClick(View v) {
                ValidarDatoCategoria();
            }
        });
    }

    private String categoria = "";

    private void ValidarDatoCategoria() {//Funcion validacion categoria
        categoria = binding.EdCategory.getText().toString().trim();

        if (TextUtils.isEmpty(categoria)) {
            Toast.makeText(this, "Ingrese una categoria", Toast.LENGTH_SHORT).show();
        } else {
            AddCategoriaFire();
        }

    }

    private void AddCategoriaFire() {//Agregar categoria
        Dialog.setMessage("Agregando Categoria");
        Dialog.show();

        long timestap = System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>(); //Configuracion de datos en RealTime Categoria
        hashMap.put("id", "" + timestap);
        hashMap.put("categoria", "" + categoria);
        hashMap.put("timestap", "" + timestap);
        hashMap.put("uid", "" + firebaseAuth.getUid());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categorias Libros");
        ref.child("" + timestap)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {//categoria agregada
                        Dialog.dismiss();
                        Toast.makeText(CategoryActivity.this, "Categoria agregada satisfactoriamente", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {//fallo de categoria
            @Override
            public void onFailure(@NonNull Exception e) {
                Dialog.dismiss();
                Toast.makeText(CategoryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}