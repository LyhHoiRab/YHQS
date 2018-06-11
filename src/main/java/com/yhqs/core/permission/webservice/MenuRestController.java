package com.yhqs.core.permission.webservice;

import com.yhqs.core.permission.entity.Menu;
import com.yhqs.core.permission.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.wah.doraemon.security.request.Page;
import org.wah.doraemon.security.request.PageRequest;
import org.wah.doraemon.security.response.Responsed;
import org.wah.ferryman.security.consts.HttpHeaderName;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/1.0/menu")
public class MenuRestController{

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Menu> save(@RequestBody Menu menu){
        menuService.save(menu);

        return new Responsed<Menu>("保存成功", menu);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Menu> update(@RequestBody Menu menu){
        menuService.update(menu);

        return new Responsed<Menu>("更新成功", menu);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Menu> getById(@PathVariable("id") String id){
        Menu menu = menuService.getById(id);

        return new Responsed<Menu>("查询成功", menu);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<Page<Menu>> page(Long pageNum, Long pageSize, String id, String name, String url, String parentId, Boolean isParent){
        PageRequest pageRequest = new PageRequest(pageNum, pageSize);
        Page<Menu> page = menuService.page(pageRequest, id, name, url, parentId, isParent);

        return new Responsed<Page<Menu>>("查询成功", page);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<List<Menu>> getByTicket(HttpServletRequest request){
        String ticket = request.getHeader(HttpHeaderName.AUTHORIZATION);
        List<Menu> list = menuService.getByTicket(ticket);

        return new Responsed<List<Menu>>("查询成功", list);
    }
}
