package com.shuabao.core.config;

/**
 * Created by Scott Wei on 4/7/2018.
 */
public interface Configurer<T> {
    T configure(T t);
}
