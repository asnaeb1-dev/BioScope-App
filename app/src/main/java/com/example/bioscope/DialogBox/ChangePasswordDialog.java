package com.example.bioscope.DialogBox;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bioscope.API.UserRoutes;
import com.example.bioscope.POJO.ChangePassPOJO;
import com.example.bioscope.POJO.CinemaPOJO;
import com.example.bioscope.POJO.UserPOJO;
import com.example.bioscope.R;
import com.example.bioscope.Utility.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ChangePasswordDialog {

    private Context context;

    public ChangePasswordDialog(Context context) {
        this.context = context;
    }

    public void showChangePasswordDialog(){

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 10, 20, 10);
        linearLayout.setLayoutParams(layoutParams);

        final EditText editText = new EditText(context);
        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setHint("Enter previous password...");

        final EditText newPass = new EditText(context);
        newPass.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newPass.setHint("Enter new Password...");

        final EditText cnfPass = new EditText(context);
        cnfPass.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cnfPass.setHint("Confirm password...");

        Button button = new Button(context);
        button.setText("Change Password");
        button.setBackground(context.getResources().getDrawable(R.drawable.rounded_button));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = editText.getText().toString(),
                        newPassword = newPass.getText().toString(),
                        confirmPass = cnfPass.getText().toString();

                if(oldPass.isEmpty() || newPassword.isEmpty() || confirmPass.isEmpty()){
                    Toast.makeText(context, "Cannot leave fields empty!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(newPassword.length()<8){
                        Toast.makeText(context, "Length should be more than 7 characters", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        if(newPassword.equals(confirmPass)){
                            changePassword(oldPass, newPassword);
                        }else{
                            Toast.makeText(context, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });
        linearLayout.addView(editText);
        linearLayout.addView(newPass);
        linearLayout.addView(cnfPass);
        linearLayout.addView(button);

        new AlertDialog.Builder(context)
                .setTitle("Change Password")
                .setView(linearLayout)
                .setNegativeButton("Cancel", null)
                .show();
    }

    private Retrofit initializeRetrofit(String url){
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
    }

    private void changePassword(String oldPass, String newPassword) {

        UserRoutes user = initializeRetrofit(Config.getBaseUrl()).create(UserRoutes.class);
        ChangePassPOJO changePassPOJO = new ChangePassPOJO(oldPass, newPassword);
        Call<UserPOJO> call = user.changePassword(changePassPOJO,context.getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("USER_TOKEN", null));
        call.enqueue(new Callback<UserPOJO>() {
            @Override
            public void onResponse(Call<UserPOJO> call, Response<UserPOJO> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    if(response.body().getId()!=null){
                        Toast.makeText(context, "Successfully Changed Password!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "Failed to change password!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserPOJO> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                Toast.makeText(context, "Failed to change password!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
