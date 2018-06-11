package com.yhqs.commons.security.converter;

import com.google.gson.Gson;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.wah.doraemon.utils.GsonUtils;

public class WahHttpMessageConverter extends GsonHttpMessageConverter{

    private Gson gson;

    public WahHttpMessageConverter(){
        super.setGson(getGson());
    }

    public Gson getGson(){
        if(gson == null){
            gson = GsonUtils.getGson();
        }

        return gson;
    }
}
