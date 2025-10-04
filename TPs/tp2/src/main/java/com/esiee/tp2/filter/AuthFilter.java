package com.esiee.tp2.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebFilter ("/*")
public class AuthFilter implements Filter{

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/login",
            "/logout",
            "/css"
    );


    @Override
    public void init(FilterConfig filterConfig) throws ServletException{
        System.out.println("Auth init");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Object user = req.getSession().getAttribute("user");
        String uri = req.getRequestURI();

        if (user == null && isPageInternal(uri)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unautorized access");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isPageInternal(String uri){
        return PUBLIC_PATHS.stream().noneMatch(uri::contains);
    }


}
