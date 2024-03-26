package io.github.kimmking.commons;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Method Utils.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/26 18:15
 */
public interface MethodUtils {

    static boolean checkLocalMethod(final String method) {
        //本地方法不代理
        if ("toString".equals(method) ||
                "hashCode".equals(method) ||
                "notifyAll".equals(method) ||
                "equals".equals(method) ||
                "wait".equals(method) ||
                "getClass".equals(method) ||
                "notify".equals(method)) {
            return true;
        }
        return false;
    }

    static boolean checkLocalMethod(final Method method) {
        return method.getDeclaringClass().equals(Object.class);
    }

    static String methodSign(Method method) {
        StringBuilder sb = new StringBuilder(method.getName());
        sb.append("@").append(method.getParameterCount());
        Arrays.stream(method.getParameterTypes()).forEach(
                c -> sb.append("_").append(c.getCanonicalName())
        );
        return sb.toString();
    }

    static List<Field> findAnnotatedField(Class<?> aClass, Class<? extends Annotation> annotationClass) {
        List<Field> result = new ArrayList<>();
        while (aClass != null) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field f : fields) {
                if (f.isAnnotationPresent(annotationClass)) {
                    result.add(f);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return result;
    }

    static void main(String[] args) {
        Arrays.stream(MethodUtils.class.getMethods()).forEach(
                m -> System.out.println(methodSign(m))
        );
    }

}
