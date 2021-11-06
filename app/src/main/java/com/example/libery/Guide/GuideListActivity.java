package com.example.libery.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.libery.Adapters.AdapterGuias;
import com.example.libery.Adapters.AdapterPdfAdmin;
import com.example.libery.PdfListAdminActivity;
import com.example.libery.databinding.ActivityGuideListBinding;
import com.example.libery.models.ModeloGuia;
import com.example.libery.models.ModeloGuidaDidactica;
import com.example.libery.models.ModeloPDF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GuideListActivity extends AppCompatActivity {
    private ActivityGuideListBinding binding;
    private ArrayList<ModeloGuidaDidactica> arrayListGuia;
    private AdapterGuias adapterGuias;
    private String semestreId, semestreTitulo;
    private final static String TAG = "PDF_LIST_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();//de AdapterSem
        semestreId = intent.getStringExtra("semestreId");
        semestreTitulo = intent.getStringExtra("semestreTitulo");

        binding.TvSubti.setText(semestreTitulo);//txtSubtitulo  Cat

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
        ref.orderByChild("GuiaId").equalTo(semestreId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       arrayListGuia.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            ModeloGuidaDidactica modelo = ds.getValue(ModeloGuidaDidactica.class);

                            arrayListGuia.add(modelo);

                            adapterGuias = new AdapterGuias(GuideListActivity.this, arrayListGuia);
                            binding.ViewLibro.setAdapter(adapterGuias);

                            Log.d(TAG, "onDataChange: " + modelo.getId() + "" + modelo.getTitulo());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}