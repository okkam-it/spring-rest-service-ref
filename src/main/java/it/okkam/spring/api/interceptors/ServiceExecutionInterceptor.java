package it.okkam.spring.api.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@ControllerAdvice
public class ServiceExecutionInterceptor extends HandlerInterceptorAdapter {

  @Value("${headers.request.start}")
  private String headerStartTime;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    request.setAttribute(headerStartTime, System.currentTimeMillis());
    return true;
  }

}
