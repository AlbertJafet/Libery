package com.example.libery.models;

public class ModeloPDF {

    String uid, id, titulo, descripcion, categoriaId, url;
    long timestap, viewCount, downloandCount;

    public ModeloPDF() {

    }

    public ModeloPDF(String uid, String id, String titulo, String descripcion, String categoriaId, String url, long timestap, long viewCount, long downloandCount) {
        this.uid = uid;
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoriaId = categoriaId;
        this.url = url;
        this.timestap = timestap;
        this.viewCount = viewCount;
        this.downloandCount = downloandCount;
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

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
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

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public long getDownloandCount() {
        return downloandCount;
    }

    public void setDownloandCount(long downloandCount) {
        this.downloandCount = downloandCount;
    }

    /*---------------Funcion loadPdfUrl---------------*/
    public static final long MAX_BYTES_PDF = 50000000;//50MB
}
