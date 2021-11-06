package com.example.libery.models;

public class ModeloGuidaDidactica {

    String uid, id, titulo, descripcion, GuiaId, url;
    long timestap;

    public ModeloGuidaDidactica(){
    }

    public ModeloGuidaDidactica(String uid, String id, String titulo, String descripcion, String guiaId, String url, long timestap) {
        this.uid = uid;
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        GuiaId = guiaId;
        this.url = url;
        this.timestap = timestap;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGuiaId() {
        return GuiaId;
    }

    public void setGuiaId(String guiaId) {
        GuiaId = guiaId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestap() {
        return timestap;
    }

    public void setTimestap(long timestap) {
        this.timestap = timestap;
    }
}
