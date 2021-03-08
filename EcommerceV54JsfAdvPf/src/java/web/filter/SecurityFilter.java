/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.filter;

import entity.CustomerEntity;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mitsuki
 */
@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter {

    FilterConfig filterConfig;

    private static final String CONTEXT_ROOT = "/EcommerceV54JsfAdvPf";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();

        if (httpSession.getAttribute("isLogin") == null) {
            httpSession.setAttribute("isLogin", false);
        }

        Boolean isLogin = (Boolean) httpSession.getAttribute("isLogin");

        if (!excludeLoginCheck(requestServletPath)) {
            if (isLogin == true) {
                CustomerEntity currentCustomer = (CustomerEntity) httpSession.getAttribute("currentCustomer");
                chain.doFilter(request, response);
            } else {
                httpServletResponse.sendRedirect(CONTEXT_ROOT + "/accessRightError.xhtml");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {

    }

    private boolean excludeLoginCheck(String requestServletPath) {
        if (requestServletPath.equals("/index.xhtml") || requestServletPath.equals("/accessRightError.xhtml") || requestServletPath.startsWith("/javax.faces.resource")) {
            return true;
        } else {
            return false;
        }
    }
}
