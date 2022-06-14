package com.github.taven;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;

import java.util.function.Function;

/**
 * @author tianwen.yin
 */
public class HooksExample {

    public static void main(String[] args) {
        Hooks.onEachOperator("HOOK_KEY", onEachOperator());

        Mono.just("a")
                .map(s -> s + ",sb")
                .subscribe(System.out::println);

    }

    public static Function<? super Publisher<Object>, ? extends Publisher<Object>> onEachOperator() {
        return publisher -> {
            return Operators.liftPublisher( (source, actual) -> {
                return new LogSubscriber(source, actual);
            }).apply(publisher);
        };
    }

    public static class LogSubscriber implements CoreSubscriber<Object> {
        Publisher<? super Object> source;
        CoreSubscriber<? super Object> actual;

        public LogSubscriber(Publisher<? super Object> source, CoreSubscriber<? super Object> actual) {
            this.source = source;
            this.actual = actual;
        }

        @Override
        public void onSubscribe(Subscription s) {
            System.out.println(source.getClass().getSimpleName() + " onSubscribe");
            actual.onSubscribe(s);
        }

        @Override
        public void onNext(Object value) {
            System.out.println(source.getClass().getSimpleName() + " onNext, value: " + value);
            actual.onNext(value);
        }

        @Override
        public void onError(Throwable t) {
            System.out.println(source.getClass().getSimpleName() + " onError, Throwable: " + t);
            actual.onError(t);
        }

        @Override
        public void onComplete() {
            System.out.println(source.getClass().getSimpleName() + " onComplete");
            actual.onComplete();
        }
    }

}
