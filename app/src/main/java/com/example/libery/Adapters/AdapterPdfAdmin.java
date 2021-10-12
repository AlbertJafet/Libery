package com.example.libery.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libery.Filters.FiltroBuscaPdfAdmin;
import com.example.libery.PdfViewActivity;
import com.example.libery.databinding.PdfRowAdminBinding;
import com.example.libery.models.ModeloPDF;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static com.example.libery.models.ModeloPDF.MAX_BYTES_PDF;

public class AdapterPdfAdmin extends RecyclerView.Adapter<AdapterPdfAdmin.PdfHolderAdmin> implements Filterable {
    private Context context;
    public ArrayList<ModeloPDF> pdfArrayList, filtroList;
    private PdfRowAdminBinding binding;
    private FiltroBuscaPdfAdmin filtro;
    private static String TAG = "PDF_ADAPTER_TAG";//logd
    private static String TAA = "PDF_DOWNLOADER_TAA";//logd

    private ProgressDialog progressDialog;

    public AdapterPdfAdmin(Context context, ArrayList<ModeloPDF> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
        this.filtroList = pdfArrayList;

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NonNull//Meotodos Holderm Adapter Recycler View
    @Override
    public PdfHolderAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = PdfRowAdminBinding.inflate(LayoutInflater.from(context), parent, false);

        return new PdfHolderAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPdfAdmin.PdfHolderAdmin holder, int position) {

        ModeloPDF modeloPDF = pdfArrayList.get(position);//conseguir Datos
        String libroId = modeloPDF.getId();
        String categoriaId = modeloPDF.getCategoriaId();
        String titulo = modeloPDF.getTitulo();
        String pdfUrl = modeloPDF.getUrl();
        String descripcion = modeloPDF.getDescripcion();
        long timestap = modeloPDF.getTimestap();

        //Fecha
        String formatFech = Features.formatTimestap(timestap);

        holder.TxtTituloPdf.setText(titulo);//envio datos
        holder.TxtDescPdf.setText(descripcion);
        holder.TxtFechaPdf.setText(formatFech);

        //FUNCIONES DE APLICATION.CLASS
        Features.loadPdfSize( //PDF SIZE
                "" + pdfUrl,
                "" + titulo,
                holder.TxtSize);

        Features.loadPdfUrl(//PDF CARGA URL
                "" + pdfUrl,
                "" + titulo,
                holder.pdfView,
                holder.progressBar);

        Features.loadCat(//PDF CARGA CATEGORIA
                "" + categoriaId,
                holder.TxtCatPdf);


        holder.moreBtn.setOnClickListener(new View.OnClickListener() {//Mas opciones
            @Override
            public void onClick(View v) {
                OpcionesMas(modeloPDF, holder);
            }
        });

        holder.BtnLeer.setOnClickListener(new View.OnClickListener() {//Leer Libro PdfViewActivity
            @Override
            public void onClick(View v) {
                Intent intentRead = new Intent(context, PdfViewActivity.class);
                intentRead.putExtra("libroId", libroId);
                context.startActivity(intentRead);

            }
        });
        holder.BtnDescarga.setOnClickListener(new View.OnClickListener() {//Descargar Libro
            @Override
            public void onClick(View v) {
                Features.DescargaLibro(
                        context,
                        "" + libroId,
                        "" + pdfUrl,
                        "" + titulo
                );
            }
        });
    }

    private void OpcionesMas(ModeloPDF modeloPDF, PdfHolderAdmin holder) {//Opciones Btn More
        String libroId = modeloPDF.getId();
        String libroUrl = modeloPDF.getUrl();
        String libroTitulo = modeloPDF.getTitulo();
        String[] opciones = {"Elimnar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Opciones")
                .setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Features.DeleteBook(
                                context,
                                "" + libroId,
                                "" + libroUrl,
                                "" + libroTitulo
                        );
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    @Override
    public Filter getFilter() {//filtro

        if (filtro == null) {
            filtro = new FiltroBuscaPdfAdmin(filtroList, this);
        }

        return filtro;
    }

    class PdfHolderAdmin extends RecyclerView.ViewHolder {

        PDFView pdfView;
        ProgressBar progressBar;
        TextView TxtTituloPdf, TxtDescPdf, TxtCatPdf, TxtSize, TxtFechaPdf;
        ImageButton moreBtn;
        Button BtnLeer, BtnDescarga;


        public PdfHolderAdmin(@NonNull View itemView) {
            super(itemView);

            pdfView = binding.PdfView;
            progressBar = binding.ProgreBar;
            TxtTituloPdf = binding.TxtTituloPdf;
            TxtDescPdf = binding.TxtDescPdf;
            TxtCatPdf = binding.TxtCatPdf;
            TxtFechaPdf = binding.TxtFechaPdf;
            TxtSize = binding.TxtSizePdf;
            moreBtn = binding.moreBtn;
            BtnLeer = binding.BtnLeer;
            BtnDescarga = binding.BtnDescargar;
        }
    }
}
