package com.jogeen.barrage.web.filter;


import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@WebFilter(urlPatterns = "/barrage/*",filterName = "sessionFilter")
public class UserSessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Object user = ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        if(user==null){
            HttpServletResponse response=((HttpServletResponse)servletResponse);
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write("failed");
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

}
