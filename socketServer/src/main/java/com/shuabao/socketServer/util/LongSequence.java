package com.shuabao.socketServer.util;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * Created by Scott Wei on 8/4/2018.
 *
 * 序号生成器, 每个线程预先申请一个区间, 步长(step)固定, 以此种方式尽量减少CAS操作,
 * 需要注意的是, 这个序号生成器不是严格自增的, 并且也溢出也是可以接受的(接受负数).

 */
public class LongSequence extends LongRhsPadding{

    private static final int DEFAULT_STEP = 128;
    private static final AtomicLongFieldUpdater<LongValue> updater = AtomicLongFieldUpdater.newUpdater(LongValue.class, "value");
    private final InternalThreadLocal<LocalSequence> localSequence = new InternalThreadLocal<LocalSequence>(){

        @Override
        protected LocalSequence initialValue() throws Exception {
            return new LocalSequence();
        }
    };

    private final int step;

    public LongSequence() {
        this(DEFAULT_STEP);
    }

    public LongSequence(int step) {
        this.step = step;
    }

    public long next() {
        return localSequence.get().next();
    }

    private long getNextBaseValue(){
        return updater.getAndAdd(this, step);
    }

    private final class LocalSequence {
        private long localBase = getNextBaseValue();
        private long localValue = 0;

        public long next() {
            long realVal = ++localValue + localBase;

            if (localValue == step) {
                localBase = getNextBaseValue();
                localValue = 0;
            }

            return realVal;
        }
    }

}

class LongLhsPadding {
    @SuppressWarnings("unused")
    protected long p01, p02, p03, p04, p05, p06, p07;
}

class LongValue extends LongLhsPadding {
    protected volatile long value;
}

class LongRhsPadding extends LongValue {
    @SuppressWarnings("unused")
    protected long p09, p10, p11, p12, p13, p14, p15;
}

