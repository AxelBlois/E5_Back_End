package com.esiee.tp2.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.swing.event.HyperlinkEvent;
import java.io.IOException;

@WebFilter ("/*")
public class AuthFilter implements Filter{

    @Override
    public void init(FilterConfig )



    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                            FilterChain chain) throws IOException, SecurityException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Object user = req.getSession().getAttribute("user");

        if (user == null && !req.getRequestURI().contains("/login")) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unautorized access");
            return;
        }

        chain.doFilter(request, response);

    }


}
