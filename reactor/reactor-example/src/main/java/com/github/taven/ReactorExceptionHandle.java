package com.github.taven;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class ReactorExceptionHandle {

    public static void main(String[] args) {
        // https://cloud.tencent.com/developer/article/1748447
        Flux<String> flux= Flux.just(1, 2, 0)
                .map(i -> "100 / " + i + " = " + (100 / i))
                .onErrorResume(e -> Mono.just(e.getMessage()));
        flux.subscribe(System.out::println);
    }

}
