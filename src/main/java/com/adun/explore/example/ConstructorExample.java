package com.adun.explore.example;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author ADun
 * @date 2022/11/26 15:47
 */
//@NoArgsConstructor
//@AllArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class ConstructorExample {
    @NonNull private Integer x;
    @NonNull private Integer y;
    private String description;

    public static void main(String[] args) {
        //Exception in thread "main" java.lang.NullPointerException: x is marked non-null but is null
//        new ConstructorExample(null, null, "");
        ConstructorExample.of(1, 2);
    }
}
