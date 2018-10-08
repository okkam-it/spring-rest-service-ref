package it.okkam.spring.api.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class GeneralControllerAdvice implements ResponseBodyAdvice<Object> {

  @Value("${build.version}")
  private String buildVersion;
  @Value("${headers.response.build.version}")
  private String headerBuildVersion;
  @Value("${headers.response.time}")
  private String headerResponseTime;
  @Value("${headers.request.start}")
  private String headerStartTime;

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {
    ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;
    if (servletServerRequest.getServletRequest() != null
        && servletServerRequest.getServletRequest().getAttribute(headerStartTime) != null) {
      final long startTime = (long) servletServerRequest.getServletRequest()
          .getAttribute(headerStartTime);
      final long timeElapsed = System.currentTimeMillis() - startTime;
      response.getHeaders().add(headerResponseTime, String.valueOf(timeElapsed));
      response.getHeaders().add(headerBuildVersion, buildVersion);
    }
    return body;
  }

}
