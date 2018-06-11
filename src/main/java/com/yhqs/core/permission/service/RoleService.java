package com.yhqs.core.permission.service;

import com.yhqs.core.permission.entity.Role;
import org.wah.doraemon.entity.consts.UsingState;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;

public interface RoleService{

    void save(Role role);

    void update(Role role);

    Role getById(String id);

    Page<Role> page(PageRequest pageRequest, String id, String name, UsingState state, Boolean isAdmin, String accountId);
}
