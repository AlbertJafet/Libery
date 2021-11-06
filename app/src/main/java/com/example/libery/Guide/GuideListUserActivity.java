package com.example.libery.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.libery.Adapters.AdapterGuiaUser;
import com.example.libery.Adapters.AdapterGuias;
import com.example.libery.databinding.ActivityGuideListUserBinding;
import com.example.libery.models.ModeloGuidaDidactica;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GuideListUserActivity extends AppCompatActivity {
    private ActivityGuideListUserBinding binding;
    private ArrayList<ModeloGuidaDidactica> arrayListGuia;
    private AdapterGuiaUser adapterGuiasUser;
    private String semestreUserId, semestreUserTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideListUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();//de AdapterSem
        semestreUserId = intent.getStringExtra("semestreUserId");
        semestreUserTitulo = intent.getStringExtra("semestreUserTitulo");

        binding.TvSubti.setText(semestreUserTitulo);//txtSubtitulo  Cat

        cargarPdfList();

        binding.backbtnAdminList.setOnClickListener(new View.OnClickListener() {//Regresar
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void cargarPdfList() {
        arrayListGuia = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Guia didactica");
        ref.orderByChild("GuiaId").equalTo(semestreUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayListGuia.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            ModeloGuidaDidactica modelo = ds.getValue(ModeloGuidaDidactica.class);

                            arrayListGuia.add(modelo);

                            adapterGuiasUser = new AdapterGuiaUser(GuideListUserActivity.this, arrayListGuia);
                            binding.ViewLibro.setAdapter(adapterGuiasUser);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}