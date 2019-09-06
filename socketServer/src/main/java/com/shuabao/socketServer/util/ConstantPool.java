package com.shuabao.socketServer.util;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Scott Wei on 8/4/2018.
 */
public abstract class ConstantPool<T extends Constant<T>> {
    private final ConcurrentMap<String, T> constants = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public T valueOf (Class<?> clz, String name) {
        Objects.requireNonNull(clz);
        Objects.requireNonNull(name);
        return valueOf(clz.getName() + "#" + name);
    }

    public T valueOf (String name) {
        Objects.requireNonNull(name);
        T constant = constants.get(name);
        if(Objects.isNull(constant)) {
            final T newConstant = newConstant(nextId.getAndIncrement(), name);
            constant = constants.putIfAbsent(name, newConstant);
            if(Objects.isNull(constant)) {
                constant = newConstant;
            }
        }
        return constant;
    }

    protected abstract T newConstant(int id, String name);
}
