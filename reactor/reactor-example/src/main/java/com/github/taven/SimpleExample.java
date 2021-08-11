package com.github.taven;

import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Consumer;

public class SimpleExample {

    public static void main(String[] args) {
//        monoExample();
//        FluxGenerate();
//        FluxCreate();
//        FluxMap();
//        FluxBuffer();
//        FluxFilter();
//        FluxTake();
//        FluxZipWith();
//        FluxMultiThread();
//        justAndThen();
//        MultipleSubscribe();
//        simplePublishAndSubscribe();
        combineFluxAndMono();
    }

    private static void combineFluxAndMono() {
        Mono<String> mono1 = Mono.just("x");
        Flux<String> flux1 = Flux.just("{1}", "{2}", "{3}", "{4}");
        flux1.flatMap(x -> mono1.map(m -> x+m)).subscribe(System.out::println);
    }

    private static void simplePublishAndSubscribe() {
        Mono.just("just").subscribe(new CoreSubscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(1);
            }

            @Override
            public void onNext(String s) {
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

    private static void MultipleSubscribe() {
        Mono<String> mono = Mono.defer(() -> {
            System.out.println("hhhh");
            return Mono.just("yyyy");
        });

        mono.subscribe(System.out::println);
        mono.subscribe(System.out::println);
    }

    private static void justAndThen() {
        Mono.just("just")
                .then(Mono.just("then"))
                .then(Mono.just("then2"))
                .concatWith(Mono.just("concat"))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String x) {
                        System.out.println(x);
                    }
                });
    }


    private static void FluxMultiThread() {
        Flux.create(sink -> {
            System.out.println("create:"+Thread.currentThread().getName());
            sink.next(Thread.currentThread().getName());
            sink.complete();
        })
                .subscribeOn(Schedulers.parallel())
                .publishOn(Schedulers.single())
                .subscribe(x -> {
                    System.out.println("subscribe:"+Thread.currentThread().getName());
                });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void FluxZipWith() {
//        Flux.just("I", "You")
//                .zipWith(Flux.just("Win", "Lose"))
//                .subscribe(System.out::println);
//        Flux.just("I", "You")
//                .zipWith(Flux.just("Win", "Lose"),
//                        (s1, s2) -> String.format("%s!%s!", s1, s2))
//                .subscribe(System.out::println);
    }

    private static void FluxTake() {
        Flux.range(1, 10).take(2).subscribe(System.out::println);
        Flux.range(1, 10).takeLast(2).subscribe(System.out::println);
        Flux.range(1, 10).takeWhile(i -> i < 5).subscribe(System.out::println);
        Flux.range(1, 10).takeUntil(i -> i == 6).subscribe(System.out::println);
    }

    private static void FluxFilter() {
        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
    }

    private static void FluxBuffer() {
        Flux.range(1, 100).buffer(20).subscribe(x -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(x);
        });
        Flux.interval(Duration.of(0, ChronoUnit.SECONDS),
                Duration.of(1, ChronoUnit.SECONDS))
                .buffer(Duration.of(5, ChronoUnit.SECONDS)).
                take(2).toStream().forEach(System.out::println);
        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0)
                .subscribe(System.out::println);
        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0)
                .subscribe(System.out::println);
    }

    private static void FluxMap() {
        Flux.create(sink -> {
            SimpleObject simpleObject = new SimpleObject("simple", 20);
            sink.next(simpleObject);
            sink.complete();
        }).doOnNext(System.out::println)
//                .map(so -> ((SimpleObject) so).name)
                .flatMap(so -> Flux.just(((SimpleObject) so).name))
                .subscribe(System.out::println);
    }

    private static class SimpleObject {
        String name;
        int age;

        public SimpleObject(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "SimpleObject{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    private static void monoExample() {
        Mono.fromSupplier(() -> "Mono1").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("Mono2")).subscribe(System.out::println);
        Mono.create(sink -> sink.success("Mono3")).subscribe(System.out::println);

        Mono.fromRunnable(() -> System.out.println("aaa")).subscribe();
    }

    private static void FluxCreate() {
        Flux.create(sink -> {
            for (char i = 'a'; i <= 'z'; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::print);
    }

    private static void FluxGenerate() {
        Flux.generate(sink -> {
            sink.next("Echo");
            sink.complete();
        }).subscribe(System.out::println);
    }
}
