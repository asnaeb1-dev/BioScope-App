package com.example.bioscope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bioscope.API.AdminRoutes;
import com.example.bioscope.POJO.CinemaPOJO;
import com.example.bioscope.POJO.LogoutPOJO;
import com.example.bioscope.POJO.PushClass;
import com.example.bioscope.POJO.Subclass.AdminPresent;
import com.example.bioscope.POJO.Suggestions;
import com.example.bioscope.POJO.UploadedObj;
import com.example.bioscope.Utility.Config;
import com.example.bioscope.Utility.MemoryAccess;
import com.example.bioscope.Utility.MovieObj;
import com.example.bioscope.Utility.UploaderAdapter;
import com.example.bioscope.Utility.UploaderMovieObj;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploaderActivity extends AppCompatActivity {

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private EditText searchTextView;
    private Button searchButton;
    private TextView fileName;
    private RecyclerView uploadList;
    private FloatingActionButton uploadFAB;
    private MovieObj movieObj;
    private StorageReference mStorageRef;
    private FirebaseStorage firebaseStorage;
    private List<CinemaPOJO> uploadedMoviesList;
    private ProgressBar fileProgressBar;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    private boolean isCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploader);

        slidingUpPanelLayout = findViewById(R.id.adminPanelLayout);
        searchTextView = findViewById(R.id.searchBarAdmin);
        searchButton = findViewById(R.id.searchButton);
        fileName = findViewById(R.id.file);
        uploadFAB = findViewById(R.id.uploadMovieAdmin);
        uploadList = findViewById(R.id.uploadListRV);
        fileProgressBar = findViewById(R.id.fileProgress);
        toolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(toolbar);
        uploadFAB.setEnabled(false);

        slidingUpPanelLayout.setPanelHeight(0);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = firebaseStorage.getReference();
        movieObj = new MovieObj();

        if(!isCalled){
            getUploadedVideos();
            isCalled = true;
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateLoadingDialog("Looking...");
                getSuggestions(searchTextView.getText().toString());
            }
        });

        uploadFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemoryAccess memoryAccess = new MemoryAccess(UploaderActivity.this);
                ArrayList<String> movieArr = memoryAccess.getAllMovies();
                generateDialog(movieArr);
            }
        });
        registerReceiver(broadcastReceiver, new IntentFilter("DELETE_MOVIE_BROADCAST"));
    }

    private void generateLoadingDialog(String message){
        progressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), message, true);
        progressDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.profile_admin:
                return true;

            case R.id.logout_admin:
                new AlertDialog.Builder(UploaderActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                generateLoadingDialog("Logging admin out...");
                                progressDialog.show();
                                logoutAdmin();
                            }
                        }).setNegativeButton("No", null).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutAdmin() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).build();
        AdminRoutes admin = retrofit.create(AdminRoutes.class);
        String token = getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("ADMIN_TOKEN", null);
        Call<LogoutPOJO> call = admin.logoutAdmin(token);
        call.enqueue(new Callback<LogoutPOJO>() {
            @Override
            public void onResponse(Call<LogoutPOJO> call, Response<LogoutPOJO> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    if(response.body().getMessage().equals("logged_out")){
                        SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS", MODE_PRIVATE).edit();
                        editor.putString("ADMIN_TOKEN", null);
                        editor.apply();
                        progressDialog.dismiss();
                        startActivity(new Intent(UploaderActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LogoutPOJO> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            generateLoadingDialog("Removing movie...");
            removeMovie(intent.getIntExtra("position", -1));
        }
    };

    private void getUploadedVideos(){
        generateLoadingDialog("Loading. Please wait....");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AdminRoutes admin = retrofit.create(AdminRoutes.class);
        Call<List<CinemaPOJO>> call = admin.getUploadedMovies(getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("ADMIN_TOKEN", null));
        call.enqueue(new Callback<List<CinemaPOJO>>() {
            @Override
            public void onResponse(Call<List<CinemaPOJO>> call, Response<List<CinemaPOJO>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    uploadedMoviesList = response.body();
                    uploadList.setAdapter(new UploaderAdapter(UploaderActivity.this, uploadedMoviesList));
                    uploadList.setLayoutManager(new LinearLayoutManager(UploaderActivity.this));
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<CinemaPOJO>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void uploadVideo(String path){

        fileName.setVisibility(View.VISIBLE);
        fileName.setText(path);
        fileProgressBar.setVisibility(View.VISIBLE);

        Uri file = Uri.fromFile(new File(path));
        final StorageReference riversRef = mStorageRef.child("movies/"+new Config().generateVideoName());

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //push video data to backend
                        fileProgressBar.setProgress(0);
                        fileName.setVisibility(View.GONE);
                        fileName.setText("");
                        fileProgressBar.setVisibility(View.GONE);

                        //push it to the database
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //put the url in a movie object
                                movieObj.setUrl(uri.toString());
                                generateLoadingDialog("Pushing...");
                                //push it to db
                                pushToDB(movieObj.getTmdbID(), movieObj.getUrl());
                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        fileProgressBar.setProgress((int)progress);

                    }
                 })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UploaderActivity.this, "Failed to upload!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeMovie(final int position){
        StorageReference ref = firebaseStorage.getReferenceFromUrl(uploadedMoviesList.get(position).getUrl());
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                removeFromDB(uploadedMoviesList.get(position).getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploaderActivity.this, "Failed to remove! Try again.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public void uploadPanel(View view) {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    private void pushToDB(long movieid, String url){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AdminRoutes admin = retrofit.create(AdminRoutes.class);
        PushClass pushClass = new PushClass(movieid, url);
        Call<PushClass> call = admin.pushMovie(pushClass, getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("ADMIN_TOKEN", null));
        call.enqueue(new Callback<PushClass>() {
            @Override
            public void onResponse(Call<PushClass> call, Response<PushClass> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    String message = response.body().getMessage();
                    if(message.equals("success")){
                        Toast.makeText(UploaderActivity.this, "Successfully pushed!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<PushClass> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("ERROR", t.getMessage());
            }
        });

    }

    private void removeFromDB(String dbID){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).build();

        AdminRoutes admin = retrofit.create(AdminRoutes.class);
        String token = getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("ADMIN_TOKEN", null);
        Call<CinemaPOJO> call = admin.removeMovie(dbID, token);
        call.enqueue(new Callback<CinemaPOJO>() {
            @Override
            public void onResponse(Call<CinemaPOJO> call, Response<CinemaPOJO> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    if(response.body().getId()!=null){
                        Toast.makeText(UploaderActivity.this, "Successfully removed!", Toast.LENGTH_SHORT).show();
                        uploadedMoviesList.clear();
                        progressDialog.dismiss();
                        getUploadedVideos();
                    }else{
                        Toast.makeText(UploaderActivity.this, "Failed to remove!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CinemaPOJO> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                progressDialog.dismiss();
            }
        });

    }

    private void getSuggestions(String search_string){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.getBaseUrl()).addConverterFactory(GsonConverterFactory.create()).build();

        AdminRoutes admin = retrofit.create(AdminRoutes.class);
        String token = getSharedPreferences("MY_PREFS", MODE_PRIVATE).getString("ADMIN_TOKEN", null);
        Call<List<Suggestions>> call = admin.getSuggestions(search_string, token);
        call.enqueue(new Callback<List<Suggestions>>() {
            @Override
            public void onResponse(Call<List<Suggestions>> call, Response<List<Suggestions>> response) {
                if(response.isSuccessful()){
                    assert response.body()!=null;
                    progressDialog.dismiss();
                    generateMovieDialog(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Suggestions>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void generateMovieDialog(final List<Suggestions> al) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(UploaderActivity.this);
        builderSingle.setTitle("Select Movie");
        ArrayList<String> al1= new ArrayList<>();
        for(Suggestions suggestion : al){
            al1.add(suggestion.getTitle());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al1);

        builderSingle.setNegativeButton("Cancel",null);

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long movieid = al.get(which).getMovieid();
                movieObj.setTmdbID(movieid);
                searchTextView.setText(al.get(which).getTitle());
                uploadFAB.setVisibility(View.VISIBLE);
                uploadFAB.setEnabled(true);
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void generateDialog(final ArrayList<String> al){
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(UploaderActivity.this);
        builderSingle.setTitle("Select Movie");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, al);

        builderSingle.setNegativeButton("Cancel",null);

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                Toast.makeText(UploaderActivity.this, strName, Toast.LENGTH_SHORT).show();

                uploadVideo(al.get(which));
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }
}
