package com.yhqs.commons.security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wah.doraemon.security.consts.ResponseCode;
import org.wah.doraemon.security.exception.*;
import org.wah.doraemon.security.response.Responsed;

@ControllerAdvice
@ResponseBody
public class ExceptionAdvice{

    private Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    /**
     * 其他异常
     */
    @ExceptionHandler(value = Exception.class)
    public Responsed exception(Exception e){
        logger.error(e.getMessage(), e);

        return new Responsed(e.getMessage(), ResponseCode.INTERNAL_SERVER_ERROR, false);
    }

    /**
     * 不支持的客户端类型
     */
    @ExceptionHandler(value = BrowserNotSupportException.class)
    public Responsed browserNotSupport(BrowserNotSupportException e){
        logger.error(e.getMessage(), e);

        return new Responsed(e.getMessage(), ResponseCode.BROWSER_NOT_SUPPORT, false);
    }

    /**
     * 持久层异常
     */
    @ExceptionHandler(value = DataAccessException.class)
    public Responsed dataAccess(DataAccessException e){
        logger.error(e.getMessage(), e);

        return new Responsed(e.getMessage(), ResponseCode.INTERNAL_SERVER_ERROR, false);
    }

    /**
     * 记录重复异常
     */
    @ExceptionHandler(value = DuplicateException.class)
    public Responsed duplicate(DuplicateException e){
        logger.error(e.getMessage(), e);

        return new Responsed(e.getMessage(), ResponseCode.DUPLICATE_DATA, false);
    }

    /**
     * 登录失败异常
     */
    @ExceptionHandler(value = LoginFailException.class)
    public Responsed loginFail(LoginFailException e){
        logger.error(e.getMessage(), e);

        return new Responsed(e.getMessage(), ResponseCode.LOGIN_FAIL, false);
    }

    /**
     * 账户认证异常
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public Responsed authenticate(AuthenticationException e){
        logger.error(e.getMessage(), e);

        return new Responsed(e.getMessage(), ResponseCode.AUTHENTICATE_FAIL, false);
    }

    /**
     * 票据凭证认证异常
     */
    @ExceptionHandler(value = TicketAuthenticationException.class)
    public Responsed ticketAuthentication(TicketAuthenticationException e){
        logger.error(e.getMessage(), e);

        return new Responsed(e.getMessage(), ResponseCode.TICKET_AUTHENTICATE_FAIL, false);
    }

    /**
     * 票据刷新异常
     */
    @ExceptionHandler(value = TicketRefreshFailException.class)
    public Responsed ticketRefreshFail(TicketRefreshFailException e){
        logger.error(e.getMessage(), e);

        return new Responsed(e.getMessage(), ResponseCode.TICKET_REFRESH_FAIL, false);
    }
}
