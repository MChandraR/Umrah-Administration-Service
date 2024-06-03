package com.example.administrationapp.apimodel;


import com.example.administrationapp.apimodel.session.SessionAPIModel;

public class FormPelayananModel {
    String nama,nim,jenis_pelayanan,keterangan,status,message,user_id,token;

    public FormPelayananModel(String nama, String nim, String jenis_pelayanan, String keterangan, SessionAPIModel credential){
        this.nama = nama;
        this.nim = nim;
        this.jenis_pelayanan = jenis_pelayanan;
        this.keterangan = keterangan;
        this.user_id = credential.getUser_id();
        this.token = credential.getToken();
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public String getJenis_pelayanan() {
        return jenis_pelayanan;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
