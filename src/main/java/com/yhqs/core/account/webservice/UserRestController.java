package com.yhqs.core.account.webservice;

import com.yhqs.core.account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wah.doraemon.entity.User;
import org.wah.doraemon.security.response.Responsed;
import org.wah.ferryman.security.consts.HttpHeaderName;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/1.0/user")
public class UserRestController{

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<User> getByTicket(HttpServletRequest request) throws Exception{
        String ticket = request.getHeader(HttpHeaderName.AUTHORIZATION);

        //查询用户信息
        User user = userService.getByTicket(ticket);

        return new Responsed<User>("查询成功", user);
    }
}
