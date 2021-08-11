package com.github.taven;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;

import java.util.function.Function;

public class MonoOperatorExample {
    public static void main(String[] args) {
        Mono.defer(() -> {
            System.out.println("defer1");
            return Mono.defer(() -> {
                System.out.println("defer just A");
                return Mono.just("a");
            })
            // MonoIgnoreThen#subscribe 会先调用 CoreSubscriber 的 onSubscribe
            // 然后再调用 Publisher 的 subscribe
            // 所以 加上 then 之后，看到的结果是先执行 onSubscribe，在执行 defer
            .then(Mono.defer(() -> {
                System.out.println("defer just B");
                return Mono.just("b");
            }))
            .then(Mono.defer(() -> {
                System.out.println("defer just C");
                return Mono.just("c");
            }));
        })
            .transform(new Function<Mono<String>, Publisher<String>>() {
                @Override
                public Publisher<String> apply(Mono<String> publisher) {
                    System.out.println("transform");
                    return new MonoOperator<String, String>(publisher) {
                        @Override
                        public void subscribe(CoreSubscriber actual) {
                            publisher.subscribe(new CoreSubscriber<String>() {
                                @Override
                                public void onSubscribe(Subscription s) {
                                    System.out.println("onSubscribe");
                                    actual.onSubscribe(s);
                                }

                                @Override
                                public void onNext(String s) {
                                    System.out.println("onNext");
                                    actual.onNext(s);
                                }

                                @Override
                                public void onError(Throwable t) {
                                    System.out.println("onError");
                                    actual.onError(t);
                                }

                                @Override
                                public void onComplete() {
                                    System.out.println("onComplete");
                                    actual.onComplete();
                                }
                            });
                        }
                    };
                }
            })
            .subscribe((x)-> System.out.println("actual subscribe: " + x))
        ;
    }
}
