package com.adun.explore.example;

import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * @author ADun
 * @date 2022/11/26 17:45
 */
@Value
public class ValueExample {
    String name;
    @NonFinal public int age;
    public double score;
}
