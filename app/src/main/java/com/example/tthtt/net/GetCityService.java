package com.example.tthtt.net;

import com.example.tthtt.model.GetCityModel;

import retrofit2.Call;
import retrofit2.http.HTTP;

/**
 * Created by book4 on 2018/1/29.
 */

public interface GetCityService {
    @HTTP(method = "GET", path = "iplookup/iplookup.php?format=json")
    Call<GetCityModel> getCity();
}
