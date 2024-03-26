package io.github.kimmking.commons;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Spring Utils.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/26 16:55
 */
public interface SpringUtils {

    String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    static List<Class<?>> scanPackages(String[] packages, Predicate<Class<?>> predicate){
        List<Class<?>> results = new ArrayList<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        for (String basePackage : packages) {
            if (StringUtils.isEmpty(basePackage)) {
                continue;
            }
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage)) + "/" + DEFAULT_RESOURCE_PATTERN;
            System.out.println("packageSearchPath="+packageSearchPath);
            try {
                Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    //System.out.println(" resource: " + resource.getFilename());
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    ClassMetadata classMetadata = metadataReader.getClassMetadata();
                    String className = classMetadata.getClassName();
                    Class<?> clazz = Class.forName(className);
                    if(predicate.test(clazz)) {
                        //System.out.println(" ===> class: " + className);
                        results.add(clazz);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }

}
