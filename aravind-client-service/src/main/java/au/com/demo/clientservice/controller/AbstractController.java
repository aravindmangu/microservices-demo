package au.com.demo.clientservice.controller;

import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbstractController {
    @Resource
    private NativeWebRequest webRequest;

    @Autowired
    private ObjectMapper objectMapper;

    protected <R> HttpStatus writeToResponse(R result) {
        final HttpServletResponse res = webRequest.getNativeResponse(HttpServletResponse.class);
        try {
            if(res == null){
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
            res.setCharacterEncoding(StandardCharsets.UTF_8.name());
            res.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            final String body = objectMapper.writeValueAsString(result);
            log.info("response-body", body);
            res.getWriter().write(body);
            return HttpStatus.OK;
        } catch (Exception e) {
            log.error(e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
