package cn.kimmking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

import static cn.kimmking.utils.SpringUtils.scanPackages;

/**
 * Test SpringUtils.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/26 17:02
 */


@SpringJUnitConfig(classes = {TestConfiguration.class})
public class SpringUtilsTest {

    @Autowired
    TestConfiguration.A a;

    @Test
    void test1() {
        System.out.println("test1 ...");
        Assert.notNull(a, "Autowired must be an instance.");
        System.out.println(a.getA());
    }

    @Test
    void test2() {
        String packages = "io.github.kimmking.";

        System.out.println(" 1. *********** ");
        System.out.println(" => scan all classes for packages: " + packages);
        List<Class<?>> classes = scanPackages(packages.split(","), p -> true);
        classes.forEach(System.out::println);
        Assert.notEmpty(classes, "classes in the package must not be empty.");

        System.out.println();
        System.out.println(" 2. *********** ");
        System.out.println(" => scan all classes with @Configuration for packages: " + packages);
        List<Class<?>> classesWithConfig = scanPackages(packages.split(","),
                p -> Arrays.stream(p.getAnnotations())
                        .anyMatch(a -> a.annotationType().equals(Configuration.class)));
        classesWithConfig.forEach(System.out::println);
        Assert.notEmpty(classes, "classes with @Configuration annotation in the package must not be empty.");
    }

}
