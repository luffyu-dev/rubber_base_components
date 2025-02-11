package com.rubber.basic.web.starter.filter.login;

import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

public class CustomResponseWrapper extends ContentCachingResponseWrapper {
    public CustomResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public String getCapturedResponse() throws IOException {
        flushBuffer(); // 确保所有数据都写入缓存
        return new String(getContentAsByteArray(), getCharacterEncoding()); // 读取缓存数据
    }

    public void writeModifiedResponse(String modifiedContent) throws IOException {
        getResponse().getOutputStream().write(modifiedContent.getBytes(getCharacterEncoding()));
        getResponse().flushBuffer();
    }
}