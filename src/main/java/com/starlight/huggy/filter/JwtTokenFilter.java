package com.starlight.huggy.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtTokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("필터1");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //TODO : id, pw 들어와서 로그인이 완료되면, 토큰을 만들어주고, 그걸 응답을 해줘야 함
        // 요청할 때마다 header의 Authorization에 value 값으로 토큰을 가져올 것이고,
        // 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증 하면 됨
        if(req.getMethod().equals("POST")){
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            if(headerAuth.equals("tokenA")){
                chain.doFilter(request,response);
            }else{
                PrintWriter outPrintWriter = response.getWriter();
                outPrintWriter.println("인증 안됨");

            }
        }


    }
}
