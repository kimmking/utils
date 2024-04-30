package cn.kimmking.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/5/1 00:05
 */
public interface ClassloaderUtils {

    String APP_EXT_DIRS = "app.ext.dirs";
    String CUSTOM_EXT_DIRS = "custom.ext.dirs";

    static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    static ClassLoader getClassLoader(Class<?> clazz) {
        return clazz.getClassLoader();
    }

    static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            cl = ClassloaderUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Exception exception) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    static ClassLoader loadCustomExtDirs() {
        String appExtDirs = System.getProperty(CUSTOM_EXT_DIRS);
        List<File> files = new ArrayList<>();
        if (appExtDirs != null) {
            String[] dirs = appExtDirs.split(";");
            for (String dir : dirs) {
                System.out.println("[ClassloaderUtils]load jar from custom ext dir:" + dir);
                File file = new File(dir);
                if(file.isDirectory()) {
                    files.addAll(Arrays.stream(Objects.requireNonNull
                            (file.listFiles((d, n) -> n.endsWith(".jar")))).toList());
                } else if(file.isFile() && file.getName().endsWith(".jar")) {
                    files.add(file);
                }
            }
            return loadJarFile(files.toArray(File[]::new));
        }
        return null;
    }

    static ClassLoader loadJarFile(File... jarFiles) {
        ClassLoader classLoader = getClassLoader();
        URL[] jars = Arrays.stream(jarFiles).map(File::getAbsolutePath).map(p -> {
            try {
                System.out.println("[ClassloaderUtils]load custom jar file:" + p);
                return new URL("file:" + p);
            } catch (MalformedURLException e) {
                // ignore
                return null;
            }
        }).filter(Objects::nonNull).toArray(URL[]::new);
        return new java.net.URLClassLoader("custom", jars, classLoader);
    }

    static void loadAppExtDirs() {
        String appExtDirs = System.getProperty(APP_EXT_DIRS);
        List<File> files = new ArrayList<>();
        if (appExtDirs != null) {
            String[] dirs = appExtDirs.split(";");
            for (String dir : dirs) {
                System.out.println("[ClassloaderUtils]load jar from app ext dir:" + dir);
                File file = new File(dir);
                if(file.isDirectory()) {
                    files.addAll(Arrays.stream(Objects.requireNonNull
                            (file.listFiles((d, n) -> n.endsWith(".jar")))).toList());
                } else if(file.isFile() && file.getName().endsWith(".jar")) {
                    files.add(file);
                }
            }
            loadAppJar(files.toArray(File[]::new));
        }
    }

    static void loadAppJar(File...jarFiles) {
        ClassLoader apploader = Thread.currentThread().getContextClassLoader();
        Method method = null;
        try {
            method = apploader.getClass().getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
        } catch (NoSuchMethodException e) {
            // ignore
            apploader = apploader.getParent();
            try {
                method = apploader.getClass().getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
            } catch (Exception ex) {
                System.out.println("[ClassloaderUtils]appendToClassPathForInstrumentation not found");
            }
        }
        try {
            if(method != null) {
                method.setAccessible(true);
                for (File jar : jarFiles) {
                    System.out.println("[ClassloaderUtils]load app jar file:" + jar.getAbsolutePath());
                    method.invoke(apploader, jar.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.out.println("[ClassloaderUtils]add jar file error:" + e.getMessage());
        }
    }

    static void main(String[] args) {

//        String jar = "/Users/kimmking/.m2/repository/com/alibaba/fastjson/1.2.83/fastjson-1.2.83.jar";
        String jar = "/Users/kimmking/tools/mysql-connector-java-5.1.47.jar";
        String className = "com.mysql.jdbc.Driver";
        String app_ext_dir = "/Users/kimmking/tools";
        try {

            System.setProperty(APP_EXT_DIRS, app_ext_dir);
            System.setProperty(CUSTOM_EXT_DIRS, app_ext_dir);

            loadAppExtDirs();
            Class<?> aClass = Class.forName(className);
            System.out.println(aClass.getCanonicalName());

            ClassLoader custom = loadCustomExtDirs();
            Class<?> aClass1 = custom.loadClass(className);
            System.out.println(aClass1.getCanonicalName());

            // test 1
//            ClassLoader classLoader = loadJarFile(jar);
//            Class<?> aClass = classLoader.loadClass("com.mysql.jdbc.Driver");
//            System.out.println(classLoader.getParent());
//            ClassLoader classLoader2 = loadJarFile(jar);
//            Class<?> aClass2 = classLoader2.loadClass("com.mysql.jdbc.Driver");
//            System.out.println(aClass2.equals(aClass));

            // test 2
//            ClassLoader apploader = Thread.currentThread().getContextClassLoader();
//            Method method = apploader.getClass().getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
//            method.setAccessible(true);
//            method.invoke(apploader, jar);
//            Class<?> aClass = Class.forName(className);
//            System.out.println(aClass.getCanonicalName());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
