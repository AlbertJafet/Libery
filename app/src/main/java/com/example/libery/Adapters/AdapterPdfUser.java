package com.example.libery.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libery.Filters.FiltroBuscaPdfUser;
import com.example.libery.PdfViewActivity;
import com.example.libery.databinding.PdfRowUserBinding;
import com.example.libery.models.ModeloPDF;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

public class AdapterPdfUser extends RecyclerView.Adapter<AdapterPdfUser.HolderPdfUser> implements Filterable {

    private PdfRowUserBinding binding;
    private final Context context;
    public ArrayList<ModeloPDF> pdfArrayListUser, filtroListaU;
    private FiltroBuscaPdfUser filtroUser;

    public AdapterPdfUser(Context context, ArrayList<ModeloPDF> pdfArrayList) {
        this.context = context;
        this.pdfArrayListUser = pdfArrayList;
        this.filtroListaU = pdfArrayList;
    }

    @NonNull
    @Override
    public HolderPdfUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = PdfRowUserBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderPdfUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfUser holder, int position) {

        ModeloPDF modeloPDF = pdfArrayListUser.get(position);//conseguir Datos
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

        holder.BtnLeer.setOnClickListener(new View.OnClickListener() {//Leer Libro PdfViewActivity
            @Override
            public void onClick(View v) {
                Intent intentRead = new Intent(context, PdfViewActivity.class);
                intentRead.putExtra("libroId", libroId);
                context.startActivity(intentRead);

            }
        });

    }

    @Override
    public int getItemCount() {
        return pdfArrayListUser.size();
    }

    @Override
    public Filter getFilter() {//filtro
        if (filtroUser == null) {
            filtroUser = new FiltroBuscaPdfUser(filtroListaU, this);
        }
        return filtroUser;
    }

    class HolderPdfUser extends RecyclerView.ViewHolder {

        PDFView pdfView;
        ProgressBar progressBar;
        TextView TxtTituloPdf, TxtDescPdf, TxtCatPdf, TxtSize, TxtFechaPdf;
        Button BtnLeer, BtnDescarga;

        public HolderPdfUser(@NonNull View itemView) {
            super(itemView);

            pdfView = binding.PdfView;
            progressBar = binding.ProgreBar;
            TxtTituloPdf = binding.TxtTituloPdf;
            TxtDescPdf = binding.TxtDescPdf;
            TxtCatPdf = binding.TxtCatPdf;
            TxtFechaPdf = binding.TxtFechaPdf;
            TxtSize = binding.TxtSizePdf;
            BtnLeer = binding.BtnLeer;
            BtnDescarga = binding.BtnDescargar;
        }
    }
}
