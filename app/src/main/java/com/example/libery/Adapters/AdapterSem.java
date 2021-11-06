package com.example.libery.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libery.Guide.GuideListActivity;
import com.example.libery.PdfListAdminActivity;
import com.example.libery.databinding.GuiaRowBinding;
import com.example.libery.models.ModeloCat;
import com.example.libery.models.ModeloGuia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AdapterSem extends RecyclerView.Adapter<AdapterSem.HolderSem> {

    private final Context context;
    public ArrayList<ModeloGuia> semestreArrayList;
    private GuiaRowBinding binding;

    public AdapterSem(Context context, ArrayList<ModeloGuia> semestreArrayList) {
        this.context = context;
        this.semestreArrayList = semestreArrayList;
    }

    @NonNull
    @Override
    public HolderSem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = GuiaRowBinding.inflate(LayoutInflater.from(context), parent, false);

        return new  HolderSem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSem holder, int position) {
        ModeloGuia model = semestreArrayList.get(position);
        String id = model.getId();
        String semestre = model.getSemestre();
        String uid = model.getUid();
        String timestap = model.getTimestap();

        holder.tituloGuia.setText(semestre);//Envio categpria


        holder.deleteGuia.setOnClickListener(new View.OnClickListener() {// Boton de borrar
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);//confirmacion eliminar
                builder.setTitle("Eliminar")
                        .setMessage("Estas seguro de eliminar este semestre")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Eliminando...", Toast.LENGTH_SHORT).show();
                                deleteCat(model, holder);

                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();
            }
        });

        //Clic PdfListAdmin pasa a semestre y catId
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GuideListActivity.class);
                intent.putExtra("semestreId", id);
                intent.putExtra("semestreTitulo", semestre);
                context.startActivity(intent);
            }
        });
    }

    private void deleteCat(ModeloGuia model, AdapterSem.HolderSem holder) {//Metodo para eliminar
        String id = model.getId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Semestre Guia");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Ekiminaxicon satisfactoria
                        Toast.makeText(context, "Se ha eliminado el semestre", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Ocurrio un error
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return semestreArrayList.size();
    }


    class HolderSem extends RecyclerView.ViewHolder{

        TextView tituloGuia;
        ImageButton deleteGuia;

        public HolderSem(@NonNull View itemView) {
            super(itemView);

            tituloGuia = binding.GuiaTitulo;
            deleteGuia = binding.BtnDeleteGuia;
        }
    }
}
