package com.example.apirequestjava;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    EditText editText;
    Uri image;
    Bitmap bitmap;
    private static final int PICK_IMAGE_REQUEST = 101;
    private static String FILE_PATH = "";
    private static final String url = "http://192.168.1.7/upload/images.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn);
        Button btn2 = findViewById(R.id.btn2);
         editText = findViewById(R.id.name);
         imageView = findViewById(R.id.imagess);

        RequestQueue queue = Volley.newRequestQueue(this);




       /** JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String nom = "";
                            // Iterate through the JSON array and extract "nom" values
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                nom = jsonObject.getString("nom") + "\n" +nom ;
                            }

                            Toast.makeText(MainActivity.this, ""+nom, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);**/


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAndUploadImage();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null){
                   // ImageUploader.uploadImage(MainActivity.this, bitmap, editText);

                   new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ImageUploader.uploadImage(MainActivity.this,bitmap, editText);

                        }
                    },500);
                }
            }
        });



    }

    private void selectAndUploadImage() {
        Intent pickImageIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pickImageIntent.setType("*/*");
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
             image = data.getData();
            if (image != null) {
                try {
                   
                   bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                    imageView.setImageBitmap(bitmap);
                   
                   // 

                    //calling the method uploadBitmap to upload image
                    FILE_PATH = getPathFromUri(image);
                    Toast.makeText(this, ""+FILE_PATH, Toast.LENGTH_SHORT).show();
                    try {
                        upload(image);
                    }catch (Exception e){
                        editText.setText(e.toString());
                        Toast.makeText(this, ""+e.toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }






    }

    private void upload(Uri path){

        //new UploadImageTask().execute();

        File file = new File(getPathFromUri(path));
        try {
            //MyHttpClient.sendPostRequestWithFile("http://192.168.1.7/upload/images.php","file",file);
            new UploadFileTask().execute(file);
        } catch (Exception e) {
            Toast.makeText(this, "error : "+e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            editText.setText(e.toString());
        }


    }



    private class UploadFileTask extends AsyncTask<File, Void, String> {

        @Override
        protected String doInBackground(File... files) {
            if (files.length > 0) {
                try {
                    return uploadFile(files[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error uploading file";
                }
            }
            return "No file to upload";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("MainActivity", "Response: " + result);
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }

        // Modify the method to accept a File parameter
        private String uploadFile(File file) throws IOException {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                    .build();

            Request request = new Request.Builder()
                    .url("http://192.168.1.7/upload/images.php")
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }
    }

    private String getPathFromUri(Uri uri) {
        String path = null;

        // Check if the URI is a content URI
        if ("content".equals(uri.getScheme())) {
            // Use DocumentFile to handle content URIs
            DocumentFile documentFile = DocumentFile.fromSingleUri(this, uri);
            if (documentFile != null && documentFile.exists()) {
                // If the DocumentFile exists, get its file path
                path = documentFile.getUri().getPath();
            }
        }
        // Check if the URI is a file URI
        else if ("file".equals(uri.getScheme())) {
            path = uri.getPath();
        }

        return path;
    }

}
