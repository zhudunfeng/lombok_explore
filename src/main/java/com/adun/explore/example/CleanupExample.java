package com.adun.explore.example;

import lombok.Cleanup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ADun
 * @date 2022/11/26 15:18
 */
public class CleanupExample {
    public static void main(String[] args) throws IOException {
        @Cleanup InputStream inputStream = new FileInputStream(args[0]);
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
    }
}
