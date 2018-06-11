package com.yhqs.core.permission.service;

import com.yhqs.core.permission.entity.Function;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;

import java.util.List;

public interface FunctionService {

    void save(Function function);

    void update(Function function);

    List<Function> find(String url, RequestMethod method, Boolean needAllot);

    Function getById(String id);

    Page<Function> page(PageRequest pageRequest, String id, String url, String description, Boolean needAllot, RequestMethod method);
}