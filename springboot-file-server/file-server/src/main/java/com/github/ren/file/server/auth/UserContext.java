package com.github.ren.file.server.auth;

/**
 * @author Mr Ren
 * @Description: 用户上下文信息
 * @date 2021/2/22 9:25
 */
public class UserContext {
    private static final ThreadLocal<AuthUser> LOGIN_USER_INFO_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置登录用户信息
     *
     * @param authUser
     */
    public static void setAuthUser(AuthUser authUser) {
        if (authUser != null) {
            LOGIN_USER_INFO_THREAD_LOCAL.set(authUser);
        } else {
            LOGIN_USER_INFO_THREAD_LOCAL.remove();
        }
    }

    /**
     * 获取用户登录信息
     *
     * @return
     */
    public static AuthUser getAuthUser() {
        return LOGIN_USER_INFO_THREAD_LOCAL.get();
    }
}
