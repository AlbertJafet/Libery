package com.example.libery.models;

public class ModeloGuia {

    String id, semestre, uid, timestap;

    public ModeloGuia(){

    }

    public ModeloGuia(String id, String semestre, String uid, String timestap) {
        this.id = id;
        this.semestre = semestre;
        this.uid = uid;
        this.timestap = timestap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
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
