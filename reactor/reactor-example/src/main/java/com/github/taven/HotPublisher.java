package com.github.taven;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class HotPublisher {
    public static void main(String[] args) {
//        Sinks.Many<String> hotSource = Sinks.unsafe().many().multicast().directBestEffort();
//
//        Flux<String> hotFlux = hotSource.asFlux().map(String::toUpperCase);
//
//        hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: "+d));
//
//        hotSource.emitNext("blue", FAIL_FAST);
//        hotSource.tryEmitNext("green").orThrow();
//
//        hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: "+d));
//
//        hotSource.emitNext("orange", FAIL_FAST);
//        hotSource.emitNext("purple", FAIL_FAST);
//        hotSource.emitComplete(FAIL_FAST);

    Mono.just("hello")
            .map(a -> a + "world")
            .subscribe(System.out::println);
    }

    public static class MonoMap implements Publisher {
        // 我们自定义的转换逻辑
        private Function mapper;
        // source 代表当前操作符的上游发布者
        private Publisher source;

        public MonoMap(Publisher source, Function mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public void subscribe(Subscriber actual) {
            source.subscribe(new MonoMapSubscriber(mapper, actual));
        }
    }

    public static class MonoMapSubscriber implements Subscriber {
        // 我们自定义的转换逻辑
        private Function mapper;
        // 真正的下游
        private Subscriber actual;

        public MonoMapSubscriber(Function mapper, Subscriber actual) {
            this.mapper = mapper;
            this.actual = actual;
        }

        @Override
        public void onSubscribe(Subscription s) {
            actual.onSubscribe(s);
        }

        @Override
        public void onNext(Object o) {
            // 当上游数据发送过来时，先进行转换再发送给下游
            Object result = mapper.apply(o);
            actual.onNext(result);
        }

        @Override
        public void onError(Throwable t) {
            actual.onError(t);
        }

        @Override
        public void onComplete() {
            actual.onComplete();
        }
    }

}
