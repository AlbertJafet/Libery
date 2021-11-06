package com.example.libery.Filters;

import android.widget.Filter;

import com.example.libery.Adapters.AdapterPdfUser;
import com.example.libery.models.ModeloPDF;

import java.util.ArrayList;

public class FiltroBuscaPdfUser extends Filter {
    ArrayList<ModeloPDF> filtrarLista;
    AdapterPdfUser adapterPdfUser;

    public FiltroBuscaPdfUser(ArrayList<ModeloPDF> filtrarLista, AdapterPdfUser adapterPdfUser) {
        this.filtrarLista = filtrarLista;
        this.adapterPdfUser = adapterPdfUser;
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
        adapterPdfUser.pdfArrayListUser = (ArrayList<ModeloPDF>) results.values;

        adapterPdfUser.notifyDataSetChanged();//notificacion de cambio

    }
}
