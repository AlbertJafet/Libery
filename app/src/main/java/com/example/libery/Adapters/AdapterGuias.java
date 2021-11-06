package com.example.libery.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libery.Guide.GuiaViewActivity;
import com.example.libery.PdfViewActivity;
import com.example.libery.databinding.GuidePdfRowBinding;
import com.example.libery.models.ModeloGuidaDidactica;
import com.example.libery.models.ModeloPDF;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;

public class AdapterGuias extends RecyclerView.Adapter<AdapterGuias.GuiaHolderAdmin> {
    private final Context context;
    private GuidePdfRowBinding binding;
    public ArrayList<ModeloGuidaDidactica> arrayListGuia;

    public AdapterGuias(Context context, ArrayList<ModeloGuidaDidactica> arrayListGuia) {
        this.context = context;
        this.arrayListGuia = arrayListGuia;
    }

    @NonNull
    @Override
    public GuiaHolderAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = GuidePdfRowBinding.inflate(LayoutInflater.from(context),parent, false);

        return new GuiaHolderAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull GuiaHolderAdmin holder, int position) {

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
                "" +guiaUrl,
                "" + titulo,
                holder.guiaView,
                holder.progressBar);

        Features.loadSemestre(//PDF CARGA CATEGORIA
                "" + semestre,
                holder.TxtCatPdf);


        holder.moreBtn.setOnClickListener(new View.OnClickListener() {//Mas opciones
            @Override
            public void onClick(View v) {
                OpcionesMas(modeloGuidaDidactica, holder);
            }
        });

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

    private void OpcionesMas(ModeloGuidaDidactica modeloGuia, GuiaHolderAdmin holder) {//Opciones Btn More
        String guiaId = modeloGuia.getId();
        String guiaUrl = modeloGuia.getUrl();
        String guiaTitulo = modeloGuia.getTitulo();
        String[] opciones = {"Elimnar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Opciones")
                .setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Features.DeleteGuia(
                                context,
                                "" + guiaId,
                                "" + guiaUrl,
                                "" + guiaTitulo
                        );
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return arrayListGuia.size();
    }

    class GuiaHolderAdmin extends RecyclerView.ViewHolder{

        PDFView guiaView;
        ProgressBar progressBar;
        TextView TxtTituloGuia, TxtDescPdf, TxtCatPdf, TxtSize, TxtFechaPdf;
        ImageButton moreBtn;
        Button BtnLeer, BtnDescarga;

        public GuiaHolderAdmin(@NonNull View itemView) {
            super(itemView);

            guiaView = binding.GuideView;
            progressBar = binding.ProgreBar;
            TxtTituloGuia = binding.TxtTituloGuia;
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
