package com.adun.explore.example;

import lombok.Data;
import lombok.NonNull;

/**
 * @author ADun
 * @date 2022/11/26 17:07
 */
@Data
public class DataExample {
    @NonNull private final String name;
    private int age;
    private double score;
}
