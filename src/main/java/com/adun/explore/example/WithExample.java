package com.adun.explore.example;

import lombok.AllArgsConstructor;
import lombok.With;

/**
 * @author ADun
 * @date 2022/11/26 18:49
 */
@AllArgsConstructor
public class WithExample {
    private String name;
    @With private final int age;

    public static void main(String[] args) {
        WithExample example = new WithExample("adun", 18);
        System.out.println(example);
        WithExample withExample = example.withAge(19);
        System.out.println(withExample);
    }

}
