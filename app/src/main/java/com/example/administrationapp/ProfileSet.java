package com.example.administrationapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.administrationapp.api.UserImageAPI;
import com.example.administrationapp.api.UsersDataAPI;
import com.example.administrationapp.apimodel.UserImageAPIModel;
import com.example.administrationapp.apimodel.users.UsersDataAPIModel;
import com.example.administrationapp.apimodel.users.UsersUpdateAPIModel;
import com.example.administrationapp.app.ClientAPI;
import com.example.administrationapp.app.Core;
import com.example.administrationapp.app.connection;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileSet extends AppCompatActivity {
    private CircleImageView ProfileImage;
    private TextInputEditText UsernameField,NameField;
    private SharedPreferences SP;
    private ClientAPI clientAPI;
    private connection conn;
    private Retrofit imageretro,usersretro,updateretro,ppretro;
    private String user_id,token;
    private Button save_btn,cancel_btn;
    private ImageButton change_btn;
    private RequestBody UserImage;
    private MultipartBody.Part ImageBody;
    private InputStream inputStream;
    private ArrayList<String> typeAllowed;
    private View parentView;
    private Drawable ProfilePicturetmp;
    private Core appCore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set);

        ProfileImage = findViewById(R.id.ProfileSetImageProfile);
        UsernameField = findViewById(R.id.ProfileSetUsernameField);
        NameField = findViewById(R.id.ProfileSetNameField);
        save_btn = findViewById(R.id.ProfileSetSaveButton);
        cancel_btn = findViewById(R.id.ProfileSetCancelButton);
        change_btn = findViewById(R.id.ProfileSetChangeImage);
        parentView = ProfileSet.this.findViewById(R.id.ProfileSetView);

        SP = this.getSharedPreferences("mcr",MODE_PRIVATE);
        clientAPI = new ClientAPI();
        appCore = new Core();
        conn = new connection();
        typeAllowed = new ArrayList<>();
        typeAllowed.add("image/png");
        typeAllowed.add("image/webp");
        typeAllowed.add("image/jpeg");
        typeAllowed.add("image/jpg");

        user_id = SP.getString("user_id","");
        token = SP.getString("token","");

        getUserImage();
        getUsersData();
        addEvent();
    }

    private String getUser_id(){
        return user_id;
    }
    private String getToken(){
        return token;
    }

    private void getUserImage(){
        String url = "res/image/profile/" + user_id;
        imageretro = clientAPI.getClientAPI();
        UserImageAPI API = imageretro.create(UserImageAPI.class);
        Call<UserImageAPIModel> res = API.getUserImage(url);

        res.enqueue(new Callback<UserImageAPIModel>() {
            @Override
            public void onResponse(Call<UserImageAPIModel> call, Response<UserImageAPIModel> response) {
                if(response.isSuccessful()){
                    UserImageAPIModel data = response.body();
                    if(data.getStatus().equals("success")){
                        if(ProfileImage != null){
                            Picasso.get().invalidate(conn.getRawUrl() + data.getPath());
                            Picasso.get().load(conn.getRawUrl() + data.getPath()).into(ProfileImage);
                            ProfilePicturetmp = ProfileImage.getDrawable();
                        }
                    }else{
                        showMessage(data.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserImageAPIModel> call, Throwable t) {
                showMessage("Tidak dapat terhubung ke server !");
            }
        });
    }

    private void getUsersData(){
        String url = "user/" + user_id;
        UsersDataAPIModel postdata = new UsersDataAPIModel(token);
        usersretro = clientAPI.getClientAPI();
        UsersDataAPI usersAPI = usersretro.create(UsersDataAPI.class);
        Call<UsersDataAPIModel> res = usersAPI.getUsersData(url,postdata);

        res.enqueue(new Callback<UsersDataAPIModel>() {
            @Override
            public void onResponse(Call<UsersDataAPIModel> call, Response<UsersDataAPIModel> response) {
                UsersDataAPIModel data = response.body();
                if(response.isSuccessful()){
                    if(data.getStatus().equals("success")){
                        ArrayList<UsersDataAPIModel.UsersDetail> detaildata = data.getDetail();
                        UsernameField.setText(detaildata.get(0).getUsername());
                        NameField.setText(detaildata.get(0).getNama());
                        appCore.UpdateUserData(ProfileSet.this,detaildata.get(0).getUser_id(),detaildata.get(0).getNama(),detaildata.get(0).getEmail());
                    }else{
                        showMessage(data.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UsersDataAPIModel> call, Throwable t) {
                showMessage("Error : gagal mendapatkan data user !");
            }
        });
    }

    private void addEvent(){
        if(save_btn != null){
            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username = UsernameField.getText().toString();
                    String name = NameField.getText().toString();
                    if(validateInput(username,name)==true){
                        updateUsersData();
                        save_btn.setEnabled(false);
                    }else{
                        showMessage("Username atau Nama tidak boleh kosong !");
                    }
                }
            });
        }
        if(cancel_btn != null){
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProfileSet.this.finish();
                }
            });
        }

        //Membuka menu file manager
        if(change_btn != null){
            change_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("image/*");
                    chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                    Intent intent = Intent.createChooser(chooseFile, "Choose image file !");
                    try {
                        startActivityForResult(intent,100);
                    }catch(Exception e){
                        showMessage("File manager not found !");
                    }
                }
            });
        }

    }


    //Event ketika File Manager Memberikan response
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 100 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            String mimType = this.getContentResolver().getType(uri);
            boolean isallowed = false;

            for(int i = 0 ; i < typeAllowed.size() ; i++){
                if(typeAllowed.get(i).equals(mimType)){
                    isallowed = true;
                    break;
                }
            }
            //File file = new File(getApplicationInfo().dataDir.toString()+"files/1.png");
            if(isallowed){
                ProfilePicturetmp = ProfileImage.getDrawable();
                ProfileImage.setImageURI(uri);
                storeFile();
            }else{
                showMessage("Tipe file tidak valid !");
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //FUnction untuk menyimpan file ke folder dan validasi
    private void storeFile(){
        Bitmap bitmap = ((BitmapDrawable)ProfileImage.getDrawable() ).getBitmap();

        //validasi jika ukuran file lebih dari 3mb
        if(bitmap.getByteCount() <= 15000000){

            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/Android/data/com.example.administrationapp/files");
            dir.mkdirs();
            String imagename = "profile.PNG";
            File file = new File(dir,imagename);
            OutputStream out;

            try{
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                out.flush();
                out.close();

                UserImage = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                ImageBody = MultipartBody.Part.createFormData("userImage",getUser_id(),UserImage);
                RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"),getUser_id());
                RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"),getToken());
                updateProfilePicture(
                        ImageBody,
                        user_id,
                        token
                );

            }catch (FileNotFoundException e){
                showMessage("Gagal mengupload ! Izin penyimpanan tidak diberikan oleh user !");
            }catch (Exception e){
                showMessage(e.toString());
            }

        }else{
            showMessage("Gagal mengupload ! ukuran file terlalu besar. maksimal 3MB !");
            if(ProfileImage != null && ProfilePicturetmp != null){
                ProfileImage.setImageDrawable(ProfilePicturetmp);
            }
        }
    }



    private void updateUsersData(){
        String url = "user/" + user_id;
        String username = UsernameField.getText().toString();
        String name = NameField.getText().toString();
        UsersUpdateAPIModel updatedata = new UsersUpdateAPIModel(token,"profile",username,name,"","");
        updateretro = clientAPI.getClientAPI();
        UsersDataAPI updateAPI = updateretro.create(UsersDataAPI.class);
        Call<UsersUpdateAPIModel> res = updateAPI.updateUsersData(url,updatedata);

        res.enqueue(new Callback<UsersUpdateAPIModel>() {
            @Override
            public void onResponse(Call<UsersUpdateAPIModel> call, Response<UsersUpdateAPIModel> response) {
                UsersUpdateAPIModel data = response.body();
                if(response.isSuccessful()){
                    showMessage(data.getMessage());
                    getUsersData();
                }else{
                    showMessage("Internal server error !");
                }
                if(save_btn != null){
                    save_btn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<UsersUpdateAPIModel> call, Throwable t) {
                showMessage("Tidak dapat mengupdate data user !");
                if(save_btn != null){
                    save_btn.setEnabled(true);
                }
            }

        });
    }

    public void updateProfilePicture(MultipartBody.Part Image,RequestBody body,RequestBody token){
        showMessage("Mengunggah file ke server !");
        ppretro = clientAPI.getClientAPI();
        UsersDataAPI ppAPI = ppretro.create(UsersDataAPI.class);
        Call<UsersUpdateAPIModel> resdata = ppAPI.updateProfilePicture(
                Image,body,token
        );

        resdata.enqueue(new Callback<UsersUpdateAPIModel>() {
            @Override
            public void onResponse(Call<UsersUpdateAPIModel> call, Response<UsersUpdateAPIModel> response) {
                UsersUpdateAPIModel data = response.body();
                showMessage(data.getMessage());
                getUserImage();
            }

            @Override
            public void onFailure(Call<UsersUpdateAPIModel> call, Throwable t) {
                showMessage("Gagal mengupload foto profil !" + t.toString());
            }
        });
    }


    private void showMessage(String message){
        if(ProfileSet.this != null){
            //Toast.makeText(ProfileSet.this,  message, Toast.LENGTH_SHORT).show();
            try {
                Snackbar.make(ProfileSet.this,parentView,message,Snackbar.LENGTH_SHORT).
                        setBackgroundTint(getResources().getColor(R.color.white)).
                        setActionTextColor(getResources().getColor(R.color.black)).
                        setTextColor(getResources().getColor(R.color.black)).
                        setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).
                        show();
            }catch (Exception e){
                Toast.makeText(ProfileSet.this,  message, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private boolean validateInput(String val1, String val2){
        if(val1.equals("") || val2.equals("")){
            return false;
        }else{
            return true;
        }
    }




}