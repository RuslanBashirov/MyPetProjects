package org.bashirov.distributed.controller;

import java.io.IOException;

import org.bashirov.distributed.Utils;
import org.bashirov.distributed.api.Service1Api;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Retrofit;

public class AddInformationController {

    public String start(String infoJson) throws IOException {
        Gson gson = Utils.getGson();
        Retrofit retrofit = Utils.getRetrofit(gson);
        Service1Api service1Api = Utils.getService1Api(retrofit);
        Call<String> call = service1Api.insert(infoJson);
        String successOrFail = "Fail";
        successOrFail = call.execute().body();
        
        return successOrFail;
    }
}
