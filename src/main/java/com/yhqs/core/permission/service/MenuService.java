package com.yhqs.core.permission.service;

import com.yhqs.core.permission.entity.Menu;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;

import java.util.List;

public interface MenuService{

    void save(Menu menu);

    void update(Menu menu);

    Menu getById(String id);

    Page<Menu> page(PageRequest pageRequest, String id, String name, String url, String parentId, Boolean isParent);

    List<Menu> getByTicket(String ticket) throws Exception;
}
