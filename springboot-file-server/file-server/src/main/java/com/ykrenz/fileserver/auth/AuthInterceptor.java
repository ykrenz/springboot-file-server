package com.ykrenz.fileserver.auth;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mr Ren
 * @Description: 认证拦截器
 */
@Component
public class AuthInterceptor extends AbstractAuthInterceptor {

    @Override
    protected AuthUser getAuthUser(HttpServletRequest request) {
        //TODO 验证用户信息
        AuthUser authUser = new AuthUser();
        authUser.setUserId("1");
        return authUser;
    }
}
