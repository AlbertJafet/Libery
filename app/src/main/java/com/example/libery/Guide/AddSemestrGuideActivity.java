package com.example.libery.Guide;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libery.databinding.ActivitySemestrNewGuideBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddSemestrGuideActivity extends AppCompatActivity {
    private ActivitySemestrNewGuideBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySemestrNewGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth = FirebaseAuth.getInstance();

        Dialog = new ProgressDialog(this);
        Dialog.setTitle("Por favor espere");
        Dialog.setCanceledOnTouchOutside(false);

        binding.backbtnSemes.setOnClickListener(new View.OnClickListener() {//Regresar
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.BtnEnvioSemestre.setOnClickListener(new View.OnClickListener() {//Agregar Categoria
            @Override
            public void onClick(View v) {
                ValidarDatoSmestre();
            }
        });

    }

    private String semestre = "";

    private void ValidarDatoSmestre() {//Funcion validacion semestre
        semestre = binding.EdSmestr.getText().toString().trim();

        if (TextUtils.isEmpty(semestre)) {
            Toast.makeText(this, "Ingrese un semestre", Toast.LENGTH_SHORT).show();
        } else {
            AddSemestreFire();
        }

    }

    private void AddSemestreFire() {//Agregar categoria
        Dialog.setMessage("Agregando Semestre");
        Dialog.show();

        long timestap = System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>(); //Configuracion de datos en RealTime Semestre
        hashMap.put("id", "" + timestap);
        hashMap.put("semestre", "" + semestre);
        hashMap.put("timestap", "" + timestap);
        hashMap.put("uid", "" + firebaseAuth.getUid());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Semestre Guia");
        ref.child("" + timestap)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {//categoria agregada
                        Dialog.dismiss();
                        Toast.makeText(AddSemestrGuideActivity.this, "Semestre agregada satisfactoriamente", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {//fallo de categoria
            @Override
            public void onFailure(@NonNull Exception e) {
                Dialog.dismiss();
                Toast.makeText(AddSemestrGuideActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}