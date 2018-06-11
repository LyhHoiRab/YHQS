package com.yhqs.core.permission.service;

import com.yhqs.core.permission.dao.FunctionDao;
import com.yhqs.core.permission.entity.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FunctionServiceImpl implements FunctionService{

    @Autowired
    private FunctionDao functionDao;

    @Override
    @Transactional(readOnly = false)
    public void save(Function function){
        Assert.notNull(function, "功能信息不能为空");

        functionDao.saveOrUpdate(function);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(Function function){
        Assert.notNull(function, "功能信息不能为空");
        Assert.hasText(function.getId(), "功能ID不能为空");

        functionDao.saveOrUpdate(function);
    }

    @Override
    public List<Function> find(String url, RequestMethod method, Boolean needAllot){
        return functionDao.find(url, method, needAllot, null);
    }

    @Override
    public Function getById(String id){
        Assert.hasText(id, "功能ID不能为空");

        return functionDao.getById(id);
    }

    @Override
    public Page<Function> page(PageRequest pageRequest, String id, String url, String description, Boolean needAllot, RequestMethod method){
        Assert.notNull(pageRequest, "分页信息不能为空");

        return functionDao.page(pageRequest, id, url, description, needAllot, method);
    }
}
