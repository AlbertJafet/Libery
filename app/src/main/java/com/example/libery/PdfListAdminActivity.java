package com.example.libery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.libery.Adapters.AdapterPdfAdmin;
import com.example.libery.databinding.ActivityPdfListAdminBinding;
import com.example.libery.models.ModeloPDF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.file.WatchEvent;
import java.util.ArrayList;

public class PdfListAdminActivity extends AppCompatActivity {
    private ActivityPdfListAdminBinding binding;
    private ArrayList<ModeloPDF> pdfArrayList;
    private AdapterPdfAdmin adapterPdfAdmin;
    private String categoriaId, categoriaTitulo;
    private final static String TAG = "PDF_LIST_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfListAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();//de AdapterCat
        categoriaId = intent.getStringExtra("categoriaId");
        categoriaTitulo = intent.getStringExtra("categoriaTitulo");

        binding.TvSubti.setText(categoriaTitulo);//txtSubtitulo  Cat

        cargarPdfList();

        binding.EdBuscarLibro.addTextChangedListener(new TextWatcher() {//Buscar Libro
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterPdfAdmin.getFilter().filter(s);
                } catch (Exception e) {
                    Log.d(TAG, "onTextChanged: " + e.getMessage());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.backbtnAdminList.setOnClickListener(new View.OnClickListener() {//Regresar
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void cargarPdfList() {
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Libros");
        ref.orderByChild("categoriaId").equalTo(categoriaId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pdfArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            ModeloPDF modelo = ds.getValue(ModeloPDF.class);

                            pdfArrayList.add(modelo);

                            adapterPdfAdmin = new AdapterPdfAdmin(PdfListAdminActivity.this, pdfArrayList);
                            binding.ViewLibro.setAdapter(adapterPdfAdmin);

                            Log.d(TAG, "onDataChange: " + modelo.getId() + "" + modelo.getTitulo());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}