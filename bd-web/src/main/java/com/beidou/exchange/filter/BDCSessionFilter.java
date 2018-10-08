package com.beidou.exchange.filter;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterChain;
import java.io.IOException;

/**
 * Created by fengguoqing on 2018/7/2.
 */
public class BDCSessionFilter implements Filter {

    private static Logger logger = Logger.getLogger(BDCSessionFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
/*
        try {

            HttpServletRequest hreq = (HttpServletRequest) request;


            HttpServletResponse hresp = (HttpServletResponse) response;

            //跨域
            hresp.setHeader("Access-Control-Allow-Origin", "*");


            //跨域 Header

            hresp.setHeader("Access-Control-Allow-Methods", "*");

            hresp.setHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE");


            // 浏览器是会先发一次options请求，如果请求通过，则继续发送正式的post请求

            // 配置options的请求返回

            if (hreq.getMethod().equals("OPTIONS")) {

                hresp.setStatus(HttpStatus.OK.value());

                // hresp.setContentLength(0);

                hresp.getWriter().write("OPTIONS returns OK");

                return;

            }

            // Filter 只是链式处理，请求依然转发到目的地址。

            chain.doFilter(request, response);

        } catch (Exception e) {

            e.printStackTrace();

        }*/
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
