package com.github.taven;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author tianwen.yin
 */
public class ProducerAndConsumer {

    public static void main(String[] args) throws InterruptedException {
        Flux.create(fluxSink -> {
            while (true) {
                System.out.println("FluxSink:" + Thread.currentThread() + ", " + new Date());
                fluxSink.next("A");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        })
        .subscribeOn(Schedulers.single())
        .publishOn(Schedulers.parallel())
        .subscribe(obj -> {
            System.out.println("consumer:" + Thread.currentThread() + ", " + new Date());
        });


        new CountDownLatch(1).await();
    }

}
