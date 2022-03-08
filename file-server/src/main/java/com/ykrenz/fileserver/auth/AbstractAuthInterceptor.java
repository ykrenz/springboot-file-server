package com.ykrenz.fileserver.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mr Ren
 * @Description: 认证拦截器抽象类
 */
@Slf4j
public abstract class AbstractAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthUser authUser = getAuthUser(request);
        UserContext.setAuthUser(authUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.setAuthUser(null);
    }

    /**
     * 验证用户信息
     *
     * @param request
     * @return
     */
    protected abstract AuthUser getAuthUser(HttpServletRequest request);

}
