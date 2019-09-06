

package com.shuabao.socketServer.tcpSocket.processor;

import com.codahale.metrics.*;
import com.shuabao.socketServer.util.ClassUtil;


import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * 指标度量
 *
 * jupiter
 * org.jupiter.rpc.metric

 */
public class Metrics {

    private static final MetricRegistry metricRegistry = new MetricRegistry();
    private static final ScheduledReporter scheduledReporter;
    static {
        // 检查是否存在slf4j, 使用Metrics必须显式引入slf4j依赖
        ClassUtil.checkClass("org.slf4j.Logger",
                "Class[" + Metric.class.getName() + "] must rely on SL4J");

        ScheduledReporter _reporter;
        try {
            _reporter = Slf4jReporter
                    .forRegistry(metricRegistry)
                    .withLoggingLevel(Slf4jReporter.LoggingLevel.WARN)
                    .build();
        } catch (NoClassDefFoundError e) {
            // No Slf4j
            _reporter = ConsoleReporter.forRegistry(metricRegistry).build();
        }
        scheduledReporter = _reporter;
        scheduledReporter.start(15, TimeUnit.MINUTES);
    }

    /**
     * Return the global registry of metric instances.
     */
    public static MetricRegistry metricRegistry() {
        return metricRegistry;
    }

    /**
     * Return the {@link Meter} registered under this name; or create and register
     * a new {@link Meter} if none is registered.
     */
    public static Meter meter(String name) {
        return metricRegistry.meter(Objects.requireNonNull(name, "name"));
    }

    /**
     * Return the {@link Meter} registered under this name; or create and register
     * a new {@link Meter} if none is registered.
     */
    public static Meter meter(Class<?> clazz, String... names) {
        return metricRegistry.meter(MetricRegistry.name(clazz, names));
    }

    /**
     * Return the {@link Timer} registered under this name; or create and register
     * a new {@link Timer} if none is registered.
     */
    public static Timer timer(String name) {
        return metricRegistry.timer(Objects.requireNonNull(name, "name"));
    }

    /**
     * Return the {@link Timer} registered under this name; or create and register
     * a new {@link Timer} if none is registered.
     */
    public static Timer timer(Class<?> clazz, String... names) {
        return metricRegistry.timer(MetricRegistry.name(clazz, names));
    }

    /**
     * Return the {@link Counter} registered under this name; or create and register
     * a new {@link Counter} if none is registered.
     */
    public static Counter counter(String name) {
        return metricRegistry.counter(Objects.requireNonNull(name, "name"));
    }

    /**
     * Return the {@link Counter} registered under this name; or create and register
     * a new {@link Counter} if none is registered.
     */
    public static Counter counter(Class<?> clazz, String... names) {
        return metricRegistry.counter(MetricRegistry.name(clazz, names));
    }

    /**
     * Return the {@link Histogram} registered under this name; or create and register
     * a new {@link Histogram} if none is registered.
     */
    public static Histogram histogram(String name) {
        return metricRegistry.histogram(Objects.requireNonNull(name, "name"));
    }

    /**
     * Return the {@link Histogram} registered under this name; or create and register
     * a new {@link Histogram} if none is registered.
     */
    public static Histogram histogram(Class<?> clazz, String... names) {
        return metricRegistry.histogram(MetricRegistry.name(clazz, names));
    }

    private Metrics() {}
}
