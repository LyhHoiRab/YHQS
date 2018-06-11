package com.yhqs.core.permission.service;

import com.yhqs.core.permission.dao.RoleDao;
import com.yhqs.core.permission.entity.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.wah.doraemon.entity.consts.UsingState;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;
import org.wah.doraemon.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDao roleDao;

    @Override
    @Transactional(readOnly = false)
    public void save(Role role){
        Assert.notNull(role, "角色信息不能为空");

        roleDao.saveOrUpdate(role);
    }

    @Override
    @Transactional(readOnly = false)
    public void update(Role role){
        Assert.notNull(role, "角色信息不能为空");
        Assert.hasText(role.getId(), "角色ID不能为空");

        roleDao.saveOrUpdate(role);
    }

    @Override
    public Role getById(String id){
       Assert.hasText(id, "角色ID不能为空");

       return roleDao.getById(id);
    }

    @Override
    public Page<Role> page(PageRequest pageRequest, String id, String name, UsingState state, Boolean isAdmin, String accountId){
        Assert.notNull(pageRequest, "分页信息不能为空");

        //角色ID列表
        List<String> ids = new ArrayList<String>();
        //查询
        if(StringUtils.isNotBlank(accountId)){
             List<Role> roles = roleDao.findByAccountId(accountId);

             if(roles != null && !roles.isEmpty()){
                 ids.addAll(ObjectUtils.ids(roles));
             }
        }

        return roleDao.page(pageRequest, id, name, state, isAdmin, ids);
    }
}
