package com.jogeen.barrage.web.filter;


import com.google.gson.Gson;
import com.jogeen.barrage.web.model.User;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@WebFilter(urlPatterns = "/admin/*", filterName = "adminSessionFilter")
public class AdminSessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Object user = ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        if (user == null) {
            HttpServletResponse response = ((HttpServletResponse) servletResponse);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write("未登录");

        } else {
            User user1 = new Gson().fromJson((String) user, User.class);
            if ("admin".equals(user1.getType())) {
                filterChain.doFilter(servletRequest, servletResponse);
            }else{
                HttpServletResponse response = ((HttpServletResponse) servletResponse);
                response.setStatus(HttpStatus.OK.value());
                response.getWriter().write("权限不足");
            }
        }
    }

}
