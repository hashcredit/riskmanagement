package com.newdumai.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 检查是否登录过滤器
 * 
 * @author yuexiao
 * 
 */
public class LoginFilter implements Filter {
	
	@Override
	public void destroy() {
		// Do nothing
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		path = path.replaceAll("[/\\\\]+", "/");// 格式化路径:/和\以及连续的/和\替换为一个/
		
		//登录了或者是/tologin.do、/logout.do、/login.do请求，通过
		if (request.getSession().getAttribute("login") != null || path.equals("/tologin.do") || path.equals("/logout.do")|| path.equals("/verification.do") || path.equals("/login.do")) {
			chain.doFilter(req, resp);
		}
		else {
			response.sendRedirect(request.getContextPath());
		}
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		// Do nothing
	}
	
}
