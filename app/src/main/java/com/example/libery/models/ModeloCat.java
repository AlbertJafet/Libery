package com.example.libery.models;

public class ModeloCat {

    String id, categoria, uid, timestap;

    public ModeloCat() {

    }

    public ModeloCat(String id, String categoria, String uid, String timestap) {
        this.id = id;
        this.categoria = categoria;
        this.uid = uid;
        this.timestap = timestap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestap() {
        return timestap;
    }

    public void setTimestap(String timestap) {
        this.timestap = timestap;
    }
}