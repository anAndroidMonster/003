package com.example.tthtt.logic.syncPhoneDb.net;

import com.example.tthtt.logic.syncPhoneDb.model.NetOutModel;

import retrofit2.Call;
import retrofit2.http.HTTP;

/**
 * Created by book4 on 2018/1/29.
 */

public interface GetPhoneService {
    @HTTP(method = "GET", path = "api/v1/phone/info?p_key=&t_key=baidu_com.acn.xiaofei&d_key=HWYBAK3WN9ZG15420A690HA7Y5281YMW")
    Call<NetOutModel> getNewPhone();
}
