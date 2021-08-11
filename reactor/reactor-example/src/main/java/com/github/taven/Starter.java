package com.github.taven;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Starter {
    static Logger logger = Logger.getGlobal();


    public static void main(String[] args) {
        List<Long> longs = Arrays.asList(1L, 2L, 3L, 4L);

        Flux.fromIterable(longs)
                .mergeWith(Flux.interval(Duration.ofSeconds(1)))
                .doOnNext(Starter::observer)
                .map(d -> d * 2)
                .take(100)
//                .onErrorResume(Starter::fallback)
                .doAfterTerminate(Starter::afterTerminate)
                .subscribe(Starter::subscribe);

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static void subscribe(Long l) {
        logger.info(l.toString());
    }

    private static void observer(Long l) {

    }

    private static Publisher<Long> fallback(Throwable throwable) {
        return null;
    }

    private static void afterTerminate() {
        System.out.println("afterTerminate");
    }

}
