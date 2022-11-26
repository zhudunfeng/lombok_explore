package com.adun.explore.example;

import lombok.SneakyThrows;

import java.io.UnsupportedEncodingException;

/**
 * @author ADun
 * @date 2022/11/26 18:32
 */
public class SneakyThrowsExample {

//    @SneakyThrows(UnsupportedEncodingException.class)
    public String utf8ToString(byte[] bytes){
        try {
            return new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
