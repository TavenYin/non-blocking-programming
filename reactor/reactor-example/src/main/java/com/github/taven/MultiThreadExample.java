package com.github.taven;

import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

public class MultiThreadExample {

    public static void main(String[] args) {
        subscribeOn();
//        publishOn();
    }

    private static void subscribeOn() {
        System.out.println("-------------------subscribeOn--------------------------");

        Mono.create(monoSink -> {
            System.out.println("create " + Thread.currentThread().getName());
            monoSink.success("a");
        })
                .map(a -> {
                    System.out.println("map " + Thread.currentThread().getName());
                    return a;
                })
                .subscribeOn(Schedulers.fromExecutor(Executors.newSingleThreadExecutor()))
                .subscribeOn(Schedulers.fromExecutor(Executors.newSingleThreadExecutor()))
                .subscribe(new CoreSubscriber<>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                        System.out.println("onSubscribe " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Object s) {
                        System.out.println("onNext " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete " + Thread.currentThread().getName());
                    }
                });
    }

    private static void publishOn() {
        System.out.println("-------------------publishOn--------------------------");

        Mono.create(monoSink -> {
            System.out.println("create " + Thread.currentThread().getName());
            monoSink.success("a");
        })
                .map(a -> {
                    System.out.println("map " + Thread.currentThread().getName());
                    return a;
                })
                .publishOn(Schedulers.fromExecutor(Executors.newSingleThreadExecutor()))
                .subscribe(new CoreSubscriber<>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                        System.out.println("onSubscribe " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Object s) {
                        System.out.println("onNext " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete " + Thread.currentThread().getName());
                    }
                });
    }

}
