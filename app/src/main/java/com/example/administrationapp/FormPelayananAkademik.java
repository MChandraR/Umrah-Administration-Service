package com.example.administrationapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrationapp.api.FormAPI;
import com.example.administrationapp.apimodel.FormPelayananModel;
import com.example.administrationapp.apimodel.session.SessionAPIModel;
import com.example.administrationapp.app.ClientAPI;
import com.example.administrationapp.app.Core;
import com.example.administrationapp.app.Feature;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FormPelayananAkademik extends AppCompatActivity {

    private TextInputEditText NamaField,NimField,KeteranganField;
    private Spinner JenisPelayananList;
    private String jenispelayanan;
    private Feature appFeature;
    private View parentView;
    private SharedPreferences SP;
    private Core appCore ;
    private TextView UserInfo,FileSelectedText;
    private Button SubmitBtn,CancelBtn,UploadFileBtn;
    private ArrayList<String> typeAllowed;
    private Date date;
    private String timeStamp;
    private ClientAPI clientAPI;
    private Retrofit formAPI;
    private String userId,Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_pelayanan_akademik);

        parentView = findViewById(R.id.FormPelayananParentView);
        JenisPelayananList = (Spinner) findViewById(R.id.FormPelayananJenisPelayanan);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jenis_pelayanan,
                R.layout.jenis_pelayanan_textview);
        adapter.setDropDownViewResource(R.layout.jenis_pelayanan_textview);
        JenisPelayananList.setAdapter(adapter);
        UserInfo = findViewById(R.id.FormPelayananUserInfo);
        SubmitBtn = findViewById(R.id.FormPelayananSubmitBtn);
        CancelBtn = findViewById(R.id.FormPelayananCancelBtn);
        UploadFileBtn = findViewById(R.id.FormPelayananUploadBtn);
        FileSelectedText = findViewById(R.id.FormPelayananFileSelecText);
        NamaField = findViewById(R.id.FormPelayananNama);
        NimField = findViewById(R.id.FormPelayananNIM);
        KeteranganField = findViewById(R.id.FormPelayananKeterangan);

        SP = this.getSharedPreferences("mcr",MODE_PRIVATE);
        userId = SP.getString("user_id","");
        Token = SP.getString("token","");

        jenispelayanan = "";
        appFeature = new Feature();
        appCore = new Core();
        clientAPI = new ClientAPI();

        typeAllowed = new ArrayList<String>();
        typeAllowed.add(".pdf");
        typeAllowed.add(".png");
        typeAllowed.add(".jpg");
        typeAllowed.add(".jpeg");
        typeAllowed.add(".webp");

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        date = Calendar.getInstance().getTime();

        SP = appCore.getSharedPreference(FormPelayananAkademik.this);
        UserInfo.setText(SP.getString("nama",""));
        NamaField.setText(SP.getString("nama",""));

        addEvent();
    }

    private void addEvent(){
        JenisPelayananList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        jenispelayanan = "Surat Aktif Kuliah";
                        break;
                    case 1:
                        jenispelayanan = "Surat permohonan KKN";
                        break;
                    case 2:
                        jenispelayanan = "Pembuatan KTM";
                        break;
                    default:
                        jenispelayanan = "";
                        break;
                }

                appFeature.showSnackbar(FormPelayananAkademik.this,parentView,"Jenis Pelayanan yang dipilih : \n" + jenispelayanan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(CancelBtn != null){
            CancelBtn.setOnClickListener(view ->
                    exitForm()
            );
        }

        if(UploadFileBtn != null){
            UploadFileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("*/*");
                    chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                    Intent intent = Intent.createChooser(chooseFile,"Pilih file !");
                    try{
                        startActivityForResult(intent,100);
                    }catch (Exception e ){
                        appFeature.showSnackbar(FormPelayananAkademik.this, parentView,e.toString());
                    }
                }
            });
        }

        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }

    private void sendRequest(){
        formAPI = clientAPI.getClientAPI();
        FormAPI apiInterface = formAPI.create(FormAPI.class);
        Call<FormPelayananModel> resData = apiInterface.sendRequest(new FormPelayananModel(
                NamaField.getText().toString(),
                NimField.getText().toString(),
                jenispelayanan,
                KeteranganField.getText().toString(),
                new SessionAPIModel(userId,Token)
        ));

        resData.enqueue(new Callback<FormPelayananModel>() {
            @Override
            public void onResponse(Call<FormPelayananModel> call, Response<FormPelayananModel> response) {
                if(response.isSuccessful()){
                    appFeature.showSnackbar(FormPelayananAkademik.this,parentView,
                            response.body().getMessage());
                    Handler handler  = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(response.body().getStatus().equals("success")) exitForm();
                        }
                    },2000);
                }else{
                    appFeature.showSnackbar(FormPelayananAkademik.this,parentView,
                            response.message());
                }
            }

            @Override
            public void onFailure(Call<FormPelayananModel> call, Throwable t) {
                appFeature.showSnackbar(FormPelayananAkademik.this,parentView,t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            storeFile(uri,timeStamp);
            //appFeature.showSnackbar(this,parentView,appCore.getFileName(uri,this));
        }
    }

    private void storeFile(Uri uri, String filename){
        InputStream IS;
        //Membaca type file
        String type = "." + appCore.getFileType(uri,FormPelayananAkademik.this);
        try{
             IS = FormPelayananAkademik.this.getContentResolver().openInputStream(uri);
        }catch (Exception e){
             IS = null;
        }

        //Memvalidasi tipe file menggunakan function sendiri
        if(validateFile(type)){
            File rootDir = Environment.getExternalStorageDirectory();
            File folderPath = new File(rootDir,"/Android/data/" + appCore.getPakcageName() + "/files/Documents");
            folderPath.mkdirs();
            File file = new File(folderPath,filename + type );
            OutputStream OS = null;

            try{
                OS = new FileOutputStream(file);
                byte bit[] = new byte[1024];
                IS.read(bit);
                do{
                    OS.write(bit);
                }while(IS.read(bit) != -1);

                FileSelectedText.setText(appCore.getFileName(uri,FormPelayananAkademik.this));

            }catch (Exception e){
                appFeature.showSnackbar(this,parentView,"Ukuran file terlalu besar ! \nMaksimal 10MB");
            }finally {
                try{
                    if(IS != null){
                        IS.close();
                    }
                    if(OS != null) {
                        OS.flush();
                        OS.close();
                    }
                }catch (Exception e){ }
            }

        }else{
            try{ IS.close(); }catch (Exception e){ }
            appFeature.showSnackbar(FormPelayananAkademik.this,parentView,"Format file tidak didukung !\nFormat yang didukung : pdf, png, jpg, jpeg.");
        }

    }

    private boolean validateFile(String type){
        boolean allow = false;
        for (String types : typeAllowed) {
            if(types.equals(type)){
                allow = true;
                break;
            }
        }
        return allow;
    }

    public boolean vaidateSize(File file){
        long size = file.length();
        if(size<5120000){
            return true;
        }else{ return false; }
    }

    private void exitForm(){
        FormPelayananAkademik.this.finish();
    }
}