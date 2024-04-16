package cn.kimmking.utils;

/**
 * tooling for runtime.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/4/10 16:56
 */
public interface RuntimeUtils {

    static long getPid() {
        return java.lang.management.ManagementFactory.getRuntimeMXBean().getPid();
    }

    static String getCommandLine() {
        return java.lang.management.ManagementFactory.getRuntimeMXBean()
                .getInputArguments().stream().reduce((a, b) -> a + " " + b).orElse("null");
    }

    static void main(String[] args) {
        System.out.println("====> PID = " + getPid());
        System.out.println("====> CommandLine = " + getCommandLine());
        System.out.println("====> JAVA_HOME = " + System.getProperty("java.home"));
        System.out.println("====> JAVA_VERSION = " + System.getProperty("java.version"));
        System.out.println("====> AAA = " + System.getProperty("AAA"));
    }

}
