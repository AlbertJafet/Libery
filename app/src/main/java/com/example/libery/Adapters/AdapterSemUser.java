package com.example.libery.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libery.Guide.GuideListActivity;
import com.example.libery.Guide.GuideListUserActivity;
import com.example.libery.databinding.GuiaRowUserBinding;
import com.example.libery.models.ModeloGuia;

import java.util.ArrayList;

public class AdapterSemUser extends RecyclerView.Adapter<AdapterSemUser.HolderSemUser>{
    private final Context context;
    private GuiaRowUserBinding binding;
    public ArrayList<ModeloGuia> semUserArray;

    public AdapterSemUser(Context context, ArrayList<ModeloGuia> semUserArray) {
        this.context = context;
        this.semUserArray = semUserArray;
    }

    @NonNull
    @Override
    public HolderSemUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = GuiaRowUserBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderSemUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSemUser holder, int position) {

        ModeloGuia model = semUserArray.get(position);
        String id = model.getId();
        String semestre = model.getSemestre();
        String uid = model.getUid();
        String timestap = model.getTimestap();

        holder.tituloGuia.setText(semestre);//Envio categpria

        //Clic GuiaList pasa a semestre y catId
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GuideListUserActivity.class);
                intent.putExtra("semestreUserId", id);
                intent.putExtra("semestreUserTitulo", semestre);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return semUserArray.size();
    }

    class HolderSemUser extends RecyclerView.ViewHolder {

        TextView tituloGuia;

        public HolderSemUser(@NonNull View itemView) {
            super(itemView);

            tituloGuia = binding.GuiaTitulo;
        }
    }
}

