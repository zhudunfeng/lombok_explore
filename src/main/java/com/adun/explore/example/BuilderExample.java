package com.adun.explore.example;

import lombok.Builder;
import lombok.Singular;
import lombok.ToString;

import java.util.Set;

/**
 * @author ADun
 * @date 2022/11/26 18:11
 */
@Builder
@ToString
public class BuilderExample {

    @Builder.Default private long id=3;
    private String name;
    private int age;
    @Singular private Set<String> friends;


    public static void main(String[] args) {
        BuilderExample example = BuilderExample.builder()
                .age(18)
                .friend("tom")
                .friend("jack")
                .build();
        System.out.println(example);
    }

}
