package com.github.taven;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;
import reactor.core.publisher.UnicastProcessor;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * @author tianwen.yin
 */
public class MonoEx {

    public static void main(String[] args) {
//        Mono.defer(() -> {
//            System.out.println("defer");
//            return Mono.fromRunnable(MonoEx::run);
//        }).subscribe(new BaseSubscriber<Object>() {
//            @Override
//            protected void hookOnSubscribe(Subscription subscription) {
//                super.hookOnSubscribe(subscription);
//                System.out.println("调用限流");
//            }
//
//            @Override
//            protected void hookOnComplete() {
//                super.hookOnComplete();
//                System.out.println("限流结束");
//            }
//
//            @Override
//            protected void hookOnError(Throwable throwable) {
//                super.hookOnError(throwable);
//            }
//        });

        Mono.just("a")
                .map(str -> str.getBytes(StandardCharsets.UTF_8))
                .filter(bytes -> bytes.length == 1)
                .subscribe(System.out::println);
    }

    public static void run() {
        System.out.println("run");
    }

}
