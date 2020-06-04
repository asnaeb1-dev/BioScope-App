package com.example.bioscope.DialogBox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.MainActivity;
import com.example.bioscope.MainUserActivity;
import com.example.bioscope.POJO.UserPOJO;
import com.example.bioscope.POJO.UserSignUp;
import com.example.bioscope.R;
import com.example.bioscope.Utility.Config;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class RemoveAccountDialog {

    private Context context;

    public RemoveAccountDialog(Context context) {
        this.context = context;
    }

    public void generateConfirmationDialog(){
        new AlertDialog.Builder(context)
                .setTitle("Delete account")
                .setMessage("Deleting your account is irreversible. All you data will be permanently wiped. Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                }).setNegativeButton("No", null).show();
    }

    private void deleteAccount(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserRoutes admin = retrofit.create(UserRoutes.class);
        Call<UserPOJO> call = admin.removeUser(context.getSharedPreferences("MY_PREFS",MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<UserPOJO>() {
            @Override
            public void onResponse(Call<UserPOJO> call, Response<UserPOJO> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MY_PREFS",MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.remove("USER_TOKEN");
                    editor.apply();
                    context.startActivity(new Intent(context, MainActivity.class));
                    ((Activity)context).finish();
                }else{
                    Toast.makeText(context, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserPOJO> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }
}
