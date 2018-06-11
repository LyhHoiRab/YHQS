package com.yhqs.core.init;

import com.yhqs.core.permission.dao.RoleDao;
import com.yhqs.core.permission.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class RoleInit{

    @Autowired
    private RoleDao roleDao;

    @Transactional
    public void init(){
        String accountId = "0";

        //创建超级管理员角色
        Role role = new Role();
        role.setName("administrator");
        role.setIsAdmin(true);
        roleDao.saveOrUpdate(role);

        //账户配置角色
        roleDao.updateRelationsToAccount(Arrays.asList(role.getId()), accountId);
    }
}
