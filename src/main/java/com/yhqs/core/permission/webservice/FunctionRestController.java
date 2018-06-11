package com.yhqs.core.permission.webservice;

import com.yhqs.core.permission.entity.Function;
import com.yhqs.core.permission.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;
import org.wah.doraemon.security.response.Responsed;

@RestController
@RequestMapping(value = "/api/1.0/function")
public class FunctionRestController{

    @Autowired
    private FunctionService functionService;

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Function> save(@RequestBody Function function){
        functionService.save(function);

        return new Responsed<Function>("保存成功", function);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Function> update(@RequestBody Function function){
        functionService.update(function);

        return new Responsed<Function>("更新成功", function);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Function> getById(@PathVariable("id") String id){
        Function function = functionService.getById(id);

        return new Responsed<Function>("查询成功", function);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Page<Function>> page(Long pageNum, Long pageSize, String id, String url, String description, Boolean needAllot, RequestMethod method){
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Page<Function> page = functionService.page(pageRequest, id, url, description, needAllot, method);

        return new Responsed<Page<Function>>("查询成功", page);
    }
}
