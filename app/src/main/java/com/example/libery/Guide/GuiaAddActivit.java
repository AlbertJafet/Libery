package com.example.libery.Guide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.libery.databinding.ActivityGuiaAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class GuiaAddActivit extends AppCompatActivity {
    private ActivityGuiaAddBinding binding;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "ADD_PDF_TAG";
    private static final int PDF_PICK_CODE = 1000;
    private Uri pdfUri;
    private ArrayList<String> arrayTituloListCat, categoriaIdArrayList;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuiaAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        cargarPDFCat();
        progressDialog = new ProgressDialog(this);//progressdialog
        progressDialog.setTitle("Por favor espere");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backbtnPdf.setOnClickListener(new View.OnClickListener() {//REGRESAR
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.ClicBtn.setOnClickListener(new View.OnClickListener() {//AGREGAR PDF
            @Override
            public void onClick(View v) {
                pdfClic();
            }
        });
        binding.EligCat.setOnClickListener(new View.OnClickListener() {//ELEGIR CATEGORIA
            @Override
            public void onClick(View v) {
                CategoriaPDF();
            }
        });
        binding.BtnSubePdf.setOnClickListener(new View.OnClickListener() {//ENVIAR AÑADIR LIBRO
            @Override
            public void onClick(View v) {
                validarDatosPDF();
            }
        });
    }

    private String titulo = "", descripcion = "";

    private void validarDatosPDF() {//Validar datos para enviar                                     STEP 1
        Log.d(TAG, "validarDatosPDF: Validando Datos");

        titulo = binding.EdTituloPdf.getText().toString().trim();//get data
        descripcion = binding.EdDesPdf.getText().toString().trim();

        if (TextUtils.isEmpty(titulo)) {
            Toast.makeText(this, "Ingrese el Titulo del PDF", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(descripcion)) {
            Toast.makeText(this, "Ingrese su descripcion", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(selecCatTitulo)) {
            Toast.makeText(this, "Ingrese el semestre", Toast.LENGTH_SHORT).show();
        } else if (pdfUri == null) {
            Toast.makeText(this, "Seleccione el PDF ", Toast.LENGTH_SHORT).show();
        } else {
            SubirPdfStorange();//datoas validos
        }
    }

    private void SubirPdfStorange() {//subir PDF a storange firebase                                STEP2
        Log.d(TAG, "SubirPdfStorange: Subiendo a storange");

        progressDialog.setMessage("Subiendo PDF");
        progressDialog.show();

        long timestap = System.currentTimeMillis();

        String FileName = "Guias/" + timestap;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(FileName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: Se ha cargado el PDF");
                        Log.d(TAG, "onSuccess: consiguiendo URL ");

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();//URL
                        while (!uriTask.isSuccessful()) ;
                        String SubePDFUrl = "" + uriTask.getResult();

                        ActuPdfInfo(SubePDFUrl, timestap);//sube db fire

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: Fallo al subir el PDF" + e.getMessage());
                Toast.makeText(GuiaAddActivit.this, "Fallo al subir el PDF" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void ActuPdfInfo(String subePDFUrl, long timestap) {//Sube los datos a DB Firebase      STEP 3
        Log.d(TAG, "ActuPdfInfo: Subiendo info a FirebaseDatabases");
        progressDialog.setMessage("Subiendo datos del PDF");

        String uid = firebaseAuth.getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", "" + uid);
        hashMap.put("id", "" + timestap);
        hashMap.put("titulo", "" + titulo);
        hashMap.put("descripcion", "" + descripcion);
        hashMap.put("GuiaId", "" + selecCatId);
        hashMap.put("url", "" + subePDFUrl);
        hashMap.put("timestap", +timestap);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Guia didactica");//DB > Libros
        ref.child("" + timestap)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onDataChange: Informacion Subida");
                        Toast.makeText(GuiaAddActivit.this, "Informacion Actualizada", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: Fallo al subir info DB");
                Toast.makeText(GuiaAddActivit.this, "Fallo al subir la informacion", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void cargarPDFCat() {//cargar categorias de firebase
        Log.d(TAG, "Cargando categorias");
        arrayTituloListCat = new ArrayList<>();
        categoriaIdArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Semestre Guia");//db ref a categorias
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayTituloListCat.clear();
                categoriaIdArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String CatId = "" + ds.child("id").getValue();//get id title
                    String CatTitulo = "" + ds.child("semestre").getValue();

                    arrayTituloListCat.add(CatTitulo);
                    categoriaIdArrayList.add(CatId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String selecCatId, selecCatTitulo;

    private void CategoriaPDF() {//elegir la categoria
        Log.d(TAG, "CategoriaPDF: Mostrando categorias");

        String[] CatArray = new String[arrayTituloListCat.size()];//array de las cat
        for (int i = 0; i < arrayTituloListCat.size(); i++) {
            CatArray[i] = arrayTituloListCat.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige un semestre")
                .setItems(CatArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selecCatTitulo = arrayTituloListCat.get(which);//item4list
                        selecCatId = categoriaIdArrayList.get(which);

                        binding.EligCat.setText(selecCatTitulo);

                        Log.d(TAG, "onClick: Selecciona la categoria" + selecCatId + "" + selecCatTitulo);

                    }
                }).show();
    }

    private void pdfClic() {
        Log.d(TAG, "pdfClic iniciando intento");

        Intent intent = new Intent();
        intent.setType("Aplicaicon/PDF");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selccione el PDF"), PDF_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PDF_PICK_CODE) {
                Log.d(TAG, "onActivityResult : PDF picked");

                pdfUri = data.getData();
                Log.d(TAG, "onActivityResult : Uri" + pdfUri);
            }
        } else {
            Log.d(TAG, "onActivityResult : Cancelled");
            Toast.makeText(this, "Pdf cancelado", Toast.LENGTH_SHORT).show();

        }
    }
}