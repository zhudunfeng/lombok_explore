package com.adun.explore.example;

import lombok.NonNull;

/**
 * @author ADun
 * @date 2022/11/26 15:02
 */
public class NoNullExample {

    /**
     * 为空回抛出java.lang.NullPointerException: param is marked non-null but is null
     * @param param
     */
    public void example(@NonNull String param){
        System.out.println(param);
    }

    public static void main(String[] args) {
        NoNullExample noNullExample = new NoNullExample();
        noNullExample.example(null);
    }
}
