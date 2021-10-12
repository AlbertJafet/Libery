package com.example.libery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.libery.Adapters.AdapterCat;
import com.example.libery.databinding.ActivityInicioAdminBinding;
import com.example.libery.models.ModeloCat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class InicioAdminActivity extends AppCompatActivity {
    private ActivityInicioAdminBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModeloCat> categoriaArrayList;
    private AdapterCat adapterCatego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInicioAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();//Funciones
        ValidacionUsuario();
        CargarCategorias();

        //Buscador con ChangedListen
        binding.EdBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterCatego.getFilter().filter(s);

                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.OffLineBtn.setOnClickListener(new View.OnClickListener() {//Logout
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                ValidacionUsuario();
            }
        });
        binding.AddCategoryBTN.setOnClickListener(new View.OnClickListener() {//Agregar Cat
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicioAdminActivity.this, CategoryActivity.class));
            }
        });

        binding.AddPDFFl.setOnClickListener(new View.OnClickListener() {//Agregar PDF
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicioAdminActivity.this, PdfAddActivity.class));
            }
        });
    }

    private void CargarCategorias() {//Cargamos todas las categorias
        categoriaArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categorias Libros");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoriaArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModeloCat model = ds.getValue(ModeloCat.class);

                    categoriaArrayList.add(model);
                }

                adapterCatego = new AdapterCat(InicioAdminActivity.this, categoriaArrayList);
                binding.RVcategorias.setAdapter(adapterCatego);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void ValidacionUsuario() {//Correo Layut
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else {
            String email = firebaseUser.getEmail();
            binding.Subtitulo.setText(email);
        }
    }
}