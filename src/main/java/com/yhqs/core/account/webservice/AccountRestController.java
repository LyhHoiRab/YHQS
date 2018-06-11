package com.yhqs.core.account.webservice;

import com.yhqs.core.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wah.doraemon.entity.User;
import org.wah.doraemon.entity.consts.Sex;
import org.wah.doraemon.security.response.Responsed;
import org.wah.ferryman.security.consts.HttpHeaderName;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/1.0/account")
public class AccountRestController{

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<User> register(String username, String password, Boolean isInternal, String name, String nickname, String headImgUrl, Sex sex) throws Exception{
        User user = accountService.register(username, password, isInternal, name, nickname, headImgUrl, sex);

        return new Responsed<User>("注册成功", user);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed<String> login(String username, String password) throws Exception{
        String ticket = accountService.login(username, password);

        return new Responsed<String>("登录成功", ticket);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Responsed logout(HttpServletRequest request) throws Exception{
        String ticket = request.getHeader(HttpHeaderName.AUTHORIZATION);

        accountService.logout(ticket);

        return new Responsed("登出成功");
    }
}
