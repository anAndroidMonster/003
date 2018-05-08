package com.example.tthtt.logic.syncPhoneDb.net;

import com.example.tthtt.logic.syncPhoneDb.model.NetOutModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.Query;

/**
 * Created by book4 on 2018/1/29.
 */

public interface GetPhoneNumberService {
    @HTTP(method = "GET", path = "dianhua_api/open/location")
    Call<JsonObject> getPhoneNumber(@Query("tel")String telPhone);
}
