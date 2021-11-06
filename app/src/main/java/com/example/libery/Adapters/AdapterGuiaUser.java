package com.example.libery.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libery.Guide.GuiaViewActivity;
import com.example.libery.databinding.GuiaRowUserBinding;
import com.example.libery.databinding.GuidePdfUserRowBinding;
import com.example.libery.models.ModeloGuidaDidactica;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

public class AdapterGuiaUser extends RecyclerView.Adapter<AdapterGuiaUser.HolderGuiaUser> {
    private final Context context;
    private GuidePdfUserRowBinding binding;
    public ArrayList<ModeloGuidaDidactica> arrayListGuia;

    public AdapterGuiaUser(Context context, ArrayList<ModeloGuidaDidactica> arrayListGuia) {
        this.context = context;
        this.arrayListGuia = arrayListGuia;
    }

    @NonNull
    @Override
    public HolderGuiaUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = GuidePdfUserRowBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderGuiaUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGuiaUser holder, int position) {

        ModeloGuidaDidactica modeloGuidaDidactica = arrayListGuia.get(position);
        String Guiaid = modeloGuidaDidactica.getId();
        String guiaUrl = modeloGuidaDidactica.getUrl();
        String titulo = modeloGuidaDidactica.getTitulo();
        String semestre = modeloGuidaDidactica.getGuiaId();
        String descripcion = modeloGuidaDidactica.getDescripcion();
        long timestap = modeloGuidaDidactica.getTimestap();

        //Fecha
        String formatFech = Features.formatTimestap(timestap);

        holder.TxtTituloGuia.setText(titulo);//envio datos
        holder.TxtDescPdf.setText(descripcion);
        holder.TxtFechaPdf.setText(formatFech);

        //FUNCIONES DE APLICATION.CLASS
        Features.loadPdfSize( //PDF SIZE
                "" + guiaUrl,
                "" + titulo,
                holder.TxtSize);

        Features.loadPdfUrl(//PDF CARGA URL
                "" + guiaUrl,
                "" + titulo,
                holder.guiaView,
                holder.progressBar);

        Features.loadSemestre(//PDF CARGA CATEGORIA
                "" + semestre,
                holder.TxtCatPdf);

        holder.BtnLeer.setOnClickListener(new View.OnClickListener() {//Leer Libro PdfViewActivity
            @Override
            public void onClick(View v) {
                Intent intentRead = new Intent(context, GuiaViewActivity.class);
                intentRead.putExtra("GuiaId", Guiaid);
                context.startActivity(intentRead);

            }
        });
        holder.BtnDescarga.setOnClickListener(new View.OnClickListener() {//Descargar Libro
            @Override
            public void onClick(View v) {
                Features.DescargaLibro(
                        context,
                        "" + Guiaid,
                        "" + guiaUrl,
                        "" + titulo
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListGuia.size();
    }

    class HolderGuiaUser extends RecyclerView.ViewHolder {

        PDFView guiaView;
        ProgressBar progressBar;
        TextView TxtTituloGuia, TxtDescPdf, TxtCatPdf, TxtSize, TxtFechaPdf;
        Button BtnLeer, BtnDescarga;

        public HolderGuiaUser(@NonNull View itemView) {
            super(itemView);

            guiaView = binding.GuideView;
            progressBar = binding.ProgreBar;
            TxtTituloGuia = binding.TxtTituloGuia;
            TxtDescPdf = binding.TxtDescPdf;
            TxtCatPdf = binding.TxtCatPdf;
            TxtFechaPdf = binding.TxtFechaPdf;
            TxtSize = binding.TxtSizePdf;
            BtnLeer = binding.BtnLeer;
            BtnDescarga = binding.BtnDescargar;
        }
    }
}
