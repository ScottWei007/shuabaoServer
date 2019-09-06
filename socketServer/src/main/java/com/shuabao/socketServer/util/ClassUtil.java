
package com.shuabao.socketServer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.shuabao.socketServer.util.StackTraceUtil.stackTrace;


public final class ClassUtil {
    private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 提前加载并初始化指定的类, 某些平台下某些类的静态块里面的代码执行实在是太慢了:(
     *
     * @param className         类的全限定名称
     * @param tolerableMillis   超过这个时间打印警告日志
     */
    public static void initializeClass(String className, long tolerableMillis) {
        long start = System.currentTimeMillis();
        try {
            Class.forName(className);
        } catch (Throwable t) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to load class [{}] {}.", className, stackTrace(t));
            }
        }

        long duration = System.currentTimeMillis() - start;
        if (duration > tolerableMillis) {
            logger.warn("{}.<clinit> duration: {} millis.", className, duration);
        }
    }

    public static void checkClass(String className, String message) {
        try {
            Class.forName(className);
        } catch (Throwable t) {
            throw new RuntimeException(message, t);
        }
    }

    private ClassUtil() {}
}
