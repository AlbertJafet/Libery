package com.example.libery.Filters;

import android.widget.Filter;

import com.example.libery.Adapters.AdapterCat;
import com.example.libery.Adapters.AdapterPdfAdmin;
import com.example.libery.models.ModeloCat;
import com.example.libery.models.ModeloPDF;

import java.util.ArrayList;

public class FiltroBuscaPdfAdmin extends Filter {
    ArrayList<ModeloPDF> filtrarLista;
    AdapterPdfAdmin adapterPdfAdmin;

    public FiltroBuscaPdfAdmin(ArrayList<ModeloPDF> filtrarLista, AdapterPdfAdmin adapterPdfAdmin) {
        this.filtrarLista = filtrarLista;
        this.adapterPdfAdmin = adapterPdfAdmin;
    }

    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        Filter.FilterResults results = new Filter.FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();

            ArrayList<ModeloPDF> filterModelo = new ArrayList<>();
            for (int i = 0; i < filtrarLista.size(); i++) {
                if (filtrarLista.get(i).getTitulo().toUpperCase().contains(constraint)) {//validacion
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
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {//Aplica combios al filtro
        adapterPdfAdmin.pdfArrayList = (ArrayList<ModeloPDF>) results.values;

        adapterPdfAdmin.notifyDataSetChanged();//notificacion de cambio

    }
}


