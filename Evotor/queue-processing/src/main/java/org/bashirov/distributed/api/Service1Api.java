package org.bashirov.distributed.api;
import java.util.List;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service1Api {
    
    @POST("PASTE ENDPOINT") //insert
    Call<String> insert(@Body String infoJson);
}