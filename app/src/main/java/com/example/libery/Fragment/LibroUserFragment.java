package com.example.libery.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.libery.Adapters.AdapterPdfAdmin;
import com.example.libery.Adapters.AdapterPdfUser;
import com.example.libery.PdfListAdminActivity;
import com.example.libery.R;
import com.example.libery.databinding.FragmentLibroUserBinding;
import com.example.libery.models.ModeloPDF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class LibroUserFragment extends Fragment {

    private String catId;
    private String categoria;
    private String uid;

    private ArrayList<ModeloPDF> pdfArrayList;
    private AdapterPdfUser adapterPdfUser;
    private FragmentLibroUserBinding binding;
    private static final String TAG = "PDF_USER:TAG";

    public LibroUserFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LibroUserFragment newInstance(String catId, String categoria, String uid) {
        LibroUserFragment fragment = new LibroUserFragment();
        Bundle args = new Bundle();
        args.putString("categoriaId", catId);
        args.putString("categoria", categoria);
        args.putString("uid", uid);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catId = getArguments().getString("categoriaId");
            categoria = getArguments().getString("categoria");
            uid = getArguments().getString("uid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLibroUserBinding.inflate(LayoutInflater.from(getContext()), container, false);
        Log.d(TAG, "onCreateView: categoria" + categoria);

        if (categoria.equals("Todos")) {
            loadTodos();

        } else if (categoria.equals("Descargados")) {
            loadMasVistos_Deacargados("DownloadCount");

        } else {
            loadCategorias();

        }

        //Buscar
        binding.EdBuscarLibroUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterPdfUser.getFilter().filter(s);
                } catch (Exception e) {
                    Log.d(TAG, "onTextChanged: " + e.getMessage());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }

    private void loadTodos() {//Cargar todos los libros
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Libros");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    ModeloPDF modelo = ds.getValue(ModeloPDF.class);

                    pdfArrayList.add(modelo);

                    adapterPdfUser = new AdapterPdfUser(getContext(), pdfArrayList);

                    binding.ViewLibro.setAdapter(adapterPdfUser);

                    Log.d(TAG, "onDataChange: " + modelo.getId() + "" + modelo.getTitulo());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadMasVistos_Deacargados(String orderBy) {//Cargar los libros mas descargados
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Libros");
        ref.orderByChild(orderBy).limitToLast(10)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pdfArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            ModeloPDF modelo = ds.getValue(ModeloPDF.class);

                            pdfArrayList.add(modelo);

                            adapterPdfUser = new AdapterPdfUser(getContext(), pdfArrayList);
                            binding.ViewLibro.setAdapter(adapterPdfUser);

                            Log.d(TAG, "onDataChange: " + modelo.getId() + "" + modelo.getTitulo());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadCategorias() {//Cargan todas las categorias
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Libros");
        ref.orderByChild("categoriaId").equalTo(catId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pdfArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            ModeloPDF modelo = ds.getValue(ModeloPDF.class);

                            pdfArrayList.add(modelo);

                            adapterPdfUser = new AdapterPdfUser(getContext(), pdfArrayList);
                            binding.ViewLibro.setAdapter(adapterPdfUser);

                            Log.d(TAG, "onDataChange: " + modelo.getId() + "" + modelo.getTitulo());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}