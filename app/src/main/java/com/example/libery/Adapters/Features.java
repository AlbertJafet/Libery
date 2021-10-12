package com.example.libery.Adapters;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.libery.models.ModeloPDF;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.example.libery.models.ModeloPDF.MAX_BYTES_PDF;

public class Features extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /*------------------------------FUNCIONES PARA ESTABLECER DIA, MES Y AÑO------------------------------*/
    //Tiempo
    public static String formatTimestap(long timestap) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestap);

        String fecha = DateFormat.format("dd/MM/yyyy", calendar).toString();

        return fecha;
    }

    /*------------------------------FUNCIONES PARA ELIMINAR LIBRO------------------------------*/
    //Borrar Libro Storage y RealTime
    public static void DeleteBook(Context context, String libroId, String libroUrl, String libroTitulo) {
        String TAG = "PDF_DELETE_TAG";//logd

        Log.d(TAG, "DeleteBook: Borrando libro");
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Por favor espere...");
        progressDialog.setMessage("Borrando Libro" + libroTitulo);
        progressDialog.show();

        Log.d(TAG, "DeleteBook: Borrando libro de storage");
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(libroUrl);
        storageReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Brorrando de storange");

                        Log.d(TAG, "onSuccess: Borrando info de DB");
                        DatabaseReference refer = FirebaseDatabase.getInstance().getReference("Libros");
                        refer.child(libroId)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: Eliminado de DB");
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Se ha eliminado la informacion del Libro", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Fallo al eliminar de DB");
                                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Fallo al borrar el libro");
                progressDialog.dismiss();
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*------------------------------FUNCIONES PARA ADAPTADORES------------------------------*/
    //Load PdfSize
    public static void loadPdfSize(String pdfUrl, String pdfTitle, TextView TxtSize) {
        String TAG = "PDF_SIZE_TAG";//logd

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getMetadata()
                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        double bytes = storageMetadata.getSizeBytes();//size en byte
                        Log.d(TAG, "onSuccess: " + pdfTitle + "" + bytes);

                        double Kb = bytes / 1024;
                        double Mb = Kb / 1024;

                        if (Mb >= 1) {
                            TxtSize.setText(String.format("%.2f", Mb) + " MB");
                        } else if (Kb >= 1) {
                            TxtSize.setText(String.format("%.2f", Mb) + " KB");
                        } else {
                            TxtSize.setText(String.format("%.2f", bytes) + " Bytes");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());

            }
        });
    }

    //Load PdfUrl
    public static void loadPdfUrl(String pdfUrl, String pdfTitle, PDFView pdfView, ProgressBar progressBar) {
        String TAG = "PDF_URL_TAG";//logd

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getBytes(MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d(TAG, "onSuccess: " + pdfTitle + "Se obtuvo el archivo");

                        pdfView.fromBytes(bytes)
                                .pages(0)//primera hoja
                                .spacing(0)
                                .swipeHorizontal(false)
                                .enableSwipe(false)
                                .onError(new OnErrorListener() {
                                    @Override
                                    public void onError(Throwable t) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "onError: " + t.getMessage());

                                    }
                                }).onPageError(new OnPageErrorListener() {
                            @Override
                            public void onPageError(int page, Throwable t) {
                                Log.d(TAG, "onPageError: " + t.getMessage());

                            }
                        }).onLoad(new OnLoadCompleteListener() {
                            @Override
                            public void loadComplete(int nbPages) {
                                //pdf visible
                                progressBar.setVisibility(View.INVISIBLE);
                                Log.d(TAG, "loadComplete: PDF CARGADO");

                            }
                        }).load();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onFailure: fallo al conseguir archivo url" + e.getMessage());
            }
        });
    }

    //Load Categoria
    public static void loadCat(String categoriaId, TextView TxtCatPdf) {


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categorias Libros");
        ref.child(categoriaId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String categoria = "" + snapshot.child("categoria").getValue();

                        TxtCatPdf.setText(categoria);//send cat
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    /*------------------------------FUNCIONES PARA ADAPTADORES------------------------------*/
    //Descargar libro
    public static void DescargaLibro(Context context, String libroIdDow, String libroUrlDow, String libroTituloDow) {//(1)
        String TAA = "PDF_DOWLOADER_TAG";

        String nameExt = libroTituloDow + ".pdf";
        Log.d(TAA, "DescargaLibro: " + nameExt);

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setMessage("Descargando " + nameExt + "...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //descarga storage
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(libroUrlDow);
        reference.getBytes(MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.d(TAA, "onSuccess: Descarga completa");
                        Log.d(TAA, "onSuccess: Guardando Libro");

                        GuardandoLibro(context, progressDialog, nameExt, libroIdDow, bytes);

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.d(TAA, "onFailure: Fallo al descargar libro" + e.getMessage());
                Toast.makeText(context, "Fallo la descarga del libro", Toast.LENGTH_SHORT).show();

            }
        });

    }

    //Guardar en descargas
    public static void GuardandoLibro(Context context, ProgressDialog progressDialog, String nameExt, String libroIdDow, byte[] bytes) {//Guardar libro
        String TAG = "PDF_DOWLOADER_SAVE_TAG";

        Log.d(TAG, "GuardandoLibro: Guardando libro");
        try {
            File downBookFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            downBookFolder.mkdirs();

            String filePath = downBookFolder.getPath() + "/" + nameExt;

            FileOutputStream out = new FileOutputStream(filePath);
            out.write(bytes);
            out.close();

            Toast.makeText(context, "Libro guardado en descargas", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "GuardandoLibro: Libro Guardado en descargas");
            progressDialog.dismiss();

            incrementDownload(libroIdDow);

        } catch (Exception e) {
            Log.d(TAG, "GuardandoLibro: Fallo al guardar en descargar");
            Toast.makeText(context, "Fallo al guardar en descargar", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

        }
    }

    private static void incrementDownload(String libroIdDow) {
        String TAG = "Icrement Dowloander";
        Log.d(TAG, "incrementDownload: Incrementa libro descarga");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Libros");
        reference.child(libroIdDow)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String downCont = "" + snapshot.child("DownloadCount").getValue();
                        Log.d(TAG, "onDataChange: contador descargas" + downCont);

                        if (downCont.equals("") || downCont.equals("null")) {
                            downCont = "0";
                        }

                        long newDownCont = Long.parseLong(downCont) + 1;
                        Log.d(TAG, "onDataChange: Nuevo contador" + newDownCont);

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("DownloadCount", newDownCont);

                        //Icrementar en la base de datos
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Libros");
                        reference.child(libroIdDow).updateChildren(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: Contador actualizado");

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Falla al actualizar contador" + e.getMessage());

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    /*------------------------------FUNCIONES PARA INCREMENTAR------------------------------*/
    public static void incremtLibro(String libroId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Libros");
        ref.child(libroId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String viewCount = "" + snapshot.child("viewCount").getValue();

                        if (viewCount.equals("") || viewCount.equals("null")) {
                            viewCount = "0";
                        }

                        long newViewCount = Long.parseLong(viewCount) + 1;
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("viewCount", newViewCount);

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Libros");
                        ref.child(libroId)
                                .updateChildren(hashMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}

