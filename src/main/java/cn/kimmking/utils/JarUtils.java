package cn.kimmking.utils;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * find jar info from jar file or class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/23 下午4:31
 */
public interface JarUtils {

    static URL getJarURL(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }

    @SneakyThrows
    static File getJarFile(Class<?> clazz) {
        return new File(getJarURL(clazz).toURI());
    }

    static JarInfo getJarInfo(Class<?> clazz) {
        String jarName = getJarFile(clazz).getName();
        int index = jarName.lastIndexOf("-");
        return new JarInfo(jarName.substring(0, index),
                jarName.substring(index + 1, jarName.lastIndexOf(".")));
    }

    @SneakyThrows
    static String getJarVersion(String groupId, String artifactId) {
        String version = null;
        try {
            Properties properties = new Properties();
            InputStream stream = JarUtils.class.getResourceAsStream("/META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");
            if (stream != null) {
                properties.load(stream);
            }
            version = properties.getProperty("version");
        } catch (Exception ex) {
            // ignore ex
        }
        return version;
    }

    static void main(String[] args) {
        System.out.println(getJarInfo(JSON.class));
        System.out.println(getJarVersion("com.alibaba", "fastjson"));
    }

    @Data
    @AllArgsConstructor
    class JarInfo {
        private String name;
        private String version;
    }

}
