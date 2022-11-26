package com.adun.explore.example;

import lombok.val;
import lombok.var;

/**
 * @author ADun
 * @date 2022/11/26 13:07
 */
public class ValAndVarExample {

    public void valExample(){
        //相当于final String example = "Hello world";
        val example = "hello world";
        //Cannot assign a value to final variable
//        example = "hello china";
        System.out.println(example);
    }

    public void varExample(){
        var example = "hello world";
        example = "hello china";
        System.out.println(example);
    }

    public static void main(String[] args) {
        ValAndVarExample valAndVarExample = new ValAndVarExample();
        valAndVarExample.valExample();
        valAndVarExample.varExample();
    }



}
