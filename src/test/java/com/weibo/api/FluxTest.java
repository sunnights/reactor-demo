package com.weibo.api;

import com.weibo.api.reactor.Flux;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class FluxTest {

    @Test
    public void fluxArrayTest() {
        Flux.just(1, 2, 3, 4, 5)
                .subscribe(new MySubscriber<>());
    }

    @Test
    public void fluxMapTest() {
        Flux.just(1, 2, 3, 4, 5)
                .map(integer -> "s" + integer)
                .subscribe(new MySubscriber<>());
    }

    @Test
    public void fluxLambdaSubscriberTest() {
        Flux.just(1, 2, 3, 4, 5)
                .map(integer -> "s" + integer)
                .subscribe(t -> System.out.println("onNext:" + t),
                        throwable -> System.out.println("onError"),
                        () -> System.out.println("onComplete"),
                        subscription -> {
                            System.out.println("onSubscribe");
                            subscription.request(6);
                        });
    }

    class MySubscriber<T> implements Subscriber<T> {

        @Override
        public void onSubscribe(Subscription s) {
            System.out.println("onSubscribe");
            s.request(6);
        }

        @Override
        public void onNext(T t) {
            System.out.println("onNext: " + t);
        }

        @Override
        public void onError(Throwable t) {
            System.out.println("onError");
        }

        @Override
        public void onComplete() {
            System.out.println("onComplete");
        }
    }

}