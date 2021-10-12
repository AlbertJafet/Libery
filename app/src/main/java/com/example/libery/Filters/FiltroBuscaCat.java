package com.example.libery.Filters;

import android.widget.Filter;

import com.example.libery.Adapters.AdapterCat;
import com.example.libery.models.ModeloCat;

import java.util.ArrayList;

public class FiltroBuscaCat extends Filter {

    ArrayList<ModeloCat> filtrarLista;
    AdapterCat adapterCat;

    public FiltroBuscaCat(ArrayList<ModeloCat> filtrarLista, AdapterCat adapterCat) {
        this.filtrarLista = filtrarLista;
        this.adapterCat = adapterCat;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();

            ArrayList<ModeloCat> filterModelo = new ArrayList<>();
            for (int i = 0; i < filtrarLista.size(); i++) {
                if (filtrarLista.get(i).getCategoria().toUpperCase().contains(constraint)) {//validacion
                    filterModelo.add(filtrarLista.get(i));


                }
            }
            results.count = filterModelo.size();
            results.values = filterModelo;

        } else {
            results.count = filtrarLista.size();
            results.values = filtrarLista;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {//Aplica combios al filtro
        adapterCat.categoriaArrayList = (ArrayList<ModeloCat>) results.values;

        adapterCat.notifyDataSetChanged();//notificacion de cambio

    }
}
