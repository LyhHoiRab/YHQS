package com.yhqs.core.permission.webservice;

import com.yhqs.core.permission.entity.Role;
import com.yhqs.core.permission.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.wah.doraemon.entity.consts.UsingState;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;
import org.wah.doraemon.security.response.Responsed;

@RestController
@RequestMapping(value = "/api/1.0/role")
public class RoleRestController{

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Role> save(@RequestBody Role role){
        roleService.save(role);

        return new Responsed<Role>("保存成功", role);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Role> update(@RequestBody Role role){
        roleService.update(role);

        return new Responsed<Role>("更新成功", role);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Role> getById(@PathVariable("id") String id){
        Role function = roleService.getById(id);

        return new Responsed<Role>("查询成功", function);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Page<Role>> page(Long pageNum, Long pageSize, String id, String name, UsingState state, Boolean isAdmin, String accountId){
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Page<Role> page = roleService.page(pageRequest, id, name, state, isAdmin, accountId);

        return new Responsed<Page<Role>>("查询成功", page);
    }

    @RequestMapping(value = "/page/{accountId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Page<Role>> pageByAccountId(Long pageNum, Long pageSize, @PathVariable("accountId") String accountId, String id, String name, UsingState state, Boolean isAdmin){
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Page<Role> page = roleService.page(pageRequest, id, name, state, isAdmin, accountId);

        return new Responsed<Page<Role>>("查询成功", page);
    }
}
