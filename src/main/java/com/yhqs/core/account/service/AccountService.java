package com.yhqs.core.account.service;

import org.wah.doraemon.entity.User;
import org.wah.doraemon.entity.consts.Sex;

public interface AccountService{

    User register(String username, String password, Boolean isInternal, String name, String nickname, String headImgUrl, Sex sex) throws Exception;

    String login(String username, String password) throws Exception;

    void logout(String ticket) throws Exception;
}
