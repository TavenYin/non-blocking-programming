package com.github.taven;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;

import java.util.function.Function;

public class MonoTransform {

    public static void main(String[] args) {
        // 定义一个 Function，用于将 Mono<String> 转换为 Publisher<String>
        Function<Mono<String>, Publisher<String>> transform = new Function<>() {
            @Override
            public Publisher<String> apply(Mono<String> source) {
                return new CustomMono(source);
            }
        };

        Mono.just("Hello World")
                .transform(transform)
                .subscribe(new CoreSubscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("RealSubscriber onSubscribe");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("RealSubscriber onNext, value: " + s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("RealSubscriber onError");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("RealSubscriber onComplete");
                    }
                });
    }

    public static class CustomMono<T> extends MonoOperator<T, T> {
        public CustomMono(Mono<? extends T> source) {
            super(source);
        }

        @Override
        public void subscribe(CoreSubscriber<? super T> actual) {
            source.subscribe(new LoggerSubscriber<>(actual));
        }
    }

    public static class LoggerSubscriber<T> implements CoreSubscriber<T> {
        private Subscriber<T> actual;

        public LoggerSubscriber(Subscriber actual) {
            this.actual = actual;
        }

        @Override
        public void onSubscribe(Subscription s) {
            System.out.println("LoggerSubscriber onSubscribe");
            actual.onSubscribe(s);
        }

        @Override
        public void onNext(T t) {
            System.out.println("LoggerSubscriber onNext, value: " + t);
            actual.onNext(t);
        }

        @Override
        public void onError(Throwable t) {
            System.out.println("LoggerSubscriber onError");
            actual.onError(t);
        }

        @Override
        public void onComplete() {
            System.out.println("LoggerSubscriber onComplete");
            actual.onComplete();
        }
    }

}
