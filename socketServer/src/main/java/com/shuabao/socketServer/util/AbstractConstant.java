package com.shuabao.socketServer.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public abstract class AbstractConstant<T extends AbstractConstant<T>> implements Constant<T>{
    private static final AtomicLong uniqueIdGenerator = new AtomicLong();
    private final int id;
    private final String name;
    private final long uniqueId;

    public AbstractConstant(int id, String name) {
        this.id = id;
        this.name = name;
        this.uniqueId = uniqueIdGenerator.getAndIncrement();
    }

    @Override
    public final int id() {
        return id;
    }

    @Override
    public final String name() {
        return name;
    }

    public final long getUniqueId() {
        return uniqueId;
    }

    @Override
    public final int compareTo(T o) {
        if(this == o) {
            return 0;
        }
        int code = hashCode() - o.hashCode();
        if(code != 0) {
            return code;
        }
        if(getUniqueId() < o.getUniqueId()) {
            return -1;
        }
        if(getUniqueId() > o.getUniqueId()) {
            return 1;
        }
        throw new Error("Failed to compare two different constants");
    }

    @Override
    public String toString() {
        return name();
    }
}
