package com.example.demo.test;/**
 * @ClassName Test.java
 * @author wuting
 * @Description TODO
 * @createTime 2024年01月09日 11:38:00
 */

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

import java.time.LocalDateTime;

/**
 * @author wuting
 * @date 2024/01/09
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        for (int i = 0; i < 100; i++) {
            System.out.println(snowflake.nextId());
        }
    }



}
