package com.wan.natives;

/**
 * Author wan
 * Created on 2017/6/16 0016.
 * 用抽象类为了防止JsonUtil无法处理Lambda表达式
 * @param <V> 返回的泛型
 */

public abstract class Result<V> {
    public abstract void back(V back);
}
