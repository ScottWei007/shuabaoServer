
package com.shuabao.socketServer.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 利用对象继承的内存布局规则来padding避免false sharing, 注意其中对象头会至少占用8个字节
 * ---------------------------------------
 * For 32 bit JVM:
 * _mark   : 4 byte constant
 * _klass  : 4 byte pointer to class
 * For 64 bit JVM:
 * _mark   : 8 byte constant
 * _klass  : 8 byte pointer to class
 * For 64 bit JVM with compressed-oops:
 * _mark   : 8 byte constant
 * _klass  : 4 byte pointer to class
 * ---------------------------------------
 */
class LhsTimePadding {
    @SuppressWarnings("unused")
    protected long p01, p02, p03, p04, p05, p06, p07;
}

class Time extends LhsTimePadding {
    protected volatile long now;
}

class RhsTimePadding extends Time {
    @SuppressWarnings("unused")
    protected long p09, p10, p11, p12, p13, p14, p15;
}

/**
 * {@link SystemClock} is a optimized substitute of {@link System#currentTimeMillis()} for avoiding context switch overload.
 * <p>
 * Every instance would start a thread to update the time, so it's supposed to be singleton in application context.
 * <p>
 * Forked from <A>https://github.com/zhongl/jtoolkit/blob/master/common/src/main/java/com/github/zhongl/jtoolkit/SystemClock.java</A>
 */
public class SystemClock extends RhsTimePadding {

    private static final long NOW_VALUE_OFFSET = UnsafeUtil.objectFieldOffset(Time.class, "now");

    private static final SystemClock millisClock = new SystemClock(1);

    private final long precision;

    public static SystemClock millisClock() {
        return millisClock;
    }

    private SystemClock(long precision) {
        now = System.currentTimeMillis();
        this.precision = precision;
        scheduleClockUpdating();
    }

    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable runnable) {
                Thread t = new Thread(runnable, "system.clock");
                t.setDaemon(true);
                return t;
            }
        });

        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                // Update the timestamp with ordered semantics.
                UnsafeUtil.getUnsafe().putOrderedLong(SystemClock.this, NOW_VALUE_OFFSET, System.currentTimeMillis());
            }
        }, precision, precision, TimeUnit.MILLISECONDS);
    }

    public long now() {
        return now;
    }

    public long precision() {
        return precision;
    }
}
