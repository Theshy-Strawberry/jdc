/*
 * Copyright 2003 Jayson Falkner (jayson@jspinsider.com)
 * This code is from "Servlets and JavaServer pages; the J2EE Web Tier",
 * http://www.jspbook.com. You may freely use the code both commercially
 * and non-commercially. If you like the code, please pick up a copy of
 * the book and help support the authors, development of more free code,
 * and the JSP/Servlet/J2EE community.
 */
package com.senyoboss.filter.gzip;

import com.senyoboss.filter.HttpFilter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GZIPFilter extends HttpFilter {

  public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

    String ae = request.getHeader("accept-encoding");
    //check if browser support gzip
    if (ae != null && ae.indexOf("gzip") != -1) {
      GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(response);
      chain.doFilter(request, wrappedResponse);
      wrappedResponse.finishResponse();
      return;
    }
    chain.doFilter(request, response);

  }

  public void init(FilterConfig filterConfig) {
    // noop
  }

  public void destroy() {
    // noop
  }
}
