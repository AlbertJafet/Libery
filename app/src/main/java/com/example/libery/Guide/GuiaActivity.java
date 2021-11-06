package com.example.libery.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.libery.Adapters.AdapterSem;
import com.example.libery.databinding.ActivityGuiaBinding;
import com.example.libery.models.ModeloGuia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GuiaActivity extends AppCompatActivity {
    private ActivityGuiaBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModeloGuia> semestreArrayList;
    private AdapterSem adapterSemestre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuiaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cargarCategorias();

        binding.backbtnGuia.setOnClickListener(new View.OnClickListener() {//Regresar
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.AddGuiaSmestrBTN.setOnClickListener(new View.OnClickListener() {//Agregar nueva guia
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuiaActivity.this, AddSemestrGuideActivity.class));
            }
        });
        binding.AddSmtrFFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuiaActivity.this, GuiaAddActivit.class));//Subir pdf guia
            }
        });
    }

    private void cargarCategorias() {//Cargamos todos los semestres
        semestreArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Semestre Guia");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                semestreArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModeloGuia model = ds.getValue(ModeloGuia.class);

                    semestreArrayList.add(model);
                }

                adapterSemestre = new AdapterSem(GuiaActivity.this, semestreArrayList);
                binding.RVcategorias.setAdapter(adapterSemestre);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}