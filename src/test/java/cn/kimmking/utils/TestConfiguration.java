package cn.kimmking.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring test configuration.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/26 17:22
 */

@Configuration
public class TestConfiguration {

    @Bean
    A aa() {
        return new A("KK is good");
    }


    @Data
    @AllArgsConstructor
    public static class A {
        private String a;
    }

}


