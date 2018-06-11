package com.yhqs.commons.security.config.converter;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import com.yhqs.commons.security.converter.WahHttpMessageConverter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class HttpMessageConverterConfig{

    private List<MediaType> SUPPORTED_MEDIA_TYPES = new ArrayList<MediaType>(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML));
    private Charset         DEFAULT_CHARSET       = Charset.forName("UTF-8");

    @Bean
    public GsonHttpMessageConverter wahHttpMessageConverter(){
        WahHttpMessageConverter converter = new WahHttpMessageConverter();

        //配置
        converter.setSupportedMediaTypes(SUPPORTED_MEDIA_TYPES);

        return converter;
    }

    @Bean
    public HttpMessageConverter stringHttpMessageConverter(){
        StringHttpMessageConverter converter = new StringHttpMessageConverter(DEFAULT_CHARSET);

        return converter;
    }

    @Bean
    public HttpMessageConverters httpMessageConverters(){
        return new HttpMessageConverters(true, Arrays.asList(wahHttpMessageConverter(), stringHttpMessageConverter()));
    }
}
