package com.example.libery.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libery.Filters.FiltroBuscaCat;
import com.example.libery.PdfListAdminActivity;
import com.example.libery.models.ModeloCat;
import com.example.libery.databinding.CategoriaRowBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterCat extends RecyclerView.Adapter<AdapterCat.HolderCategoria> implements Filterable {

    private Context context;
    public ArrayList<ModeloCat> categoriaArrayList, filtrarLista;
    private CategoriaRowBinding binding;    //binding
    private FiltroBuscaCat filtro;

    public AdapterCat(Context context, ArrayList<ModeloCat> categoriaArrayList) {
        this.context = context;
        this.categoriaArrayList = categoriaArrayList;
        this.filtrarLista = categoriaArrayList;
    }

    //METODOS
    @NonNull
    @Override
    public HolderCategoria onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//Bindin xml
        binding = CategoriaRowBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderCategoria(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCat.HolderCategoria holder, int position) {//conseguir datos
        ModeloCat model = categoriaArrayList.get(position);
        String id = model.getId();
        String categoria = model.getCategoria();
        String uid = model.getUid();
        String timestap = model.getTimestap();

        holder.CategoryTitle.setText(categoria);//Envio categpria


        holder.BtnDelete.setOnClickListener(new View.OnClickListener() {// Boton de borrar
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);//confirmacion eliminar
                builder.setTitle("Eliminar")
                        .setMessage("Estas seguro de eliminar esta categoria")
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

        //Clic PdfListAdmin pasa a categoria y catId
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PdfListAdminActivity.class);
                intent.putExtra("categoriaId", id);
                intent.putExtra("categoriaTitulo", categoria);
                context.startActivity(intent);
            }
        });
    }

    private void deleteCat(ModeloCat model, HolderCategoria holder) {//Metodo para eliminar
        String id = model.getId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categorias Libros");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Ekiminaxicon satisfactoria
                        Toast.makeText(context, "Se ha eliminado la categoria", Toast.LENGTH_SHORT).show();
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
        return categoriaArrayList.size();
    }

    @Override
    public Filter getFilter() {//Metodo filtrar la busqueda
        if (filtro == null) {
            filtro = new FiltroBuscaCat(filtrarLista, this);

        }
        return filtro;
    }

    class HolderCategoria extends RecyclerView.ViewHolder {//RecycleView

        TextView CategoryTitle;
        ImageButton BtnDelete;

        public HolderCategoria(@NonNull View itemView) {
            super(itemView);
            CategoryTitle = binding.CategoryTitulo;
            BtnDelete = binding.BtnDelete;
        }
    }
}
