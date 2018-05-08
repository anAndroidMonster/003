package com.example.tthtt.model;

import java.util.List;

/**
 * Created by book4 on 2018/4/9.
 */

public class GetPhoneNumResultModel {
    private String operator;//电信
    private List<GetPhoneNumCityModel> area;//城市列表

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<GetPhoneNumCityModel> getArea() {
        return area;
    }

    public void setArea(List<GetPhoneNumCityModel> area) {
        this.area = area;
    }
}
