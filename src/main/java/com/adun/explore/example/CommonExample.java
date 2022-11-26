package com.adun.explore.example;

import lombok.*;

/**
 * @author ADun
 * @date 2022/11/26 15:27
 */
@ToString
@Getter(value = AccessLevel.PRIVATE)
@Setter
@EqualsAndHashCode
public class CommonExample {
    @ToString.Exclude private Integer id;
    private String name;
    private Integer age;
    @EqualsAndHashCode.Exclude private String grade;
}
