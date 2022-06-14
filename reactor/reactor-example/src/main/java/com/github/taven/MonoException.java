package com.github.taven;

import reactor.core.publisher.Mono;

/**
 * 演示 Hooks.GLOBAL_TRACE 的效果
 *
 * 添加参数 -Dreactor.trace.operatorStacktrace=false
 */
public class MonoException {

    public static void main(String[] args) {
        Mono.defer(() -> {
            throw new RuntimeException("error");
        }).subscribe(System.out::println);
    }

}
