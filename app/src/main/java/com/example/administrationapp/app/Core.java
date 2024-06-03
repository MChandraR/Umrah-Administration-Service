package com.example.administrationapp.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class Core {
    SharedPreferences.Editor Editor;

    public SharedPreferences getSharedPreference(Context context){
        return context.getSharedPreferences("mcr",Context.MODE_PRIVATE);
    }

    public SharedPreferences.Editor getSharedPreferenceEditor(Context context){
        return context.getSharedPreferences("mcr",Context.MODE_PRIVATE).edit();
    }

    public void UpdateUserData(Context context,String user_id,String nama,String email){
        Editor = context.getSharedPreferences("mcr",Context.MODE_PRIVATE).edit();
        Editor.putString("user_id",user_id);
        Editor.putString("nama",nama);
        Editor.putString("email",email);
        Editor.apply();
    }

    public String getPakcageName(){
        return "com.example.administrationapp";
    }

    public String getFileType(Uri uri, Context context){
        String type = context.getContentResolver().getType(uri);
        int index = 0;
        for(int i = 0 ; i < type.length() ; i++){
            if(type.charAt(i) == (char)'/'){
               index = i;
                break;
            }
        }
        type = type.substring(index+1);
        return type;
    }

    public String getFileName(Uri uri, Context context){
        String name = uri.getPath();
        int index = 0;
        for(int i = 0 ; i < name.length() ; i++){
            if(name.charAt(i) == (char)':'){
                index = i;
                break;
            }
        }
        name = name.substring(index+1);
        return name;
    }

}
