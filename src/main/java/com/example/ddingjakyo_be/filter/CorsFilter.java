package com.example.ddingjakyo_be.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;

public class CorsFilter implements Filter {

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException, IOException {
    HttpServletResponse response = (HttpServletResponse) res;
    HttpServletRequest request = (HttpServletRequest) req;

    response.setHeader("Access-Control-Allow-Credentials","true");
    response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
    response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS, GET, DELETE, PUT");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "Authorization, x-requested-with, origin, content-type, accept");

    ServletServerHttpRequest ServerRequest = new ServletServerHttpRequest(request);
    ServletServerHttpResponse ServerResponse = new ServletServerHttpResponse(response);

    if(ServerRequest.getMethod().equals(HttpMethod.OPTIONS)){
      ServerResponse.getServletResponse().setStatus(HttpStatus.OK.value());
      return;
    }

    chain.doFilter(req, res);
  }
}
