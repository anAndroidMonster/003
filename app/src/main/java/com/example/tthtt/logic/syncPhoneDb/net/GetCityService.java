package com.example.tthtt.logic.syncPhoneDb.net;

import com.example.tthtt.logic.syncPhoneDb.model.GetCityModel;
import com.example.tthtt.logic.syncPhoneDb.model.NetOutModel;

import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by book4 on 2018/1/29.
 */

public interface GetCityService {
    @HTTP(method = "GET", path = "iplookup/iplookup.php?format=json")
    Call<GetCityModel> getCity();
}
