package cn.kimmking.utils;

import lombok.SneakyThrows;
import org.springframework.objenesis.instantiator.util.UnsafeUtils;
import sun.misc.Unsafe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Field Utils.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/17 01:55
 */
public interface FieldUtils {

    static List<Field> findAnnotatedField(Class<?> aClass, Class<? extends Annotation> annotationClass) {
        return findField(aClass, f -> f.isAnnotationPresent(annotationClass));
    }

    static List<Field> findField(Class<?> aClass, Function<Field, Boolean> function) {
        List<Field> result = new ArrayList<>();
        while (aClass != null) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field f : fields) {
                if (function.apply(f)) {
                    result.add(f);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return result;
    }

    @SneakyThrows
    static void setFinalField(Object obj, String fieldName, Object value) {
        setFinalField(obj, obj.getClass().getField(fieldName), value);
    }

    static void setFinalField(Object obj, Field field, Object value) {
        Unsafe unsafe = UnsafeUtils.getUnsafe();
        long fieldOffset = unsafe.objectFieldOffset(field);
        unsafe.putObject(obj, fieldOffset, value);
    }

    @SneakyThrows
    static Object getField(Object obj, String fieldName) {
        return getField(obj, obj.getClass().getField(fieldName));
    }

    static Object getField(Object obj, Field field) {
        Unsafe unsafe = UnsafeUtils.getUnsafe();
        long fieldOffset = unsafe.objectFieldOffset(field);
        return unsafe.getObject(obj, fieldOffset);
    }

    @SneakyThrows
    static void main(String[] args) {
        A a = new A();
        String finalFieldName = "finalFieldName";
        setFinalField(a, finalFieldName, "kimmking");
        System.out.println(getField(a, finalFieldName));
        System.out.println(a.getClass().getField(finalFieldName).get(a));
        System.out.println(a.finalFieldName);
    }


    class A {
        public final String finalFieldName = "finalFieldName";
    }

}
