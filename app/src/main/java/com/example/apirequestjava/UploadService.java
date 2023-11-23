package com.example.apirequestjava;

import com.android.volley.Response;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {
    @Multipart
    @POST("images.php")
    Call<Response> uploadFile(@Part("file\"; type = 'multipart/form-data'") File file);
}
