package com.example.administrationapp.apimodel;

public class BeritaModel {
    public String judul,isi,tanggal;
    int id_berita;

    public BeritaModel(int id,String j,String i, String t){
        id_berita = id;
        judul = j;
        isi = i;
        tanggal = t;
    }

    public String getJudul() {
        return judul;
    }

    public String getIsi() {
        return isi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public int getId_berita() {
        return id_berita;
    }
}
