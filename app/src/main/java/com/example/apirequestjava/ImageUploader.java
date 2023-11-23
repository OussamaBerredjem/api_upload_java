package com.example.apirequestjava;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageUploader {

    private static final String UPLOAD_URL = "http://192.168.1.7/upload/images.php";

    public static void uploadImage(final Context context, Bitmap bitmap, final EditText editText) {
        // Instantiate the RequestQueue

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(context);

        // Convert the image to a Base64 string
        final String imageData = convertBitmapToBase64(bitmap);

        // Create a StringRequest to make a POST request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        progressDialog.dismiss();
                        Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors that occur during the request
                        editText.setText(error.toString());
                        Toast.makeText(context, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Set the parameters for the POST request
                Map<String, String> params = new HashMap<>();
                params.put("file", imageData);
                return params;
            }
        };

        // Set a retry policy with an extended timeout and more attempts
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }

    private static String convertBitmapToBase64(Bitmap bitmap) {
        // Convert the Bitmap to a Base64 string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private String convert(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] img = byteArrayOutputStream.toByteArray();
        return  Base64.encodeToString(img,Base64.DEFAULT);
    }
}
