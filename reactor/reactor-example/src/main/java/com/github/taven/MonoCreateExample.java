package com.github.taven;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;

public class MonoCreateExample {

    public static void main(String[] args) {
        Mono.create(monoSink -> {
            System.out.println("create");
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("monoSink success");
                monoSink.success("a");
            }).start();
        }).doOnError(e -> {

        }).subscribe(new CoreSubscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(Object s) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("onNext");
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });

    }

}
