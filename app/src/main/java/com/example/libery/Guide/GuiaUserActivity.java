package com.example.libery.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.libery.Adapters.AdapterSem;
import com.example.libery.Adapters.AdapterSemUser;
import com.example.libery.databinding.ActivityGuiaUserBinding;
import com.example.libery.models.ModeloGuia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GuiaUserActivity extends AppCompatActivity {
    private ActivityGuiaUserBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModeloGuia> semUserArray;
    private AdapterSemUser adapterSemUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityGuiaUserBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        cargarCategorias();
        
        binding.backbtnGuia.setOnClickListener(new View.OnClickListener() {//Regresar
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void cargarCategorias() {//Cargamos todos los semestres
        semUserArray = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Semestre Guia");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                semUserArray.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModeloGuia model = ds.getValue(ModeloGuia.class);

                    semUserArray.add(model);
                }

                adapterSemUser = new AdapterSemUser(GuiaUserActivity.this, semUserArray);
                binding.RVcategorias.setAdapter(adapterSemUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}