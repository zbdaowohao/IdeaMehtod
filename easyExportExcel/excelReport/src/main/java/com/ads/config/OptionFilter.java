package com.ads.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class OptionFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		/* 跨域设置 */
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Methods", "*");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE");
		httpResponse.setHeader("Access-Control-Expose-Headers", "*");
		if (httpRequest.getMethod().equals("OPTIONS")) {
			httpResponse.setStatus(200);
			httpResponse.getWriter().write("OPTIONS returns OK");
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

	private static boolean isValid(Object obj) throws UnsupportedEncodingException {
		return true;
	}

	private static void resp(HttpServletResponse response, String[] msg) {
	}

}
