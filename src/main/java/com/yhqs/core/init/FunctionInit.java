package com.yhqs.core.init;

import com.yhqs.core.permission.consts.ResourceType;
import com.yhqs.core.permission.dao.FunctionDao;
import com.yhqs.core.permission.dao.PermissionDao;
import com.yhqs.core.permission.entity.Function;
import com.yhqs.core.permission.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wah.doraemon.utils.IDGenerator;
import org.wah.doraemon.utils.ObjectUtils;

import java.util.Arrays;

@Component
public class FunctionInit{

    @Autowired
    private FunctionDao functionDao;

    @Autowired
    private PermissionDao permissionDao;

    @Transactional
    public void init(){
        String roleId = "0";

        //创建超级管理员功能
        Function post = new Function();
        post.setId(IDGenerator.uuid32());
        post.setNeedAllot(true);
        post.setMethod(RequestMethod.POST);
        post.setUrl("/**");
        post.setDescription("超级管理员POST请求");

        Function get = new Function();
        get.setId(IDGenerator.uuid32());
        get.setNeedAllot(true);
        get.setMethod(RequestMethod.GET);
        get.setUrl("/**");
        get.setDescription("超级管理员GET请求");

        Function put = new Function();
        put.setId(IDGenerator.uuid32());
        put.setNeedAllot(true);
        put.setMethod(RequestMethod.PUT);
        put.setUrl("/**");
        put.setDescription("超级管理员PUT请求");

        Function delete = new Function();
        delete.setId(IDGenerator.uuid32());
        delete.setNeedAllot(true);
        delete.setMethod(RequestMethod.DELETE);
        delete.setUrl("/**");
        delete.setDescription("超级管理员DELETE请求");

        functionDao.saveList(Arrays.asList(post, get, put, delete));

        //创建超级管理员权限
        Permission postPermission = new Permission();
        postPermission.setType(ResourceType.FUNCTION);
        postPermission.setResourceId(post.getId());

        Permission getPermission = new Permission();
        getPermission.setType(ResourceType.FUNCTION);
        getPermission.setResourceId(get.getId());

        Permission putPermission = new Permission();
        putPermission.setType(ResourceType.FUNCTION);
        putPermission.setResourceId(put.getId());

        Permission deletePermission = new Permission();
        deletePermission.setType(ResourceType.FUNCTION);
        deletePermission.setResourceId(delete.getId());

        permissionDao.saveList(Arrays.asList(postPermission, getPermission, putPermission, deletePermission));

        //角色配置权限
        permissionDao.updateFunctionsToRole(ObjectUtils.ids(Arrays.asList(postPermission, getPermission, putPermission, deletePermission)), roleId);
    }
}
