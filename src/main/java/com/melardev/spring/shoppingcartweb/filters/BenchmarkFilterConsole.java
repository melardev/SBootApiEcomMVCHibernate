package com.melardev.spring.shoppingcartweb.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class BenchmarkFilterConsole extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        long elapsed = (System.currentTimeMillis() - startTime);
        System.out.printf("[+] Performance info %s: %d milliseconds\n", httpServletRequest.getRequestURI(), elapsed);
    }
}
